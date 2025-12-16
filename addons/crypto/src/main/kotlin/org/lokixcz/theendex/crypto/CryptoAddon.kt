package org.lokixcz.theendex.crypto

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.InventoryView
import org.bukkit.plugin.java.JavaPlugin
import org.lokixcz.theendex.Endex
import org.lokixcz.theendex.addon.EndexAddon
import org.lokixcz.theendex.addon.AddonSubcommandHandler
import org.lokixcz.theendex.addon.AddonTabCompleter
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.util.*
import kotlin.collections.HashMap

class CryptoAddon : EndexAddon, Listener {
    override fun id() = "crypto"
    override fun name() = "Crypto Addon"
    override fun version() = "1.0.0"

    private lateinit var host: Endex
    private var eco: Economy? = null

    // Config/state
    private data class Config(
        val enabled: Boolean,
        val name: String,
        val symbol: String,
        val decimals: Int,
        val priceFixed: BigDecimal,
        val priceMode: String, // fixed | market
        val priceBase: BigDecimal,
        val priceMin: BigDecimal,
        val priceMax: BigDecimal,
        val priceSensitivity: BigDecimal,
        val feeBuyPct: BigDecimal,
        val feeSellPct: BigDecimal,
        val feeTransferPct: BigDecimal,
        val feeSink: String, // burn | treasury
        // Supply model
        val supplyModel: String, // fixed | inflation | halving
        val supplyCap: BigDecimal,
        val emissionPerMinute: BigDecimal,
        val halvingMinutes: Int,
        // AMM + staking + airdrops
        val ammEnabled: Boolean,
        val ammFeePct: BigDecimal,
        val stakingEnabled: Boolean,
        val stakeRatePctPerDay: BigDecimal,
        val airdropsEnabled: Boolean,
        val dailyBuyCap: BigDecimal,
        val dailySellCap: BigDecimal,
        val tradeCooldownSeconds: Int,
        val aliases: List<String>,
        val refundOnFailure: Boolean
    )

    private var cfg: Config = Config(
        false,
        "",
        "",
        2,
        BigDecimal.ONE,
        "fixed",
        BigDecimal.ONE,
        BigDecimal("0.01"),
        BigDecimal("1000000"),
        BigDecimal("0.05"), // 5% sensitivity per full-cap trade unit
        bd(1), bd(1), bd(0),
        "burn",
        // Supply
        "fixed",
        BigDecimal("100000000"),
        BigDecimal("0"),
        10080,
        // AMM / staking / airdrops
        true,
        BigDecimal("0.30"),
        true,
        BigDecimal("2.0"),
        false,
        bd(100000), bd(100000),
        10,
        listOf("cc","crypto"),
        false
    )

    private val balances = HashMap<UUID, BigDecimal>()
    private var lastTrade: MutableMap<UUID, Long> = HashMap()
    private var dailyCountersDate: LocalDate = LocalDate.now()
    private var dailyBuy: MutableMap<UUID, BigDecimal> = HashMap()
    private var dailySell: MutableMap<UUID, BigDecimal> = HashMap()
    private var currentPrice: BigDecimal = BigDecimal.ONE
    private var historyTaskId: Int = -1
    private var tickTaskId: Int = -1
    // Supply & sinks
    private var totalSupply: BigDecimal = BigDecimal.ZERO
    private var burnedTotal: BigDecimal = BigDecimal.ZERO
    private var treasuryBalance: BigDecimal = BigDecimal.ZERO
    // AMM pool
    private data class AmmPool(
        var tokenReserves: BigDecimal,
        var moneyReserves: BigDecimal,
        var lpSharesTotal: BigDecimal
    )
    private var pool = AmmPool(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)
    private val lpShares: MutableMap<UUID, BigDecimal> = HashMap()

    // Shop configuration (YAML-driven)
    private data class ShopItem(
        val slot: Int,
        val material: Material,
        val name: String,
        val lore: List<String>,
        val price: BigDecimal,
        val commands: List<String>,
        val runAs: String,
        val permission: String?
    )
    private data class Shop(val title: String, val size: Int, val items: List<ShopItem>)
    private var shop: Shop? = null
    private val slotToItem: MutableMap<Int, ShopItem> = HashMap()
    // Staking
    private data class StakeEntry(var amount: BigDecimal, var startMs: Long, var endMs: Long, var claimed: BigDecimal)
    private val stakes: MutableMap<UUID, MutableList<StakeEntry>> = HashMap()
    private val claimable: MutableMap<UUID, BigDecimal> = HashMap()
    // Orders
    private enum class Side { BUY, SELL }
    private enum class OrderType { LIMIT, STOP }
    private data class Order(
        val id: Long,
        val owner: UUID,
        val side: Side,
        var type: OrderType,
        var price: BigDecimal?,
        var remaining: BigDecimal,
        val trigger: BigDecimal? = null
    )
    private val orderBook: MutableList<Order> = mutableListOf()
    private var nextOrderId: Long = 1

    private fun bd(v: Int) = BigDecimal(v)
    private fun scale(v: BigDecimal) = v.setScale(cfg.decimals, RoundingMode.HALF_UP)
    private fun parseAmountFromJson(body: String): BigDecimal {
        return try {
            val regex = Regex("\"amount\"\\s*:\\s*\"?([0-9]+(?:\\.[0-9]+)?)\"?", RegexOption.IGNORE_CASE)
            val m = regex.find(body)
            val s = m?.groupValues?.get(1)
            val v = s?.toBigDecimalOrNull() ?: BigDecimal.ZERO
            scale(v)
        } catch (_: Throwable) {
            BigDecimal.ZERO
        }
    }
    private fun <T> callSync(action: () -> T): T {
        return if (Bukkit.isPrimaryThread()) action() else {
            val fut = host.server.scheduler.callSyncMethod(host, java.util.concurrent.Callable { action() })
            fut.get()
        }
    }

