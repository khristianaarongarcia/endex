package org.lokixcz.theendex.crypto

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.lokixcz.theendex.Endex
import org.lokixcz.theendex.addon.EndexAddon
import org.lokixcz.theendex.addon.AddonSubcommandHandler
import org.lokixcz.theendex.addon.AddonTabCompleter
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
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

    private fun bd(v: Int) = BigDecimal(v)
    private fun scale(v: BigDecimal) = v.setScale(cfg.decimals, RoundingMode.HALF_UP)

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
    // Load shop config
    loadShop()

        // Register listeners and commands
    host.server.pluginManager.registerEvents(this, host)
    schedulePriceHistoryWriter()
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
                    }
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
                    val unitPrice = getUnitPrice()
                    val cost = amount.multiply(unitPrice)
                    val fee = amount.multiply(cfg.feeBuyPct).divide(BigDecimal(100))
                    val net = amount.subtract(fee)
                    val econ = eco
                    if (econ == null) { sender.sendMessage("${ChatColor.RED}Economy not available"); return@AddonSubcommandHandler true }
                    if (!econ.has(sender, cost.toDouble())) { sender.sendMessage("${ChatColor.RED}Not enough money (${cost})"); return@AddonSubcommandHandler true }
                    econ.withdrawPlayer(sender, cost.toDouble())
                    addBal(sender.uniqueId, net)
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
                    val net = amount.subtract(fee)
                    if (!hasBal(sender.uniqueId, amount)) { sender.sendMessage("${ChatColor.RED}Insufficient ${cfg.symbol}"); return@AddonSubcommandHandler true }
                    val unitPrice = getUnitPrice()
                    val payout = amount.multiply(unitPrice)
                    addBal(sender.uniqueId, net.negate()) // burn fee, user loses net
                    val econ = eco
                    if (econ == null) { sender.sendMessage("${ChatColor.RED}Economy not available"); return@AddonSubcommandHandler true }
                    econ.depositPlayer(sender, payout.toDouble())
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
            val f = host.javaClass.getDeclaredField("addonCommandRouter"); f.isAccessible = true
            val router = f.get(host) as? org.lokixcz.theendex.addon.AddonCommandRouter
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

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        val p = e.whoClicked as? Player ?: return
        val s = shop ?: return
        val expectedTitle = colorize(s.title.replace("{name}", cfg.name).replace("{symbol}", cfg.symbol))
        if (e.view.title != expectedTitle) return
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

    // IO
    private fun dir(): File = File(host.dataFolder, "addons/settings/${id()}")
    private fun cfgFile(): File = File(dir(), "config.yml")
    private fun balFile(): File = File(dir(), "balances.yml")
    private fun logFile(): File = File(dir(), "audit.log")

    private fun loadConfig() {
        val d = dir(); if (!d.exists()) d.mkdirs()
        val f = cfgFile()
        if (!f.exists()) {
            // Write defaults
            val defaults = """
                |enabled: true
                |name: "CryptoCoin"
                |symbol: "CC"
                |decimals: 2
                |price:
                |  mode: fixed # fixed | market
                |  fixed: 1.00
                |  base: 1.00
                |  min: 0.01
                |  max: 1000000.0
                |  sensitivity: 0.05
                |fees:
                |  buyPercent: 1.0
                |  sellPercent: 1.0
                |  transferPercent: 0.0
                |  sink: burn
                |limits:
                |  dailyBuyCap: 100000.00
                |  dailySellCap: 100000.00
                |  tradeCooldownSeconds: 10
                |aliases:
                |  - cc
                |  - crypto
                |refundOnFailure: false
            """.trimMargin()
            f.writeText(defaults)
        }
        // Naive parse (MVP) using regex; a full YAML parse would require bringing a parser here.
        val text = f.readText()
            // Correctly escaped regex to capture optional quoted scalar values
            fun find(key: String, def: String): String? =
                Regex("^$key:\\s*\\\"?([^\\\"\\n]+)\\\"?", RegexOption.MULTILINE)
                    .find(text)?.groupValues?.get(1) ?: def
        fun findNum(key: String, def: String) = find(key, def)!!.trim()
        val name = find("name", "")!!.trim()
        val symbol = find("symbol", "")!!.trim()
        val decimals = findNum("decimals", "2").toIntOrNull() ?: 2
        val fixed = Regex("fixed:\\s*([0-9.]+)").find(text)?.groupValues?.get(1)?.toBigDecimalOrNull() ?: BigDecimal.ONE
        val mode = Regex("mode:\\s*(fixed|market)").find(text)?.groupValues?.get(1) ?: "fixed"
        val base = Regex("base:\\s*([0-9.]+)").find(text)?.groupValues?.get(1)?.toBigDecimalOrNull() ?: fixed
        val minP = Regex("min:\\s*([0-9.]+)").find(text)?.groupValues?.get(1)?.toBigDecimalOrNull() ?: BigDecimal("0.01")
        val maxP = Regex("max:\\s*([0-9.]+)").find(text)?.groupValues?.get(1)?.toBigDecimalOrNull() ?: BigDecimal("1000000")
        val sens = Regex("sensitivity:\\s*([0-9.]+)").find(text)?.groupValues?.get(1)?.toBigDecimalOrNull() ?: BigDecimal("0.05")
        val buy = Regex("buyPercent:\\s*([0-9.]+)").find(text)?.groupValues?.get(1)?.toBigDecimalOrNull() ?: bd(1)
        val sell = Regex("sellPercent:\\s*([0-9.]+)").find(text)?.groupValues?.get(1)?.toBigDecimalOrNull() ?: bd(1)
        val transfer = Regex("transferPercent:\\s*([0-9.]+)").find(text)?.groupValues?.get(1)?.toBigDecimalOrNull() ?: bd(0)
        val buyCap = Regex("dailyBuyCap:\\s*([0-9.]+)").find(text)?.groupValues?.get(1)?.toBigDecimalOrNull() ?: bd(100000)
        val sellCap = Regex("dailySellCap:\\s*([0-9.]+)").find(text)?.groupValues?.get(1)?.toBigDecimalOrNull() ?: bd(100000)
        val cooldown = Regex("tradeCooldownSeconds:\\s*([0-9]+)").find(text)?.groupValues?.get(1)?.toIntOrNull() ?: 10
        val aliases = Regex("aliases:\n((?:\\s*-\\s*[^\n]+\n?)+)").find(text)?.groupValues?.get(1)?.lines()?.mapNotNull { it.trim().removePrefix("-").trim().ifBlank { null } } ?: listOf("cc","crypto")
        val refund = Regex("refundOnFailure:\\s*(true|false)").find(text)?.groupValues?.get(1)?.toBoolean() ?: false
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
            buy, sell, transfer,
            buyCap.setScale(decimals), sellCap.setScale(decimals),
            cooldown,
            aliases,
            refund
        )
        currentPrice = if (cfg.priceMode == "fixed") cfg.priceFixed else cfg.priceBase
    }

    private fun saveConfig() {
        val f = cfgFile()
        // Precompute alias lines to avoid nested lambda inside string template
        val aliasLines = cfg.aliases.joinToString("\n") { "|  - $it" }
        val out = """
            |enabled: ${cfg.enabled}
            |name: "${cfg.name}"
            |symbol: "${cfg.symbol}"
            |decimals: ${cfg.decimals}
            |price:
            |  mode: ${cfg.priceMode}
            |  fixed: ${cfg.priceFixed}
            |  base: ${cfg.priceBase}
            |  min: ${cfg.priceMin}
            |  max: ${cfg.priceMax}
            |  sensitivity: ${cfg.priceSensitivity}
            |fees:
            |  buyPercent: ${cfg.feeBuyPct}
            |  sellPercent: ${cfg.feeSellPct}
            |  transferPercent: ${cfg.feeTransferPct}
            |  sink: burn
            |limits:
            |  dailyBuyCap: ${cfg.dailyBuyCap}
            |  dailySellCap: ${cfg.dailySellCap}
            |  tradeCooldownSeconds: ${cfg.tradeCooldownSeconds}
            |aliases:
            ${aliasLines}
            |refundOnFailure: ${cfg.refundOnFailure}
        """.trimMargin()
        f.writeText(out)
    }

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
}