    override fun init(plugin: Endex) {
        host = plugin
        eco = plugin.economy

        // Load settings and data
        loadConfig()
        if (!cfg.enabled) {
            host.logger.warning("[Crypto] Disabled: set enabled: true and non-empty name/symbol in addons/settings/crypto/config.yml")
            return
        }
        if (cfg.name.isBlank() || cfg.symbol.isBlank() || cfg.decimals < 0 || cfg.decimals > 8) {
            host.logger.warning("[Crypto] Invalid name/symbol/decimals; disabling addon")
            return
        }
    loadBalances()
    // Ensure persisted state for new features is loaded
    loadSupply()
    loadPool()
    loadStakes()
    // Load shop config
    loadShop()

        // Register listeners and commands
        host.server.pluginManager.registerEvents(this, host)
        // Register in web Addons tab (name displayed)
        runCatching { host.getWebServer()?.registerAddonNav(name()) }
        // Web endpoints for Crypto addon
        // Use WebServer addon helpers (session-aware) to avoid direct Javalin dependency
        runCatching {
            // Tickers — summary
            host.getWebServer()?.addGet("/api/crypto/tickers", true) { _ ->
                val price = getUnitPrice()
                listOf(
                    mapOf(
                        "symbol" to cfg.symbol,
                        "name" to cfg.name,
                        "price" to price.toPlainString(),
                        "supply" to totalSupply.toPlainString(),
                        "treasury" to treasuryBalance.toPlainString(),
                        "burned" to burnedTotal.toPlainString(),
                        "ammEnabled" to cfg.ammEnabled,
                        "pool" to mapOf(
                            "token" to pool.tokenReserves.toPlainString(),
                            "money" to pool.moneyReserves.toPlainString(),
                            "lpTotal" to pool.lpSharesTotal.toPlainString()
                        )
                    )
                )
            }
            // Trades: naive tail of audit.log (fixed last 50 lines with BUY/SELL)
            host.getWebServer()?.addGet("/api/crypto/trades", true) { _ ->
                val f = logFile()
                if (!f.exists()) return@addGet emptyList<Any>()
                val lines = runCatching { f.readLines() }.getOrDefault(emptyList())
                val filtered = lines.asReversed().asSequence()
                    .filter { (it.contains(" BUY ") || it.contains(" SELL ")) && !it.contains("\${") }
                    .take(50)
                    .toList()
                filtered.asReversed().map { line -> mapOf("entry" to line) }
            }
            // History: parse price-history.csv
            host.getWebServer()?.addGet("/api/crypto/history", true) { _ ->
                val f = historyFile(); if (!f.exists()) return@addGet emptyList<Map<String, Any>>()
                val rows = runCatching { f.readLines() }.getOrDefault(emptyList())
                rows.mapNotNull { ln ->
                    val parts = ln.split(",")
                    val ts = parts.getOrNull(0)?.toLongOrNull() ?: return@mapNotNull null
                    val p = parts.getOrNull(1)?.toBigDecimalOrNull() ?: return@mapNotNull null
                    mapOf("timestamp" to (ts / 1000), "price" to p.toPlainString())
                }
            }
            // Holdings: token balances for the authed player
            host.getWebServer()?.addGet("/api/crypto/holdings", true) { session ->
                val id = session!!.playerUuid
                val bal = getBal(id)
                listOf(mapOf("symbol" to cfg.symbol, "name" to cfg.name, "balance" to bal.toPlainString()))
            }
            // Receipts: last 50 audit entries for this player
            host.getWebServer()?.addGet("/api/crypto/receipts", true) { session ->
                val f = logFile()
                if (!f.exists()) return@addGet emptyList<Any>()
                val name = session!!.playerName
                val lines = runCatching { f.readLines() }.getOrDefault(emptyList())
                val mine = lines.asReversed().asSequence()
                    .filter { it.contains(" $name ") && !it.contains("\${") }
                    .take(50)
                    .toList()
                mine.asReversed().map { line -> mapOf("entry" to line) }
            }
            // Trading over web: buy/sell (uses Vault and internal balance). Body: { amount: "123.45" }
            host.getWebServer()?.addPost("/api/crypto/buy") { session, body ->
                val p = Bukkit.getPlayer(session.playerUuid) ?: throw IllegalStateException("Player must be online to trade")
                val amt = parseAmountFromJson(body)
                if (amt <= BigDecimal.ZERO) return@addPost mapOf("error" to "Invalid amount")
                if (!checkDaily(p.uniqueId, amt, true)) return@addPost mapOf("error" to "Daily buy cap reached")
                val unit = getTradePriceBuy()
                val cost = amt.multiply(unit)
                val econ = eco ?: return@addPost mapOf("error" to "Economy not available")
                if (!callSync { econ.has(p, cost.toDouble()) }) return@addPost mapOf("error" to "Insufficient funds")
                val fee = amt.multiply(cfg.feeBuyPct).divide(BigDecimal(100))
                val net = amt.subtract(fee)
                val withdraw = callSync { econ.withdrawPlayer(p, cost.toDouble()) }
                if (!withdraw.transactionSuccess()) {
                    return@addPost mapOf("error" to (withdraw.errorMessage ?: "Payment failed"))
                }
                addBal(p.uniqueId, net)
                creditFeeSink(fee)
                addDaily(p.uniqueId, amt, true)
                setCooldown(p)
                maybeAdjustPrice(amt, true)
                log("BUY ${p.name} amount=$amt fee=$fee cost=$cost price=$unit")
                mapOf("ok" to true, "price" to unit.toPlainString(), "cost" to cost.toPlainString(), "net" to net.toPlainString())
            }
            host.getWebServer()?.addPost("/api/crypto/sell") { session, body ->
                val p = Bukkit.getPlayer(session.playerUuid) ?: throw IllegalStateException("Player must be online to trade")
                val amt = parseAmountFromJson(body)
                if (amt <= BigDecimal.ZERO) return@addPost mapOf("error" to "Invalid amount")
                if (!checkDaily(p.uniqueId, amt, false)) return@addPost mapOf("error" to "Daily sell cap reached")
                if (!hasBal(p.uniqueId, amt)) return@addPost mapOf("error" to "Insufficient ${'$'}{cfg.symbol}")
                val unit = getTradePriceSell()
                val payout = amt.multiply(unit)
                val fee = amt.multiply(cfg.feeSellPct).divide(BigDecimal(100))
                val econ = eco ?: return@addPost mapOf("error" to "Economy not available")
                val deposit = callSync { econ.depositPlayer(p, payout.toDouble()) }
                if (!deposit.transactionSuccess()) {
                    return@addPost mapOf("error" to (deposit.errorMessage ?: "Payout failed"))
                }
                // Deduct full amount from user's token balance; fee goes to sink (burn/treasury)
                addBal(p.uniqueId, amt.negate())
                creditFeeSink(fee)
                addDaily(p.uniqueId, amt, false)
                setCooldown(p)
                maybeAdjustPrice(amt, false)
                log("SELL ${p.name} amount=$amt fee=$fee payout=$payout price=$unit")
                mapOf("ok" to true, "price" to unit.toPlainString(), "payout" to payout.toPlainString())
            }
        }
    schedulePriceHistoryWriter()
    scheduleTick()
    host.registerAddonSubcommand("crypto", AddonSubcommandHandler { sender: CommandSender, _: String, args: Array<out String> ->
            if (args.isEmpty()) {
                sender.sendMessage("${ChatColor.GOLD}[${cfg.name}] ${ChatColor.AQUA}Use /endex crypto help")
                return@AddonSubcommandHandler true
            }
            when (args[0].lowercase()) {
                "help" -> {
                    sender.sendMessage("${ChatColor.GOLD}[${cfg.name}] ${ChatColor.AQUA}Commands:")
                    sender.sendMessage("${ChatColor.AQUA}/endex crypto info${ChatColor.GRAY} — Token details and price")
                    sender.sendMessage("${ChatColor.AQUA}/endex crypto balance${ChatColor.GRAY} — View your token balance")
                    sender.sendMessage("${ChatColor.AQUA}/endex crypto buy <amount>${ChatColor.GRAY} — Buy ${cfg.symbol}")
                    sender.sendMessage("${ChatColor.AQUA}/endex crypto sell <amount>${ChatColor.GRAY} — Sell ${cfg.symbol}")
                    sender.sendMessage("${ChatColor.AQUA}/endex crypto transfer <player> <amount>${ChatColor.GRAY} — Transfer ${cfg.symbol}")
                    sender.sendMessage("${ChatColor.AQUA}/endex crypto shop${ChatColor.GRAY} — Open shop GUI")
                    if (sender.hasPermission("theendex.crypto.admin")) {
                        sender.sendMessage("${ChatColor.AQUA}/endex crypto admin setprice <value>${ChatColor.GRAY}")
                        sender.sendMessage("${ChatColor.AQUA}/endex crypto admin mint|burn <player> <amount>${ChatColor.GRAY}")
                        sender.sendMessage("${ChatColor.AQUA}/endex crypto admin give|take <player> <amount>${ChatColor.GRAY}")
                        sender.sendMessage("${ChatColor.AQUA}/endex crypto admin limits set|get${ChatColor.GRAY}")
                        sender.sendMessage("${ChatColor.AQUA}/endex crypto admin reload${ChatColor.GRAY}")
                        sender.sendMessage("${ChatColor.AQUA}/endex crypto admin pool add <tokens> <money>${ChatColor.GRAY}")
                        sender.sendMessage("${ChatColor.AQUA}/endex crypto admin pool remove <shares>${ChatColor.GRAY}")
                    }
                    true
                }
                "stake" -> {
                    if (sender !is Player) return@AddonSubcommandHandler true
                    if (!cfg.stakingEnabled) { sender.sendMessage("${ChatColor.RED}Staking disabled"); return@AddonSubcommandHandler true }
                    val amount = args.getOrNull(1)?.toBigDecimalOrNull()?.let { scale(it) } ?: run { sender.sendMessage("${ChatColor.RED}Usage: /endex crypto stake <amount> <days>"); return@AddonSubcommandHandler true }
                    val days = args.getOrNull(2)?.toIntOrNull() ?: run { sender.sendMessage("${ChatColor.RED}Usage: /endex crypto stake <amount> <days>"); return@AddonSubcommandHandler true }
                    if (!hasBal(sender.uniqueId, amount)) { sender.sendMessage("${ChatColor.RED}Insufficient ${cfg.symbol}"); return@AddonSubcommandHandler true }
                    addBal(sender.uniqueId, amount.negate())
                    val now = System.currentTimeMillis()
                    val end = now + days * 86400000L
                    val list = stakes.getOrPut(sender.uniqueId) { mutableListOf() }
                    list.add(StakeEntry(amount, now, end, BigDecimal.ZERO))
                    saveStakes()
                    sender.sendMessage("${ChatColor.GREEN}Staked ${format(amount)} for ${days}d")
                    true
                }
                "claim" -> {
                    if (sender !is Player) return@AddonSubcommandHandler true
                    val amt = claimable.getOrDefault(sender.uniqueId, BigDecimal.ZERO)
                    if (amt <= BigDecimal.ZERO) { sender.sendMessage("${ChatColor.RED}Nothing to claim"); return@AddonSubcommandHandler true }
                    addBal(sender.uniqueId, amt)
                    claimable[sender.uniqueId] = BigDecimal.ZERO
                    saveStakes()
                    sender.sendMessage("${ChatColor.GREEN}Claimed ${format(amt)} rewards")
                    true
                }
                "unstake" -> {
                    if (sender !is Player) return@AddonSubcommandHandler true
                    val now = System.currentTimeMillis()
                    val list = stakes[sender.uniqueId] ?: run { sender.sendMessage("${ChatColor.RED}No stakes"); return@AddonSubcommandHandler true }
                    var total = BigDecimal.ZERO
                    val remain = mutableListOf<StakeEntry>()
                    for (s in list) { if (s.endMs <= now) total = total.add(s.amount) else remain.add(s) }
                    stakes[sender.uniqueId] = remain
                    if (total > BigDecimal.ZERO) addBal(sender.uniqueId, total)
                    saveStakes()
                    sender.sendMessage("${ChatColor.GREEN}Unstaked ${format(total)}")
                    true
                }
                "limit" -> {
                    if (sender !is Player) return@AddonSubcommandHandler true
                    val side = when (args.getOrNull(1)?.lowercase()) { "buy" -> Side.BUY; "sell" -> Side.SELL; else -> { sender.sendMessage("${ChatColor.RED}Usage: /endex crypto limit <buy|sell> <amount> <price>"); return@AddonSubcommandHandler true } }
                    val amount = args.getOrNull(2)?.toBigDecimalOrNull()?.let { scale(it) } ?: run { sender.sendMessage("${ChatColor.RED}Invalid amount"); return@AddonSubcommandHandler true }
                    val price = args.getOrNull(3)?.toBigDecimalOrNull()?.let { scale(it) } ?: run { sender.sendMessage("${ChatColor.RED}Invalid price"); return@AddonSubcommandHandler true }
                    val id = placeOrder(sender.uniqueId, side, OrderType.LIMIT, amount, price, null)
                    sender.sendMessage("${ChatColor.YELLOW}Placed LIMIT ${side.name} order #${id} for ${format(amount)} @ ${format(price)}")
                    processOrders(); true
                }
                "stop" -> {
                    if (sender !is Player) return@AddonSubcommandHandler true
                    val side = when (args.getOrNull(1)?.lowercase()) { "buy" -> Side.BUY; "sell" -> Side.SELL; else -> { sender.sendMessage("${ChatColor.RED}Usage: /endex crypto stop <buy|sell> <amount> <trigger>"); return@AddonSubcommandHandler true } }
                    val amount = args.getOrNull(2)?.toBigDecimalOrNull()?.let { scale(it) } ?: run { sender.sendMessage("${ChatColor.RED}Invalid amount"); return@AddonSubcommandHandler true }
                    val trigger = args.getOrNull(3)?.toBigDecimalOrNull()?.let { scale(it) } ?: run { sender.sendMessage("${ChatColor.RED}Invalid trigger"); return@AddonSubcommandHandler true }
                    val id = placeOrder(sender.uniqueId, side, OrderType.STOP, amount, null, trigger)
                    sender.sendMessage("${ChatColor.YELLOW}Placed STOP ${side.name} order #${id} for ${format(amount)} trigger ${format(trigger)}")
                    true
                }
                "cancel" -> {
                    if (sender !is Player) return@AddonSubcommandHandler true
                    val id = args.getOrNull(1)?.toLongOrNull() ?: run { sender.sendMessage("${ChatColor.RED}Usage: /endex crypto cancel <orderId>"); return@AddonSubcommandHandler true }
                    val removed = cancelOrder(sender.uniqueId, id)
                    sender.sendMessage(if (removed) "${ChatColor.YELLOW}Cancelled order #$id" else "${ChatColor.RED}Order not found")
                    true
                }
                "info" -> {
                    if (!sender.hasPermission("theendex.crypto.info")) { sender.sendMessage("${ChatColor.RED}No permission."); return@AddonSubcommandHandler true }
                    val mode = cfg.priceMode
                    val price = getUnitPrice()
                    sender.sendMessage("${ChatColor.GOLD}[${cfg.name}] ${ChatColor.AQUA}${cfg.symbol} Info")
                    sender.sendMessage("${ChatColor.GRAY}Mode: ${ChatColor.YELLOW}${mode.uppercase()}")
                    if (mode == "market") {
                        sender.sendMessage("${ChatColor.GRAY}Base: ${ChatColor.YELLOW}${cfg.priceBase}  Sensitivity: ${cfg.priceSensitivity}")
                        sender.sendMessage("${ChatColor.GRAY}Range: ${ChatColor.YELLOW}${cfg.priceMin} - ${cfg.priceMax}")
                    }
                    sender.sendMessage("${ChatColor.GRAY}Current Price: ${ChatColor.AQUA}${format(price)}")
                    sender.sendMessage("${ChatColor.GRAY}Supply: ${ChatColor.YELLOW}${format(totalSupply)}  Burned: ${format(burnedTotal)}  Treasury: ${format(treasuryBalance)}")
                    sender.sendMessage("${ChatColor.GRAY}Fees: buy ${cfg.feeBuyPct}%  sell ${cfg.feeSellPct}%  transfer ${cfg.feeTransferPct}%")
                    sender.sendMessage("${ChatColor.GRAY}Limits: buy ${cfg.dailyBuyCap}  sell ${cfg.dailySellCap}  cooldown ${cfg.tradeCooldownSeconds}s")
                    if (sender is Player) {
                        val b = dailyBuy.getOrDefault(sender.uniqueId, BigDecimal.ZERO)
                        val s = dailySell.getOrDefault(sender.uniqueId, BigDecimal.ZERO)
                        sender.sendMessage("${ChatColor.GRAY}Your 24h usage → buy: ${format(b)} / ${cfg.dailyBuyCap}, sell: ${format(s)} / ${cfg.dailySellCap}")
                    }
                    true
                }
                "balance" -> {
                    if (sender !is Player) return@AddonSubcommandHandler true
                    val bal = getBal(sender.uniqueId)
                    sender.sendMessage("${ChatColor.GOLD}[${cfg.name}] ${ChatColor.AQUA}Balance: ${format(bal)} ${cfg.symbol}")
                    true
                }
                "buy" -> {
                    if (sender !is Player) return@AddonSubcommandHandler true
                    if (!sender.hasPermission("theendex.crypto.buy")) { sender.sendMessage("${ChatColor.RED}No permission."); return@AddonSubcommandHandler true }
                    val amount = args.getOrNull(1)?.toBigDecimalOrNull()?.let { scale(it) } ?: run { sender.sendMessage("${ChatColor.RED}Invalid amount"); return@AddonSubcommandHandler true }
                    if (!checkCooldown(sender)) return@AddonSubcommandHandler true
                    if (!checkDaily(sender.uniqueId, amount, true)) { sender.sendMessage("${ChatColor.RED}Daily buy cap reached"); return@AddonSubcommandHandler true }
                    val unitPrice = getTradePriceBuy()
                    val cost = amount.multiply(unitPrice)
                    val fee = amount.multiply(cfg.feeBuyPct).divide(BigDecimal(100))
                    val net = amount.subtract(fee)
                    val econ = eco
                    if (econ == null) { sender.sendMessage("${ChatColor.RED}Economy not available"); return@AddonSubcommandHandler true }
                    if (!econ.has(sender, cost.toDouble())) { sender.sendMessage("${ChatColor.RED}Not enough money (${cost})"); return@AddonSubcommandHandler true }
                    val rsp = econ.withdrawPlayer(sender, cost.toDouble())
                    if (!rsp.transactionSuccess()) {
                        sender.sendMessage("${ChatColor.RED}Payment failed: ${rsp.errorMessage ?: "unknown"}")
                        return@AddonSubcommandHandler true
                    }
                    addBal(sender.uniqueId, net)
                    creditFeeSink(fee)
                    addDaily(sender.uniqueId, amount, true)
                    setCooldown(sender)
                    maybeAdjustPrice(amount, true)
                    sender.sendMessage("${ChatColor.GREEN}Bought ${format(net)} ${cfg.symbol} @ ${format(unitPrice)} (fee ${format(fee)}) for ${cost}")
                    log("BUY ${sender.name} amount=${amount} fee=${fee} cost=${cost} price=${unitPrice}")
                    true
                }
                "sell" -> {
                    if (sender !is Player) return@AddonSubcommandHandler true
                    if (!sender.hasPermission("theendex.crypto.sell")) { sender.sendMessage("${ChatColor.RED}No permission."); return@AddonSubcommandHandler true }
                    val amount = args.getOrNull(1)?.toBigDecimalOrNull()?.let { scale(it) } ?: run { sender.sendMessage("${ChatColor.RED}Invalid amount"); return@AddonSubcommandHandler true }
                    if (!checkCooldown(sender)) return@AddonSubcommandHandler true
                    if (!checkDaily(sender.uniqueId, amount, false)) { sender.sendMessage("${ChatColor.RED}Daily sell cap reached"); return@AddonSubcommandHandler true }
                    val fee = amount.multiply(cfg.feeSellPct).divide(BigDecimal(100))
                    if (!hasBal(sender.uniqueId, amount)) { sender.sendMessage("${ChatColor.RED}Insufficient ${cfg.symbol}"); return@AddonSubcommandHandler true }
                    val unitPrice = getTradePriceSell()
                    val payout = amount.multiply(unitPrice)
                    val econ = eco
                    if (econ == null) { sender.sendMessage("${ChatColor.RED}Economy not available"); return@AddonSubcommandHandler true }
                    val rsp = econ.depositPlayer(sender, payout.toDouble())
                    if (!rsp.transactionSuccess()) {
                        sender.sendMessage("${ChatColor.RED}Payout failed: ${rsp.errorMessage ?: "unknown"}")
                        return@AddonSubcommandHandler true
                    }
                    // Deduct full amount from user; fee goes to sink
                    addBal(sender.uniqueId, amount.negate())
                    creditFeeSink(fee)
                    addDaily(sender.uniqueId, amount, false)
                    setCooldown(sender)
                    maybeAdjustPrice(amount, false)
                    sender.sendMessage("${ChatColor.GREEN}Sold ${format(amount)} ${cfg.symbol} @ ${format(unitPrice)} (fee ${format(fee)}) for ${payout}")
                    log("SELL ${sender.name} amount=${amount} fee=${fee} payout=${payout} price=${unitPrice}")
                    true
                }
                "transfer" -> {
                    if (sender !is Player) return@AddonSubcommandHandler true
                    if (!sender.hasPermission("theendex.crypto.transfer")) { sender.sendMessage("${ChatColor.RED}No permission."); return@AddonSubcommandHandler true }
                    val targetName = args.getOrNull(1) ?: run { sender.sendMessage("${ChatColor.RED}Usage: /endex crypto transfer <player> <amount>"); return@AddonSubcommandHandler true }
                    val amount = args.getOrNull(2)?.toBigDecimalOrNull()?.let { scale(it) } ?: run { sender.sendMessage("${ChatColor.RED}Invalid amount"); return@AddonSubcommandHandler true }
                    val target = Bukkit.getPlayerExact(targetName) ?: run { sender.sendMessage("${ChatColor.RED}Player not found"); return@AddonSubcommandHandler true }
                    if (!hasBal(sender.uniqueId, amount)) { sender.sendMessage("${ChatColor.RED}Insufficient ${cfg.symbol}"); return@AddonSubcommandHandler true }
                    val fee = amount.multiply(cfg.feeTransferPct).divide(BigDecimal(100))
                    val net = amount.subtract(fee)
                    addBal(sender.uniqueId, amount.negate()) // fee burned from sender
                    addBal(target.uniqueId, net)
                    creditFeeSink(fee)
                    sender.sendMessage("${ChatColor.GREEN}Transferred ${format(net)} ${cfg.symbol} to ${target.name} (fee ${format(fee)})")
                    target.sendMessage("${ChatColor.GREEN}Received ${format(net)} ${cfg.symbol} from ${sender.name}")
                    log("XFER ${sender.name}->${target.name} amount=${amount} fee=${fee}")
                    true
                }
                "shop" -> {
                    if (sender !is Player) return@AddonSubcommandHandler true
                    if (!sender.hasPermission("theendex.crypto.shop")) { sender.sendMessage("${ChatColor.RED}No permission."); return@AddonSubcommandHandler true }
                    openShop(sender)
                    true
                }
                "admin" -> {
                    if (!sender.hasPermission("theendex.crypto.admin")) { sender.sendMessage("${ChatColor.RED}No permission."); return@AddonSubcommandHandler true }
                    when (args.getOrNull(1)?.lowercase()) {
                        "setprice" -> {
                            val v = args.getOrNull(2)?.toBigDecimalOrNull() ?: run { sender.sendMessage("${ChatColor.RED}Usage: /endex crypto admin setprice <value>"); return@AddonSubcommandHandler true }
                            cfg = cfg.copy(priceFixed = scale(v), priceBase = scale(v))
                            if (cfg.priceMode == "fixed") currentPrice = cfg.priceFixed else currentPrice = cfg.priceBase
                            saveConfig()
                            sender.sendMessage("${ChatColor.YELLOW}Price set to ${cfg.priceFixed}")
                            true
                        }
                        "mint","give" -> {
                            val player = Bukkit.getPlayerExact(args.getOrNull(2) ?: return@AddonSubcommandHandler true) ?: return@AddonSubcommandHandler true
                            val amt = args.getOrNull(3)?.toBigDecimalOrNull()?.let { scale(it) } ?: return@AddonSubcommandHandler true
                            addBal(player.uniqueId, amt)
                            sender.sendMessage("${ChatColor.YELLOW}Gave ${format(amt)} ${cfg.symbol} to ${player.name}")
                            true
                        }
                        "burn","take" -> {
                            val player = Bukkit.getPlayerExact(args.getOrNull(2) ?: return@AddonSubcommandHandler true) ?: return@AddonSubcommandHandler true
                            val amt = args.getOrNull(3)?.toBigDecimalOrNull()?.let { scale(it) } ?: return@AddonSubcommandHandler true
                            addBal(player.uniqueId, amt.negate())
                            sender.sendMessage("${ChatColor.YELLOW}Took ${format(amt)} ${cfg.symbol} from ${player.name}")
                            true
                        }
                        "limits" -> {
                            when (args.getOrNull(2)?.lowercase()) {
                                "set" -> {
                                    val key = args.getOrNull(3) ?: return@AddonSubcommandHandler true
                                    val value = args.getOrNull(4) ?: return@AddonSubcommandHandler true
                                    when (key.lowercase()) {
                                        "dailybuycap" -> cfg = cfg.copy(dailyBuyCap = scale(value.toBigDecimalOrNull() ?: return@AddonSubcommandHandler true))
                                        "dailysellcap" -> cfg = cfg.copy(dailySellCap = scale(value.toBigDecimalOrNull() ?: return@AddonSubcommandHandler true))
                                        "cooldown" -> cfg = cfg.copy(tradeCooldownSeconds = value.toIntOrNull() ?: return@AddonSubcommandHandler true)
                                    }
                                    saveConfig(); sender.sendMessage("${ChatColor.YELLOW}Updated limits")
                                    true
                                }
                                "get" -> { sender.sendMessage("${ChatColor.YELLOW}Caps: buy ${cfg.dailyBuyCap} sell ${cfg.dailySellCap} cooldown ${cfg.tradeCooldownSeconds}s"); true }
                                else -> true
                            }
                        }
                        "reload" -> { loadConfig(); loadShop(); sender.sendMessage("${ChatColor.YELLOW}Crypto config and shop reloaded"); true }
                        "airdrop" -> {
                            if (!cfg.airdropsEnabled) { sender.sendMessage("${ChatColor.RED}Airdrops disabled"); return@AddonSubcommandHandler true }
                            val amt = args.getOrNull(2)?.toBigDecimalOrNull()?.let { scale(it) } ?: return@AddonSubcommandHandler true
                            val scope = args.getOrNull(3)?.lowercase() ?: "online"
                            val targets: List<UUID> = when (scope) {
                                "all" -> Bukkit.getOnlinePlayers().map { it.uniqueId }
                                else -> Bukkit.getOnlinePlayers().map { it.uniqueId }
                            }
                            targets.forEach { addBal(it, amt) }
                            sender.sendMessage("${ChatColor.YELLOW}Airdropped ${format(amt)} to ${targets.size} players")
                            true
                        }
                        "pool" -> {
                            when (args.getOrNull(2)?.lowercase()) {
                                "add" -> {
                                    val t = args.getOrNull(3)?.toBigDecimalOrNull()?.let { scale(it) } ?: return@AddonSubcommandHandler true
                                    val m = args.getOrNull(4)?.toBigDecimalOrNull()?.let { scale(it) } ?: return@AddonSubcommandHandler true
                                    addLiquidity((sender as? Player)?.uniqueId ?: UUID(0,0), t, m)
                                    sender.sendMessage("${ChatColor.YELLOW}Added liquidity: tokens=${format(t)} money=${format(m)}")
                                    true
                                }
                                "remove" -> {
                                    val s = args.getOrNull(3)?.toBigDecimalOrNull()?.let { scale(it) } ?: return@AddonSubcommandHandler true
                                    val (t,m) = removeLiquidity((sender as? Player)?.uniqueId ?: UUID(0,0), s)
                                    sender.sendMessage("${ChatColor.YELLOW}Removed liquidity -> tokens=${format(t)} money=${format(m)}")
                                    true
                                }
                                else -> { sender.sendMessage("${ChatColor.RED}Usage: /endex crypto admin pool <add|remove> ..."); true }
                            }
                        }
                        else -> { sender.sendMessage("${ChatColor.RED}Usage: /endex crypto admin <setprice|mint|burn|give|take|limits|reload>"); true }
                    }
                }
                else -> false
            }
        })

        // Register aliases as direct commands
        cfg.aliases.forEach { host.registerAddonAlias(it, "crypto") }

        // Register a basic tab completer for crypto
        try {
            val router = host.getAddonCommandRouter()
            router?.registerCompleter("crypto", AddonTabCompleter { sender, _, args ->
                val base = listOf("help","info","balance","buy","sell","transfer","shop","admin")
                return@AddonTabCompleter when (args.size) {
                    0 -> base
                    1 -> base.filter { it.startsWith(args[0], true) }
                    else -> when (args[0].lowercase()) {
                        "buy","sell" -> listOf("10","100","1000").filter { it.startsWith(args.getOrNull(1) ?: "", true) }
                        "transfer" -> emptyList() // Would require player lookups
                        "admin" -> listOf("setprice","mint","burn","give","take","limits","reload").filter { it.startsWith(args.getOrNull(1) ?: "", true) }
                        else -> emptyList()
                    }
                }
            })
        } catch (_: Throwable) {}
    }

    // GUI (YAML-driven)
    private fun openShop(player: Player) {
        val s = shop ?: run {
            player.sendMessage("${ChatColor.RED}Shop not configured.")
            return
        }
        val title = colorize(s.title.replace("{name}", cfg.name).replace("{symbol}", cfg.symbol))
        val inv = Bukkit.createInventory(player, s.size, title)
        slotToItem.clear()
        for (si in s.items) {
            val item = ItemStack(si.material)
            val meta = item.itemMeta
            val displayName = si.name
                .replace("{name}", cfg.name)
                .replace("{symbol}", cfg.symbol)
                .replace("{price}", format(si.price))
            meta.setDisplayName(colorize(displayName))
            val lore = si.lore.map { line ->
                colorize(line
                    .replace("{name}", cfg.name)
                    .replace("{symbol}", cfg.symbol)
                    .replace("{price}", format(si.price))
                )
            }
            meta.lore = lore
            item.itemMeta = meta
            if (si.slot in 0 until s.size) {
                inv.setItem(si.slot, item)
                slotToItem[si.slot] = si
            }
        }
        player.openInventory(inv)
    }

    // Helper to get inventory view title as String for MC 1.21+ compatibility
    private fun getViewTitle(view: InventoryView): String {
        return PlainTextComponentSerializer.plainText().serialize(view.title())
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        val p = e.whoClicked as? Player ?: return
        val s = shop ?: return
        val expectedTitle = ChatColor.stripColor(colorize(s.title.replace("{name}", cfg.name).replace("{symbol}", cfg.symbol))) ?: ""
        // Use helper for MC 1.21+ compatibility (InventoryView is now an interface)
        val viewTitle = getViewTitle(e.view)
        if (viewTitle != expectedTitle) return
        e.isCancelled = true
        val clicked = e.currentItem ?: return
        if (clicked.type == Material.AIR) return
        val si = slotToItem[e.slot] ?: return
        if (si.permission != null && !p.hasPermission(si.permission)) {
            p.sendMessage("${ChatColor.RED}You don't have permission to buy this item.")
            return
        }
        val price = scale(si.price)
        if (!hasBal(p.uniqueId, price)) { p.sendMessage("${ChatColor.RED}Not enough ${cfg.symbol}"); return }
        addBal(p.uniqueId, price.negate())
        val placeholders = mapOf(
            "{player}" to p.name,
            "{uuid}" to p.uniqueId.toString(),
            "{symbol}" to cfg.symbol,
            "{name}" to cfg.name,
            "{price}" to format(price)
        )
        for (raw in si.commands) {
            val cmd = placeholders.entries.fold(raw) { acc, (k, v) -> acc.replace(k, v) }
            when (si.runAs.uppercase(Locale.ROOT)) {
                "PLAYER" -> p.performCommand(cmd)
                else -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd)
            }
        }
        p.sendMessage("${ChatColor.GREEN}Purchased ${ChatColor.GOLD}${ChatColor.stripColor(clicked.itemMeta?.displayName ?: "item")} ${ChatColor.GREEN}for ${format(price)} ${cfg.symbol}")
        log("SHOP ${p.name} itemSlot=${si.slot} price=${price}")
    }

    // Balances
    private fun getBal(id: UUID): BigDecimal = balances.getOrDefault(id, BigDecimal.ZERO).setScale(cfg.decimals, RoundingMode.HALF_UP)
    private fun hasBal(id: UUID, needed: BigDecimal): Boolean = getBal(id) >= scale(needed)
    private fun addBal(id: UUID, delta: BigDecimal) {
        val cur = getBal(id)
        balances[id] = scale(cur.add(delta))
        saveBalances()
    }

    // Limits / cooldown
    private fun checkCooldown(p: Player): Boolean {
        val last = lastTrade[p.uniqueId] ?: return true
        val now = System.currentTimeMillis()
        val ok = now - last >= cfg.tradeCooldownSeconds * 1000L
        if (!ok) p.sendMessage("${ChatColor.RED}Please wait ${((cfg.tradeCooldownSeconds * 1000L - (now - last)) / 1000)}s")
        return ok
    }
    private fun setCooldown(p: Player) { lastTrade[p.uniqueId] = System.currentTimeMillis() }
    private fun rollDay() {
        val today = LocalDate.now()
        if (today != dailyCountersDate) {
            dailyCountersDate = today
            dailyBuy.clear(); dailySell.clear()
        }
    }
    private fun checkDaily(id: UUID, amt: BigDecimal, isBuy: Boolean): Boolean {
        rollDay()
        val map = if (isBuy) dailyBuy else dailySell
        val cur = map.getOrDefault(id, BigDecimal.ZERO)
        val cap = if (isBuy) cfg.dailyBuyCap else cfg.dailySellCap
        return cur.add(amt) <= cap
    }
    private fun addDaily(id: UUID, amt: BigDecimal, isBuy: Boolean) {
        rollDay()
        val map = if (isBuy) dailyBuy else dailySell
        map[id] = map.getOrDefault(id, BigDecimal.ZERO).add(amt)
    }

    // Formatting
    private fun format(v: BigDecimal): String = v.setScale(cfg.decimals, RoundingMode.HALF_UP).toPlainString()
    private fun getUnitPrice(): BigDecimal = if (cfg.priceMode == "fixed") cfg.priceFixed else currentPrice.setScale(cfg.decimals, RoundingMode.HALF_UP)
    private fun getTradePriceBuy(): BigDecimal = if (cfg.ammEnabled && pool.tokenReserves.compareTo(BigDecimal.ZERO) > 0) pool.moneyReserves.divide(pool.tokenReserves, cfg.decimals, RoundingMode.HALF_UP) else getUnitPrice()
    private fun getTradePriceSell(): BigDecimal = getTradePriceBuy()
    private fun clampPrice(v: BigDecimal): BigDecimal = v.max(cfg.priceMin).min(cfg.priceMax)
    private fun maybeAdjustPrice(amount: BigDecimal, isBuy: Boolean) {
        if (cfg.priceMode != "market") return
        // Normalize trade amount to caps to scale the effect
        val cap = if (isBuy) cfg.dailyBuyCap else cfg.dailySellCap
        val norm = if (cap.compareTo(BigDecimal.ZERO) == 0) BigDecimal.ZERO else amount.divide(cap, 8, RoundingMode.HALF_UP)
        val dir = if (isBuy) BigDecimal.ONE else BigDecimal.ONE.negate()
        val factor = BigDecimal.ONE.add(cfg.priceSensitivity.multiply(dir).multiply(norm))
        currentPrice = clampPrice(currentPrice.multiply(factor))
        // Gentle mean reversion towards base on each trade
        val revert = BigDecimal("0.01")
        val towardsBase = cfg.priceBase.subtract(currentPrice).multiply(revert)
        currentPrice = clampPrice(currentPrice.add(towardsBase))
    }
    private fun colorize(s: String): String = ChatColor.translateAlternateColorCodes('&', s)
    private fun historyFile(): File = File(dir(), "price-history.csv")
    private fun schedulePriceHistoryWriter() {
        // Log price every 60 seconds for graphing; async safe as we only write to disk
        val runnable = Runnable {
            try {
                if (cfg.priceMode == "market") {
                    val f = historyFile(); if (!f.parentFile.exists()) f.parentFile.mkdirs()
                    val ts = System.currentTimeMillis()
                    val line = "$ts,${getUnitPrice()}\n"
                    f.appendText(line)
                }
            } catch (_: Throwable) {}
        }
        // Schedule repeating task; keep id to potentially cancel in the future (e.g., on disable)
        historyTaskId = host.server.scheduler.scheduleSyncRepeatingTask(host, runnable, 20L * 60, 20L * 60)
    }
    private fun scheduleTick() {
        val runnable = Runnable {
            try {
                accrueStaking()
                processOrders()
            } catch (_: Throwable) {}
        }
        tickTaskId = host.server.scheduler.scheduleSyncRepeatingTask(host, runnable, 20L * 30, 20L * 30)
    }

    // IO
    private fun dir(): File = File(host.dataFolder, "addons/settings/${id()}")
    private fun cfgFile(): File = File(dir(), "config.yml")
    private fun balFile(): File = File(dir(), "balances.yml")
    private fun logFile(): File = File(dir(), "audit.log")

    private fun loadConfig() {
        val d = dir(); if (!d.exists()) d.mkdirs()
        val f = cfgFile()
        var created = false
        if (!f.exists()) {
            // Write defaults with comments on first creation
            val defaults = """
                |# Crypto Addon Configuration
                |enabled: true
                |name: "CryptoCoin"
                |symbol: "CC"
                |decimals: 2
                |
                |price:
                |  # fixed => always use price.fixed
                |  # market => dynamic EMA-style price around price.base with sensitivity and clamps
                |  mode: fixed # fixed | market
                |  fixed: 1.00
                |  base: 1.00
                |  min: 0.01
                |  max: 1000000.0
                |  sensitivity: 0.05
                |
                |fees:
                |  buyPercent: 1.0
                |  sellPercent: 1.0
                |  transferPercent: 0.0
                |  sink: burn # burn | treasury
                |
                |supply:
                |  # Choose supply regime: fixed | inflation | halving
                |  model: fixed
                |  cap: 100000000
                |  emissionPerMinute: 0
                |  halvingMinutes: 10080
                |
                |amm:
                |  enabled: true
                |  feePercent: 0.30
                |
                |staking:
                |  enabled: true
                |  ratePctPerDay: 2.0
                |
                |airdrops:
                |  enabled: false
                |
                |limits:
                |  dailyBuyCap: 100000.00
                |  dailySellCap: 100000.00
                |  tradeCooldownSeconds: 10
                |
                |aliases:
                |  - cc
                |  - crypto
                |
                |refundOnFailure: false
            """.trimMargin()
            f.writeText(defaults)
            created = true
        }
        val y = YamlConfiguration.loadConfiguration(f)
        if (!created) {
            // Merge defaults into existing configs without overwriting existing values
            val def = YamlConfiguration()
            def.set("enabled", true)
            def.set("name", "CryptoCoin")
            def.set("symbol", "CC")
            def.set("decimals", 2)
            def.set("price.mode", "fixed")
            def.set("price.fixed", "1.00")
            def.set("price.base", "1.00")
            def.set("price.min", "0.01")
            def.set("price.max", "1000000.0")
            def.set("price.sensitivity", "0.05")
            def.set("fees.buyPercent", "1.0")
            def.set("fees.sellPercent", "1.0")
            def.set("fees.transferPercent", "0.0")
            def.set("fees.sink", "burn")
            def.set("supply.model", "fixed")
            def.set("supply.cap", "100000000")
            def.set("supply.emissionPerMinute", "0")
            def.set("supply.halvingMinutes", 10080)
            def.set("amm.enabled", true)
            def.set("amm.feePercent", "0.30")
            def.set("staking.enabled", true)
            def.set("staking.ratePctPerDay", "2.0")
            def.set("airdrops.enabled", false)
            def.set("limits.dailyBuyCap", "100000.00")
            def.set("limits.dailySellCap", "100000.00")
            def.set("limits.tradeCooldownSeconds", 10)
            def.set("aliases", listOf("cc","crypto"))
            def.set("refundOnFailure", false)
            y.addDefaults(def)
            y.options().copyDefaults(true)
            y.save(f)
        }
        val name = y.getString("name", "") ?: ""
        val symbol = y.getString("symbol", "") ?: ""
        val decimals = y.getInt("decimals", 2)
        val mode = y.getString("price.mode", "fixed") ?: "fixed"
        val fixed = BigDecimal(y.getString("price.fixed", "1.0"))
        val base = BigDecimal(y.getString("price.base", fixed.toPlainString()))
        val minP = BigDecimal(y.getString("price.min", "0.01"))
        val maxP = BigDecimal(y.getString("price.max", "1000000.0"))
        val sens = BigDecimal(y.getString("price.sensitivity", "0.05"))
        val buy = BigDecimal(y.getString("fees.buyPercent", "1.0"))
        val sell = BigDecimal(y.getString("fees.sellPercent", "1.0"))
        val transfer = BigDecimal(y.getString("fees.transferPercent", "0.0"))
        val sink = y.getString("fees.sink", "burn") ?: "burn"
        val supplyModel = y.getString("supply.model", "fixed") ?: "fixed"
        val supplyCap = BigDecimal(y.getString("supply.cap", "100000000"))
        val emission = BigDecimal(y.getString("supply.emissionPerMinute", "0"))
        val halvingMin = y.getInt("supply.halvingMinutes", 10080)
        val ammEnabled = y.getBoolean("amm.enabled", true)
        val ammFee = BigDecimal(y.getString("amm.feePercent", "0.30"))
        val stakingEnabled = y.getBoolean("staking.enabled", true)
        val stakeRate = BigDecimal(y.getString("staking.ratePctPerDay", "2.0"))
        val airdropsEnabled = y.getBoolean("airdrops.enabled", false)
        val buyCap = BigDecimal(y.getString("limits.dailyBuyCap", "100000"))
        val sellCap = BigDecimal(y.getString("limits.dailySellCap", "100000"))
        val cooldown = y.getInt("limits.tradeCooldownSeconds", 10)
        val aliases = y.getStringList("aliases").ifEmpty { listOf("cc","crypto") }
        val refund = y.getBoolean("refundOnFailure", false)
        cfg = Config(
            true,
            name,
            symbol,
            decimals,
            fixed.setScale(decimals, RoundingMode.HALF_UP),
            mode,
            base.setScale(decimals, RoundingMode.HALF_UP),
            minP.setScale(decimals, RoundingMode.HALF_UP),
            maxP.setScale(decimals, RoundingMode.HALF_UP),
            sens,
            buy, sell, transfer, sink,
            supplyModel, supplyCap.setScale(decimals), emission.setScale(decimals), halvingMin,
            ammEnabled, ammFee, stakingEnabled, stakeRate, airdropsEnabled,
            buyCap.setScale(decimals), sellCap.setScale(decimals), cooldown, aliases, refund
        )
        currentPrice = if (cfg.priceMode == "fixed") cfg.priceFixed else cfg.priceBase
    }

    private fun saveConfig() {
        val f = cfgFile()
        val y = YamlConfiguration()
        y.set("enabled", cfg.enabled)
        y.set("name", cfg.name)
        y.set("symbol", cfg.symbol)
        y.set("decimals", cfg.decimals)
        y.set("price.mode", cfg.priceMode)
        y.set("price.fixed", cfg.priceFixed.toPlainString())
        y.set("price.base", cfg.priceBase.toPlainString())
        y.set("price.min", cfg.priceMin.toPlainString())
        y.set("price.max", cfg.priceMax.toPlainString())
        y.set("price.sensitivity", cfg.priceSensitivity.toPlainString())
        y.set("fees.buyPercent", cfg.feeBuyPct.toPlainString())
        y.set("fees.sellPercent", cfg.feeSellPct.toPlainString())
        y.set("fees.transferPercent", cfg.feeTransferPct.toPlainString())
        y.set("fees.sink", cfg.feeSink)
        y.set("supply.model", cfg.supplyModel)
        y.set("supply.cap", cfg.supplyCap.toPlainString())
        y.set("supply.emissionPerMinute", cfg.emissionPerMinute.toPlainString())
        y.set("supply.halvingMinutes", cfg.halvingMinutes)
        y.set("amm.enabled", cfg.ammEnabled)
        y.set("amm.feePercent", cfg.ammFeePct.toPlainString())
        y.set("staking.enabled", cfg.stakingEnabled)
        y.set("staking.ratePctPerDay", cfg.stakeRatePctPerDay.toPlainString())
        y.set("airdrops.enabled", cfg.airdropsEnabled)
        y.set("limits.dailyBuyCap", cfg.dailyBuyCap.toPlainString())
        y.set("limits.dailySellCap", cfg.dailySellCap.toPlainString())
        y.set("limits.tradeCooldownSeconds", cfg.tradeCooldownSeconds)
        y.set("aliases", cfg.aliases)
        y.set("refundOnFailure", cfg.refundOnFailure)
        y.save(f)
    }

    // AMM persistence and operations
    private fun poolFile(): File = File(dir(), "pool.yml")
    private fun loadPool() {
        val f = poolFile(); if (!f.exists()) return
        val y = YamlConfiguration.loadConfiguration(f)
        pool.tokenReserves = BigDecimal(y.getString("token", "0") ?: "0").setScale(cfg.decimals, RoundingMode.HALF_UP)
        pool.moneyReserves = BigDecimal(y.getString("money", "0") ?: "0").setScale(cfg.decimals, RoundingMode.HALF_UP)
        pool.lpSharesTotal = BigDecimal(y.getString("lpTotal", "0") ?: "0").setScale(cfg.decimals, RoundingMode.HALF_UP)
        val sec = y.getConfigurationSection("lp")
        lpShares.clear()
        if (sec != null) for (k in sec.getKeys(false)) {
            lpShares[UUID.fromString(k)] = BigDecimal(y.getString("lp.$k", "0") ?: "0").setScale(cfg.decimals, RoundingMode.HALF_UP)
        }
    }
    private fun savePool() {
        val f = poolFile(); if (!f.parentFile.exists()) f.parentFile.mkdirs()
        val y = YamlConfiguration()
        y.set("token", pool.tokenReserves.toPlainString())
        y.set("money", pool.moneyReserves.toPlainString())
        y.set("lpTotal", pool.lpSharesTotal.toPlainString())
        for ((k,v) in lpShares) y.set("lp.${k}", v.toPlainString())
        y.save(f)
    }
    private fun addLiquidity(owner: UUID, tokenAmt: BigDecimal, moneyAmt: BigDecimal) {
        val shares = if (pool.lpSharesTotal == BigDecimal.ZERO || pool.tokenReserves == BigDecimal.ZERO || pool.moneyReserves == BigDecimal.ZERO) {
            sqrt(tokenAmt.multiply(moneyAmt))
        } else {
            val s1 = tokenAmt.multiply(pool.lpSharesTotal).divide(pool.tokenReserves, cfg.decimals, RoundingMode.HALF_UP)
            val s2 = moneyAmt.multiply(pool.lpSharesTotal).divide(pool.moneyReserves, cfg.decimals, RoundingMode.HALF_UP)
            s1.min(s2)
        }
        pool.tokenReserves = scale(pool.tokenReserves.add(tokenAmt))
        pool.moneyReserves = scale(pool.moneyReserves.add(moneyAmt))
        pool.lpSharesTotal = scale(pool.lpSharesTotal.add(shares))
        lpShares[owner] = scale(lpShares.getOrDefault(owner, BigDecimal.ZERO).add(shares))
        savePool()
    }
    private fun removeLiquidity(owner: UUID, shares: BigDecimal): Pair<BigDecimal, BigDecimal> {
        val owned = lpShares.getOrDefault(owner, BigDecimal.ZERO)
        if (owned <= BigDecimal.ZERO) return BigDecimal.ZERO to BigDecimal.ZERO
        val take = shares.min(owned)
        val tokOut = pool.tokenReserves.multiply(take).divide(pool.lpSharesTotal, cfg.decimals, RoundingMode.HALF_UP)
        val moneyOut = pool.moneyReserves.multiply(take).divide(pool.lpSharesTotal, cfg.decimals, RoundingMode.HALF_UP)
        pool.tokenReserves = scale(pool.tokenReserves.subtract(tokOut))
        pool.moneyReserves = scale(pool.moneyReserves.subtract(moneyOut))
        pool.lpSharesTotal = scale(pool.lpSharesTotal.subtract(take))
        lpShares[owner] = scale(owned.subtract(take))
        savePool()
        return tokOut to moneyOut
    }
    private fun sqrt(x: BigDecimal): BigDecimal = BigDecimal(Math.sqrt(x.toDouble())).setScale(cfg.decimals, RoundingMode.HALF_UP)

    private fun loadShop() {
        val d = dir(); if (!d.exists()) d.mkdirs()
        val f = File(d, "shop.yml")
        if (!f.exists()) {
            val defaults = """
                |# Crypto Addon Shop Configuration
                |title: "&3{name} Shop"
                |size: 9 # one of 9,18,27,36,45,54
                |items:
                |  - slot: 4
                |    material: NETHER_STAR
                |    name: "&6VIP Rank"
                |    lore:
                |      - "&7Costs &b{price} {symbol}"
                |      - "&aClick to buy"
                |    price: "5000"
                |    run-as: CONSOLE # or PLAYER
                |    commands:
                |      - "lp user {player} parent add vip"
            """.trimMargin()
            f.writeText(defaults)
        }
        val yml = YamlConfiguration.loadConfiguration(f)
        val title = yml.getString("title", "${cfg.name} Shop") ?: "${cfg.name} Shop"
        val rawSize = yml.getInt("size", 9)
        val size = listOf(9, 18, 27, 36, 45, 54).firstOrNull { it == rawSize } ?: 9
        val itemsList = mutableListOf<ShopItem>()
        val maps = yml.getMapList("items")
        for (m in maps) {
            val slot = (m["slot"] as? Number)?.toInt() ?: continue
            val matName = (m["material"] as? String)?.trim() ?: continue
            val mat = Material.matchMaterial(matName) ?: continue
            val name = (m["name"] as? String) ?: mat.name
            val lore = (m["lore"] as? List<*>)?.mapNotNull { it?.toString() } ?: emptyList()
            val priceStr = (m["price"] as? String) ?: (m["price"] as? Number)?.toString() ?: "0"
            val price = priceStr.toBigDecimalOrNull()?.let { scale(it) } ?: BigDecimal.ZERO
            val commands = (m["commands"] as? List<*>)?.mapNotNull { it?.toString() } ?: emptyList()
            val runAs = (m["run-as"] as? String) ?: "CONSOLE"
            val perm = (m["permission"] as? String)?.trim()?.ifBlank { null }
            itemsList.add(ShopItem(slot, mat, name, lore, price, commands, runAs, perm))
        }
        shop = Shop(title, size, itemsList)
    }

    private fun loadBalances() {
        val f = balFile(); if (!f.exists()) return
        balances.clear()
        f.forEachLine { line ->
            val parts = line.split(":", limit = 2)
            if (parts.size == 2) {
                val id = runCatching { UUID.fromString(parts[0].trim()) }.getOrNull() ?: return@forEachLine
                val amt = parts[1].trim().toBigDecimalOrNull() ?: return@forEachLine
                balances[id] = amt
            }
        }
    }
    private fun saveBalances() {
        val f = balFile(); if (!f.parentFile.exists()) f.parentFile.mkdirs()
        val b = StringBuilder()
        for ((k, v) in balances) b.append(k.toString()).append(": ").append(v.toPlainString()).append('\n')
        f.writeText(b.toString())
    }

    private fun log(msg: String) { logFile().appendText("${System.currentTimeMillis()} $msg\n") }

    // Supply persistence and fee sink
    private fun supplyFile(): File = File(dir(), "supply.yml")
    private fun loadSupply() {
        val f = supplyFile(); if (!f.exists()) return
        val y = YamlConfiguration.loadConfiguration(f)
        totalSupply = BigDecimal(y.getString("total", "0") ?: "0").setScale(cfg.decimals, RoundingMode.HALF_UP)
        burnedTotal = BigDecimal(y.getString("burned", "0") ?: "0").setScale(cfg.decimals, RoundingMode.HALF_UP)
        treasuryBalance = BigDecimal(y.getString("treasury", "0") ?: "0").setScale(cfg.decimals, RoundingMode.HALF_UP)
    }
    private fun saveSupply() {
        val f = supplyFile(); if (!f.parentFile.exists()) f.parentFile.mkdirs()
        val y = YamlConfiguration()
        y.set("total", totalSupply.toPlainString())
        y.set("burned", burnedTotal.toPlainString())
        y.set("treasury", treasuryBalance.toPlainString())
        y.save(f)
    }
    private fun creditFeeSink(amount: BigDecimal) {
        if (amount <= BigDecimal.ZERO) return
        if (cfg.feeSink.lowercase() == "burn") {
            burnedTotal = scale(burnedTotal.add(amount))
            totalSupply = scale(totalSupply.subtract(amount).max(BigDecimal.ZERO))
        } else {
            treasuryBalance = scale(treasuryBalance.add(amount))
        }
        saveSupply()
    }

    // Crafting sinks
    @EventHandler
    fun onCraft(e: CraftItemEvent) {
        val p = e.whoClicked as? Player ?: return
        val result = e.recipe?.result ?: return
        val need = craftingCost(result.type) ?: return
        if (!hasBal(p.uniqueId, need)) {
            p.sendMessage("${ChatColor.RED}Crafting ${result.type} requires ${format(need)} ${cfg.symbol}")
            e.isCancelled = true
            return
        }
        addBal(p.uniqueId, need.negate())
        p.sendMessage("${ChatColor.YELLOW}Spent ${format(need)} ${cfg.symbol} for crafting ${result.type}")
    }
    private fun craftingCost(mat: Material): BigDecimal? {
        val f = File(dir(), "crafting-sinks.yml"); if (!f.exists()) return null
        val y = YamlConfiguration.loadConfiguration(f)
        val v = y.getString(mat.name) ?: return null
        return v.toBigDecimalOrNull()?.let { scale(it) }
    }

    // Staking persistence and accrual
    private fun stakesFile(): File = File(dir(), "stakes.yml")
    private fun loadStakes() {
        val f = stakesFile(); if (!f.exists()) return
        val y = YamlConfiguration.loadConfiguration(f)
        stakes.clear(); claimable.clear()
        for (key in y.getKeys(false)) {
            val id = runCatching { UUID.fromString(key) }.getOrNull() ?: continue
            val list = mutableListOf<StakeEntry>()
            val sec = y.getConfigurationSection(key) ?: continue
            for (idx in sec.getKeys(false)) {
                val s = y.getConfigurationSection("$key.$idx") ?: continue
                val amt = BigDecimal(s.getString("amount", "0") ?: "0")
                val start = s.getLong("start")
                val end = s.getLong("end")
                val cl = BigDecimal(s.getString("claimed", "0") ?: "0")
                list.add(StakeEntry(amt, start, end, cl))
            }
            stakes[id] = list
        }
    }
    private fun saveStakes() {
        val f = stakesFile(); if (!f.parentFile.exists()) f.parentFile.mkdirs()
        val y = YamlConfiguration()
        for ((id, list) in stakes) {
            var i = 0
            for (s in list) {
                y.set("$id.$i.amount", s.amount.toPlainString())
                y.set("$id.$i.start", s.startMs)
                y.set("$id.$i.end", s.endMs)
                y.set("$id.$i.claimed", s.claimed.toPlainString())
                i++
            }
        }
        y.save(f)
    }
    private fun accrueStaking() {
        if (!cfg.stakingEnabled) return
        val now = System.currentTimeMillis()
        val perMsRate = cfg.stakeRatePctPerDay.divide(BigDecimal(100), 8, RoundingMode.HALF_UP).divide(BigDecimal(86400000L), 8, RoundingMode.HALF_UP)
        for ((id, list) in stakes) {
            var acc = claimable.getOrDefault(id, BigDecimal.ZERO)
            for (s in list) {
                val end = kotlin.math.min(now, s.endMs)
                val dur = kotlin.math.max(0L, end - s.startMs)
                val reward = s.amount.multiply(perMsRate).multiply(BigDecimal(dur)).setScale(cfg.decimals, RoundingMode.HALF_UP)
                val unclaimed = reward.subtract(s.claimed).max(BigDecimal.ZERO)
                acc = acc.add(unclaimed)
                s.claimed = s.claimed.add(unclaimed)
            }
            claimable[id] = scale(acc)
        }
        saveStakes()
    }

    // Orders
    private fun placeOrder(owner: UUID, side: Side, type: OrderType, amount: BigDecimal, price: BigDecimal?, trigger: BigDecimal?): Long {
        val id = nextOrderId++
        orderBook.add(Order(id, owner, side, type, price, amount, trigger))
        return id
    }
    private fun cancelOrder(owner: UUID, id: Long): Boolean {
        val it = orderBook.iterator()
        while (it.hasNext()) {
            val o = it.next()
            if (o.id == id && o.owner == owner) { it.remove(); return true }
        }
        return false
    }
    private fun processOrders() {
        if (orderBook.isEmpty()) return
        val price = getUnitPrice()
        val it = orderBook.iterator()
        while (it.hasNext()) {
            val o = it.next()
            when (o.type) {
                OrderType.LIMIT -> {
                    val executable = (o.side == Side.BUY && price <= (o.price ?: price)) || (o.side == Side.SELL && price >= (o.price ?: price))
                    if (executable) {
                        val ownerPlayer = Bukkit.getPlayer(o.owner)
                        if (ownerPlayer != null) {
                            if (o.side == Side.BUY) {
                                val econ = eco ?: continue
                                val cost = o.remaining.multiply(price)
                                if (econ.has(ownerPlayer, cost.toDouble())) {
                                    econ.withdrawPlayer(ownerPlayer, cost.toDouble())
                                    addBal(o.owner, o.remaining)
                                    it.remove()
                                }
                            } else {
                                if (hasBal(o.owner, o.remaining)) {
                                    addBal(o.owner, o.remaining.negate())
                                    eco?.depositPlayer(ownerPlayer, o.remaining.multiply(price).toDouble())
                                    it.remove()
                                }
                            }
                        }
                    }
                }
                OrderType.STOP -> {
                    val trig = o.trigger ?: continue
                    val hit = (o.side == Side.BUY && price >= trig) || (o.side == Side.SELL && price <= trig)
                    if (hit) {
                        o.type = OrderType.LIMIT
                        o.price = price
                    }
                }
            }
        }
    }
}
