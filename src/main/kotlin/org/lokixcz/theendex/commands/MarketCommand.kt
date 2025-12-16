package org.lokixcz.theendex.commands

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.lokixcz.theendex.Endex
import org.lokixcz.theendex.invest.InvestmentsManager
import kotlin.math.max
import org.lokixcz.theendex.gui.MarketGUI
import org.lokixcz.theendex.api.events.PreBuyEvent
import org.lokixcz.theendex.api.events.PreSellEvent

class MarketCommand(private val plugin: Endex) : CommandExecutor {
    private val investments by lazy { InvestmentsManager(plugin) }
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            if (sender is Player) {
                plugin.marketGUI.open(sender)
            } else {
                sender.sendMessage("${ChatColor.GOLD}[TheEndex] ${ChatColor.YELLOW}Use: /market help for a list of commands")
            }
            return true
        }
        return when (args[0].lowercase()) {
            "help", "?" -> handleHelp(sender)
            "price" -> handlePrice(sender, args)
            "buy" -> handleBuy(sender, args)
            "sell" -> handleSell(sender, args)
            "top" -> handleTop(sender)
            "invest" -> handleInvest(sender, args)
            "event" -> handleEvent(sender, args)
            "delivery", "deliveries" -> handleDelivery(sender, args)
            "withdraw" -> handleWithdraw(sender, args)
            "holdings" -> handleHoldings(sender, args)
            else -> {
                sender.sendMessage("${ChatColor.RED}Unknown subcommand. Use ${ChatColor.YELLOW}/market help ${ChatColor.RED}for a list of commands.")
                true
            }
        }
    }

    private fun handleHelp(sender: CommandSender): Boolean {
        sender.sendMessage("${ChatColor.GOLD}═══════════════════════════════════════")
        sender.sendMessage("${ChatColor.LIGHT_PURPLE}${ChatColor.BOLD}The Endex ${ChatColor.GRAY}- Market Commands")
        sender.sendMessage("${ChatColor.GOLD}═══════════════════════════════════════")
        sender.sendMessage("")
        sender.sendMessage("${ChatColor.YELLOW}/market ${ChatColor.GRAY}- Open market GUI")
        sender.sendMessage("${ChatColor.YELLOW}/market price <item> ${ChatColor.GRAY}- Check item price")
        sender.sendMessage("${ChatColor.YELLOW}/market buy <item> <amount> ${ChatColor.GRAY}- Buy items")
        sender.sendMessage("${ChatColor.YELLOW}/market sell <item> <amount> ${ChatColor.GRAY}- Sell items")
        sender.sendMessage("${ChatColor.YELLOW}/market top ${ChatColor.GRAY}- View top gainers/losers")
        sender.sendMessage("")
        sender.sendMessage("${ChatColor.AQUA}Holdings & Delivery:")
        sender.sendMessage("${ChatColor.YELLOW}/market holdings ${ChatColor.GRAY}- View your virtual holdings")
        sender.sendMessage("${ChatColor.YELLOW}/market withdraw <item> [amount] ${ChatColor.GRAY}- Withdraw from holdings")
        sender.sendMessage("${ChatColor.YELLOW}/market withdraw all ${ChatColor.GRAY}- Withdraw all holdings")
        sender.sendMessage("${ChatColor.YELLOW}/market delivery ${ChatColor.GRAY}- View pending deliveries")
        sender.sendMessage("${ChatColor.YELLOW}/market delivery claim <item> ${ChatColor.GRAY}- Claim specific delivery")
        sender.sendMessage("${ChatColor.YELLOW}/market delivery claim-all ${ChatColor.GRAY}- Claim all deliveries")
        sender.sendMessage("")
        sender.sendMessage("${ChatColor.AQUA}Investments:")
        sender.sendMessage("${ChatColor.YELLOW}/market invest buy <item> <amount> ${ChatColor.GRAY}- Buy investment")
        sender.sendMessage("${ChatColor.YELLOW}/market invest list ${ChatColor.GRAY}- View investments")
        sender.sendMessage("${ChatColor.YELLOW}/market invest redeem-all ${ChatColor.GRAY}- Redeem all investments")
        
        if (sender.hasPermission("theendex.admin")) {
            sender.sendMessage("")
            sender.sendMessage("${ChatColor.RED}Admin:")
            sender.sendMessage("${ChatColor.YELLOW}/market event ${ChatColor.GRAY}- List/trigger events")
            sender.sendMessage("${ChatColor.YELLOW}/market event <name> ${ChatColor.GRAY}- Trigger an event")
            sender.sendMessage("${ChatColor.YELLOW}/market event end <name> ${ChatColor.GRAY}- End an event")
            sender.sendMessage("${ChatColor.YELLOW}/market event clear ${ChatColor.GRAY}- Clear all events")
        }
        
        sender.sendMessage("${ChatColor.GOLD}═══════════════════════════════════════")
        return true
    }

    private fun handlePrice(sender: CommandSender, args: Array<out String>): Boolean {
        if (args.size < 2) {
            sender.sendMessage("${ChatColor.RED}Usage: /market price <material>")
            return true
        }
        val mat = Material.matchMaterial(args[1].uppercase())
        if (mat == null) {
            sender.sendMessage("${ChatColor.RED}Unknown material: ${args[1]}")
            return true
        }
        val item = plugin.marketManager.get(mat)
        if (item == null) {
            sender.sendMessage("${ChatColor.RED}${mat} is not tracked by the market.")
            return true
        }
    val current = item.currentPrice
    val prev = item.history.toList().dropLast(1).lastOrNull()?.price ?: current
        val diff = current - prev
        val arrow = when {
            diff > 0.0001 -> "${ChatColor.GREEN}↑"
            diff < -0.0001 -> "${ChatColor.RED}↓"
            else -> "${ChatColor.YELLOW}→"
        }
    val mul = plugin.eventManager.multiplierFor(mat)
    sender.sendMessage("${ChatColor.GOLD}[The Endex] ${ChatColor.AQUA}${prettyName(mat)}${ChatColor.GRAY}: ${ChatColor.GREEN}${format(current)} ${ChatColor.GRAY}(${arrow}${ChatColor.GRAY} ${format(diff)})")
    if (mul != 1.0) sender.sendMessage("${ChatColor.DARK_AQUA}Event multiplier: ${ChatColor.AQUA}x${format(mul)}  ${ChatColor.GRAY}Effective: ${ChatColor.GREEN}${format(current*mul)}")
        sender.sendMessage("${ChatColor.DARK_GRAY}Base ${format(item.basePrice)}  Min ${format(item.minPrice)}  Max ${format(item.maxPrice)}")
        // history line (last up to 5)
        if (item.history.isNotEmpty()) {
            val last = item.history.takeLast(12)
            val lastNums = last.map { it.price }
            val min = lastNums.minOrNull() ?: 0.0
            val max = lastNums.maxOrNull() ?: 0.0
            val span = (max - min).takeIf { it > 1e-9 } ?: 1.0
            val bars = lastNums.map {
                val pct = (it - min) / span
                when {
                    pct >= 0.85 -> "█"
                    pct >= 0.70 -> "▇"
                    pct >= 0.55 -> "▆"
                    pct >= 0.40 -> "▅"
                    pct >= 0.25 -> "▃"
                    pct >= 0.10 -> "▂"
                    else -> "▁"
                }
            }.joinToString("")
            val last5 = item.history.takeLast(5).map { format(it.price) }
            sender.sendMessage("${ChatColor.GRAY}History: ${last5.joinToString(separator = " ${ChatColor.DARK_GRAY}| ${ChatColor.GRAY}")}")
            sender.sendMessage("${ChatColor.DARK_GRAY}Chart: ${ChatColor.GRAY}${bars}")
        }
        return true
    }

    private fun handleBuy(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}This command can only be used by players.")
            return true
        }
        if (!sender.hasPermission("theendex.buy")) {
            sender.sendMessage("${ChatColor.RED}You don't have permission to buy.")
            return true
        }
        if (plugin.economy == null) {
            sender.sendMessage("${ChatColor.RED}Economy unavailable. Install Vault and an economy plugin.")
            return true
        }
        if (args.size < 3) {
            sender.sendMessage("${ChatColor.RED}Usage: /market buy <material> <amount>")
            return true
        }
        val mat = Material.matchMaterial(args[1].uppercase())
        if (mat == null) { sender.sendMessage("${ChatColor.RED}Invalid material or amount."); return true }
        var amount: Int = args[2].toIntOrNull()?.takeIf { it > 0 }
            ?: run { sender.sendMessage("${ChatColor.RED}Invalid material or amount."); return true }
        val item = plugin.marketManager.get(mat)
        if (item == null) {
            sender.sendMessage("${ChatColor.RED}${mat} is not tracked by the market.")
            return true
        }

        // Check if virtual holdings system is enabled
        val holdingsEnabled = plugin.config.getBoolean("holdings.enabled", true)
        val db = plugin.marketManager.sqliteStore()

        val taxPct = plugin.config.getDouble("transaction-tax-percent", 0.0).coerceAtLeast(0.0)
        var unit = item.currentPrice * (plugin.eventManager.multiplierFor(mat))
        
        // Fire pre-buy event (modifiable)
        val pre = PreBuyEvent(mat, amount, unit)
        org.bukkit.Bukkit.getPluginManager().callEvent(pre)
        if (pre.isCancelled) { sender.sendMessage("${ChatColor.RED}Purchase cancelled by server policy."); return true }
        amount = pre.amount
        unit = pre.unitPrice
        val subtotal = unit * amount
        val tax = subtotal * (taxPct / 100.0)
        val total = subtotal + tax

        val eco = plugin.economy!!
        val bal = eco.getBalance(sender)
        if (bal + 1e-9 < total) {
            sender.sendMessage("${ChatColor.RED}You need ${format(total)} but only have ${format(bal)}.")
            return true
        }
        
        // Virtual Holdings Mode: Items go to holdings, not inventory
        if (holdingsEnabled && db != null) {
            // Check holdings limit before purchase
            val success = db.addToHoldings(sender.uniqueId.toString(), mat, amount, unit)
            if (!success) {
                val maxHoldings = plugin.config.getInt("holdings.max-total-per-player", 100000)
                sender.sendMessage("${ChatColor.RED}Holdings limit reached ($maxHoldings items max). Withdraw some items first.")
                sender.sendMessage("${ChatColor.GRAY}Use ${ChatColor.YELLOW}/market withdraw ${ChatColor.GRAY}to claim items from holdings.")
                return true
            }
            
            // Payment successful, deduct money
            val withdraw = eco.withdrawPlayer(sender, total)
            if (!withdraw.transactionSuccess()) {
                // Rollback holdings if payment fails
                db.removeFromHoldings(sender.uniqueId.toString(), mat, amount)
                sender.sendMessage("${ChatColor.RED}Payment failed: ${withdraw.errorMessage}")
                return true
            }
            
            // Record trade
            db.insertTrade(sender.uniqueId.toString(), mat, "BUY", amount, unit, total)
            
            plugin.marketManager.addDemand(mat, amount.toDouble())
            
            sender.sendMessage("${ChatColor.GOLD}[TheEndex] ${ChatColor.GREEN}Bought $amount ${prettyName(mat)} for ${format(total)}!")
            sender.sendMessage("${ChatColor.YELLOW}  → Items added to your ${ChatColor.LIGHT_PURPLE}Holdings${ChatColor.YELLOW}.")
            sender.sendMessage("${ChatColor.GRAY}Use ${ChatColor.YELLOW}/market withdraw ${mat.name} ${ChatColor.GRAY}to claim items to inventory.")
            if (tax > 0) {
                sender.sendMessage("${ChatColor.DARK_GRAY}(Tax: ${format(tax)} at ${taxPct}%)")
            }
            return true
        }
        
        // Legacy Mode: Direct to inventory (fallback if holdings disabled)
        val maxCapacity = calculateInventoryCapacity(sender, mat)
        val originalAmount = amount
        val deliveryEnabled = plugin.getDeliveryManager() != null && plugin.config.getBoolean("delivery.enabled", true)
        
        if (amount > maxCapacity) {
            if (!deliveryEnabled) {
                // Old behavior: cap the purchase
                amount = maxCapacity
                if (amount <= 0) {
                    sender.sendMessage("${ChatColor.RED}Your inventory is full. Please make space and try again.")
                    return true
                }
                sender.sendMessage("${ChatColor.YELLOW}[TheEndex] ${ChatColor.GOLD}Purchase capped to $amount ${mat.name} due to inventory space (requested: $originalAmount).")
                sender.sendMessage("${ChatColor.GRAY}Tip: Empty your inventory or use Ender Chest for larger orders.")
            }
            // Else: delivery enabled, we'll charge for full amount and overflow goes to pending
        }

        val withdraw = eco.withdrawPlayer(sender, total)
        if (!withdraw.transactionSuccess()) {
            sender.sendMessage("${ChatColor.RED}Payment failed: ${withdraw.errorMessage}")
            return true
        }
        // Give items
        fun countMaterial(): Int = sender.inventory.contents
            .filterNotNull()
            .filter { it.type == mat }
            .sumOf { it.amount }

        var remaining = amount
        var delivered = 0
        var pendingDelivery = 0
        var safety = 0
        
        while (remaining > 0) {
            safety += 1
            if (safety > 256) {
                plugin.logger.warning("Market buy safety break triggered for ${sender.name} purchasing ${mat.name}; remaining=$remaining")
                break
            }

            val beforeCount = countMaterial()
            val toGive = max(1, minOf(remaining, mat.maxStackSize))
            val give = ItemStack(mat, toGive)
            val leftovers = sender.inventory.addItem(give)
            val leftoverCount = leftovers.values.sumOf { it.amount }
            val afterGiveCount = countMaterial()
            val accepted = (afterGiveCount - beforeCount).coerceAtLeast(0)
            
            if (accepted > 0) {
                delivered += accepted
                remaining -= accepted
            }

            if (accepted == 0 && leftoverCount == 0) {
                if (deliveryEnabled) {
                    val success = plugin.getDeliveryManager()?.addPending(sender.uniqueId, mat, remaining) ?: false
                    if (success) {
                        pendingDelivery += remaining
                    } else {
                        sender.sendMessage("${ChatColor.YELLOW}[TheEndex] ${ChatColor.GOLD}$remaining ${mat.name} dropped (delivery limit reached / zero-accept).")
                        repeat(remaining) { sender.world.dropItemNaturally(sender.location, ItemStack(mat, 1)) }
                    }
                } else {
                    repeat(remaining) { sender.world.dropItemNaturally(sender.location, ItemStack(mat, 1)) }
                    sender.sendMessage("${ChatColor.YELLOW}[TheEndex] ${ChatColor.GOLD}$remaining ${mat.name} dropped (zero progress).")
                }
                remaining = 0
                break
            }

            if (leftoverCount > 0) {
                if (deliveryEnabled) {
                    val success = plugin.getDeliveryManager()?.addPending(sender.uniqueId, mat, remaining) ?: false
                    if (success) {
                        pendingDelivery += remaining
                    } else {
                        leftovers.values.forEach { sender.world.dropItemNaturally(sender.location, it) }
                        sender.sendMessage("${ChatColor.YELLOW}[TheEndex] ${ChatColor.GOLD}$leftoverCount ${mat.name} dropped (delivery limit reached).")
                    }
                } else {
                    leftovers.values.forEach { sender.world.dropItemNaturally(sender.location, it) }
                    sender.sendMessage("${ChatColor.YELLOW}[TheEndex] ${ChatColor.GOLD}$leftoverCount ${mat.name} dropped (inventory full).")
                }
                remaining = 0
                break
            }
        }

        plugin.marketManager.addDemand(mat, amount.toDouble())
        
        // Build success message
        if (pendingDelivery > 0) {
            sender.sendMessage("${ChatColor.GOLD}[TheEndex] ${ChatColor.GREEN}Bought $amount ${mat.name} for ${format(total)}.")
            sender.sendMessage("${ChatColor.YELLOW}  Delivered: $delivered | Pending: $pendingDelivery ${ChatColor.GRAY}(click Ender Chest in market GUI)")
        } else if (delivered == amount) {
            sender.sendMessage("${ChatColor.GOLD}[TheEndex] ${ChatColor.GREEN}Bought $amount ${mat.name} for ${format(total)} (tax ${format(tax)} at ${taxPct}%).")
        } else {
            // Some items dropped (delivery disabled or limit reached)
            sender.sendMessage("${ChatColor.GOLD}[TheEndex] ${ChatColor.GREEN}Bought $amount ${mat.name} for ${format(total)}.")
            sender.sendMessage("${ChatColor.YELLOW}  Delivered: $delivered | Dropped: ${amount - delivered}")
        }
        
        return true
    }

    private fun handleSell(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}This command can only be used by players.")
            return true
        }
        if (!sender.hasPermission("theendex.sell")) {
            sender.sendMessage("${ChatColor.RED}You don't have permission to sell.")
            return true
        }
        if (plugin.economy == null) {
            sender.sendMessage("${ChatColor.RED}Economy unavailable. Install Vault and an economy plugin.")
            return true
        }
        if (args.size < 3) {
            sender.sendMessage("${ChatColor.RED}Usage: /market sell <material> <amount>")
            return true
        }
        val mat = Material.matchMaterial(args[1].uppercase())
        if (mat == null) { sender.sendMessage("${ChatColor.RED}Invalid material or amount."); return true }
        var amount: Int = args[2].toIntOrNull()?.takeIf { it > 0 }
            ?: run { sender.sendMessage("${ChatColor.RED}Invalid material or amount."); return true }
        val item = plugin.marketManager.get(mat)
        if (item == null) {
            sender.sendMessage("${ChatColor.RED}${mat} is not tracked by the market.")
            return true
        }

        val taxPct = plugin.config.getDouble("transaction-tax-percent", 0.0).coerceAtLeast(0.0)
        var unit = item.currentPrice * (plugin.eventManager.multiplierFor(mat))
        // Fire pre-sell event (modifiable)
        val pre = PreSellEvent(mat, amount, unit)
        org.bukkit.Bukkit.getPluginManager().callEvent(pre)
        if (pre.isCancelled) { sender.sendMessage("${ChatColor.RED}Sale cancelled by server policy."); return true }
        amount = pre.amount
        unit = pre.unitPrice
        // Now remove items based on possibly adjusted amount
        val removed = removeItems(sender, mat, amount)
        if (removed < amount) {
            sender.sendMessage("${ChatColor.RED}You only have $removed of $amount required ${mat}.")
            return true
        }
        val subtotal = unit * amount
        val tax = subtotal * (taxPct / 100.0)
        val payout = subtotal - tax

        val eco = plugin.economy!!
        val deposit = eco.depositPlayer(sender, payout)
        if (!deposit.transactionSuccess()) {
            sender.sendMessage("${ChatColor.RED}Payment failed: ${deposit.errorMessage}")
            return true
        }

        plugin.marketManager.addSupply(mat, amount.toDouble())
        sender.sendMessage("${ChatColor.GOLD}[TheEndex] ${ChatColor.GREEN}Sold $amount ${mat} for ${format(payout)} (tax ${format(tax)} at ${taxPct}%).")
        return true
    }

    private fun handleTop(sender: CommandSender): Boolean {
        val entries = plugin.marketManager.allItems()
        if (entries.isEmpty()) {
            sender.sendMessage("${ChatColor.YELLOW}No market items available.")
            return true
        }
        val changes = entries.map { it.material to pctChange(it.history) }
        val topGainers = changes.sortedByDescending { it.second }.take(5)
        val topLosers = changes.sortedBy { it.second }.take(5)

        sender.sendMessage("${ChatColor.GOLD}[The Endex] ${ChatColor.GREEN}Top Gainers:")
        topGainers.forEach {
            sender.sendMessage("${ChatColor.GREEN}+${formatPct(it.second)} ${ChatColor.AQUA}${prettyName(it.first)}")
        }
        sender.sendMessage("${ChatColor.GOLD}[The Endex] ${ChatColor.RED}Top Losers:")
        topLosers.forEach {
            sender.sendMessage("${ChatColor.RED}${formatPct(it.second)} ${ChatColor.AQUA}${prettyName(it.first)}")
        }
        return true
    }

    private fun pctChange(history: Collection<org.lokixcz.theendex.market.PricePoint>): Double {
        if (history.size < 2) return 0.0
        val list = if (history is List<org.lokixcz.theendex.market.PricePoint>) history else history.toList()
        val last = list.last().price
        val prev = list[list.lastIndex - 1].price
        if (prev == 0.0) return 0.0
        return (last - prev) / prev * 100.0
    }

    private fun removeItems(player: Player, material: Material, amount: Int): Int {
        var toRemove = amount
        val inv = player.inventory
        for (slot in 0 until inv.size) {
            val stack = inv.getItem(slot) ?: continue
            if (stack.type != material) continue
            val take = minOf(toRemove, stack.amount)
            stack.amount -= take
            if (stack.amount <= 0) inv.setItem(slot, null)
            toRemove -= take
            if (toRemove <= 0) break
        }
        return amount - toRemove
    }

    /**
     * Calculate how many items of the given material the player can receive in their inventory.
     * Accounts for existing partial stacks and empty slots.
     */
    private fun calculateInventoryCapacity(player: Player, material: Material): Int {
        val inv = player.inventory
        val maxStack = material.maxStackSize
        var capacity = 0
        
        // Count space in existing stacks of this material
        for (slot in 0 until inv.size) {
            val stack = inv.getItem(slot) ?: continue
            if (stack.type == material) {
                capacity += (maxStack - stack.amount).coerceAtLeast(0)
            }
        }
        
        // Count empty slots (each can hold maxStack items)
        val emptySlots = inv.storageContents.count { it == null || it.type == Material.AIR }
        capacity += emptySlots * maxStack
        
        return capacity
    }

    private fun format(n: Double): String = String.format("%.2f", n)
    private fun formatPct(n: Double): String = String.format("%.2f%%", n)
    private fun prettyName(mat: Material): String = mat.name.lowercase().split('_').joinToString(" ") { it.replaceFirstChar { c -> c.titlecase() } }

    private fun handleInvest(sender: CommandSender, args: Array<out String>): Boolean {
        if (!plugin.config.getBoolean("investments.enabled", true)) {
            sender.sendMessage("${ChatColor.RED}Investments are disabled in config."); return true
        }
        if (sender !is Player) { sender.sendMessage("${ChatColor.RED}Players only."); return true }
        if (!sender.hasPermission("theendex.invest")) { sender.sendMessage("${ChatColor.RED}No permission."); return true }
        if (args.size == 1) {
            sender.sendMessage("${ChatColor.GOLD}[The Endex] ${ChatColor.AQUA}/market invest buy <material> <amount> ${ChatColor.GRAY}| ${ChatColor.AQUA}/market invest list ${ChatColor.GRAY}| ${ChatColor.AQUA}/market invest redeem-all")
            return true
        }
        when (args[1].lowercase()) {
            "buy" -> {
                if (plugin.economy == null) { sender.sendMessage("${ChatColor.RED}Economy unavailable."); return true }
                if (args.size < 4) { sender.sendMessage("${ChatColor.RED}Usage: /market invest buy <material> <amount>"); return true }
                val mat = Material.matchMaterial(args[2].uppercase()) ?: run { sender.sendMessage("${ChatColor.RED}Unknown material"); return true }
                val amt = args[3].toDoubleOrNull()?.takeIf { it > 0 } ?: run { sender.sendMessage("${ChatColor.RED}Invalid amount"); return true }
                val item = plugin.marketManager.get(mat) ?: run { sender.sendMessage("${ChatColor.RED}${mat} is not tracked"); return true }
                val unit = item.currentPrice * plugin.eventManager.multiplierFor(mat)
                val totalCost = unit * amt
                val eco = plugin.economy!!
                if (eco.getBalance(sender) + 1e-9 < totalCost) { sender.sendMessage("${ChatColor.RED}You need ${format(totalCost)}."); return true }
                val res = eco.withdrawPlayer(sender, totalCost)
                if (!res.transactionSuccess()) { sender.sendMessage("${ChatColor.RED}Payment failed: ${res.errorMessage}"); return true }
                // Buy an investment certificate tracking material and principal paid
                val inv = investments.buy(sender.uniqueId, mat.name, totalCost)
                sender.sendMessage("${ChatColor.GOLD}[The Endex] ${ChatColor.GREEN}Purchased investment in ${prettyName(mat)} for ${format(totalCost)} at APR ${investments.defaultApr()}%.")
                return true
            }
            "list" -> {
                val list = investments.list(sender.uniqueId)
                if (list.isEmpty()) { sender.sendMessage("${ChatColor.GRAY}No active investments."); return true }
                sender.sendMessage("${ChatColor.GOLD}[The Endex] ${ChatColor.AQUA}Your investments:")
                list.forEach { s ->
                    sender.sendMessage("${ChatColor.GRAY}- ${ChatColor.AQUA}${s.material}${ChatColor.GRAY} ${s.id.take(8)}…  P: ${format(s.principal)}  Accrued: ${ChatColor.GREEN}${format(s.accrued)}")
                }
                return true
            }
            "redeem-all" -> {
                if (plugin.economy == null) { sender.sendMessage("${ChatColor.RED}Economy unavailable."); return true }
                val (payout, count) = investments.redeemAll(sender.uniqueId)
                if (count == 0) { sender.sendMessage("${ChatColor.GRAY}No active investments."); return true }
                val eco = plugin.economy!!
                val res = eco.depositPlayer(sender, payout)
                if (!res.transactionSuccess()) { sender.sendMessage("${ChatColor.RED}Payout failed: ${res.errorMessage}"); return true }
                sender.sendMessage("${ChatColor.GOLD}[The Endex] ${ChatColor.GREEN}Redeemed $count investment(s) for ${format(payout)}.")
                return true
            }
        }
        sender.sendMessage("${ChatColor.RED}Unknown subcommand. Use /market invest [buy|list|redeem-all]")
        return true
    }

    private fun handleEvent(sender: CommandSender, args: Array<out String>): Boolean {
        if (!sender.hasPermission("theendex.admin")) {
            sender.sendMessage("${ChatColor.RED}No permission.")
            return true
        }
        if (args.size == 1) {
            sender.sendMessage("${ChatColor.GOLD}[The Endex] ${ChatColor.AQUA}Events:")
            val defs = plugin.eventManager.listEvents()
            if (defs.isEmpty()) sender.sendMessage("${ChatColor.GRAY}(none)") else defs.forEach {
                val target = it.affectedCategory ?: it.affected
                val bc = if (it.broadcast) "${ChatColor.GREEN}broadcast" else "${ChatColor.DARK_GRAY}silent"
                sender.sendMessage("${ChatColor.AQUA}${it.name}${ChatColor.GRAY} -> ${target} x${it.multiplier} ${it.durationMinutes}m ${bc}")
            }
            val act = plugin.eventManager.listActive()
            if (act.isNotEmpty()) sender.sendMessage("${ChatColor.GOLD}Active:")
            act.forEach { sender.sendMessage("${ChatColor.AQUA}${it.event.name}${ChatColor.GRAY} ends ${ChatColor.YELLOW}${java.time.Duration.between(java.time.Instant.now(), it.endsAt).toMinutes()}m") }
            sender.sendMessage("${ChatColor.GRAY}Use ${ChatColor.AQUA}/market event <name>${ChatColor.GRAY} to trigger, ${ChatColor.AQUA}/market event end <name>${ChatColor.GRAY} to end, ${ChatColor.AQUA}/market event clear${ChatColor.GRAY} to clear all.")
            return true
        }
        if (args[1].equals("clear", ignoreCase = true)) {
            val n = plugin.eventManager.clearAll()
            sender.sendMessage("${ChatColor.GOLD}[The Endex] ${ChatColor.GRAY}Cleared ${ChatColor.AQUA}$n${ChatColor.GRAY} active event(s).")
            return true
        }
        if (args[1].equals("end", ignoreCase = true)) {
            if (args.size < 3) { sender.sendMessage("${ChatColor.RED}Usage: /market event end <name>"); return true }
            val name = args.drop(2).joinToString(" ")
            val ok = plugin.eventManager.end(name)
            if (!ok) sender.sendMessage("${ChatColor.RED}Event not found or not active: $name")
            return true
        }
        val name = args.drop(1).joinToString(" ")
        val ok = plugin.eventManager.trigger(name)
        if (!ok) sender.sendMessage("${ChatColor.RED}Unknown event: $name")
        return true
    }

    private fun handleDelivery(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}Only players can use delivery commands.")
            return true
        }
        
        val deliveryMgr = plugin.getDeliveryManager()
        if (deliveryMgr == null || !plugin.config.getBoolean("delivery.enabled", true)) {
            sender.sendMessage("${ChatColor.RED}Delivery system is not enabled.")
            return true
        }
        
        // /market delivery (default: list)
        if (args.size == 1) {
            return handleDeliveryList(sender, deliveryMgr)
        }
        
        return when (args[1].lowercase()) {
            "list" -> handleDeliveryList(sender, deliveryMgr)
            "claim" -> handleDeliveryClaim(sender, deliveryMgr, args)
            "claim-all", "claimall" -> handleDeliveryClaimAll(sender, deliveryMgr)
            "gui" -> {
                // Open deliveries GUI panel directly
                plugin.marketGUI.open(sender)
                sender.sendMessage("${ChatColor.GOLD}[TheEndex] ${ChatColor.YELLOW}Click the ${ChatColor.LIGHT_PURPLE}Ender Chest ${ChatColor.YELLOW}to view deliveries.")
                true
            }
            else -> {
                sender.sendMessage("${ChatColor.RED}Usage: /market delivery [list|claim|claim-all|gui]")
                true
            }
        }
    }
    
    private fun handleDeliveryList(player: Player, deliveryMgr: org.lokixcz.theendex.delivery.DeliveryManager): Boolean {
        val pending = deliveryMgr.listPending(player.uniqueId)
        
        if (pending.isEmpty()) {
            player.sendMessage("${ChatColor.GOLD}[TheEndex] ${ChatColor.GRAY}You have no pending deliveries.")
            player.sendMessage("${ChatColor.DARK_GRAY}Items purchased with full inventory will appear here.")
            return true
        }
        
        val totalCount = pending.values.sum()
        player.sendMessage("${ChatColor.GOLD}═══════════════════════════════════════")
        player.sendMessage("${ChatColor.LIGHT_PURPLE}${ChatColor.BOLD}Pending Deliveries ${ChatColor.GRAY}($totalCount items)")
        player.sendMessage("${ChatColor.GOLD}═══════════════════════════════════════")
        
        pending.entries.sortedByDescending { it.value }.forEach { (material, amount) ->
            val prettyName = material.name.lowercase().split('_').joinToString(" ") { 
                it.replaceFirstChar { c -> c.titlecase() } 
            }
            player.sendMessage("${ChatColor.AQUA}$prettyName: ${ChatColor.YELLOW}$amount")
        }
        
        player.sendMessage("${ChatColor.GOLD}═══════════════════════════════════════")
        player.sendMessage("${ChatColor.GRAY}Use ${ChatColor.YELLOW}/market delivery claim <material> ${ChatColor.GRAY}to claim specific items")
        player.sendMessage("${ChatColor.GRAY}Use ${ChatColor.YELLOW}/market delivery claim-all ${ChatColor.GRAY}to claim everything")
        player.sendMessage("${ChatColor.GRAY}Or click the ${ChatColor.LIGHT_PURPLE}Ender Chest ${ChatColor.GRAY}in ${ChatColor.YELLOW}/market ${ChatColor.GRAY}GUI")
        return true
    }
    
    private fun handleDeliveryClaim(player: Player, deliveryMgr: org.lokixcz.theendex.delivery.DeliveryManager, args: Array<out String>): Boolean {
        if (args.size < 3) {
            player.sendMessage("${ChatColor.RED}Usage: /market delivery claim <material> [amount]")
            player.sendMessage("${ChatColor.GRAY}Example: /market delivery claim diamond 64")
            return true
        }
        
        val mat = Material.matchMaterial(args[2].uppercase())
        if (mat == null) {
            player.sendMessage("${ChatColor.RED}Unknown material: ${args[2]}")
            return true
        }
        
        val requestedAmount = if (args.size >= 4) {
            args[3].toIntOrNull()?.takeIf { it > 0 } ?: run {
                player.sendMessage("${ChatColor.RED}Invalid amount: ${args[3]}")
                return true
            }
        } else {
            Int.MAX_VALUE // Claim all by default
        }
        
        val result = deliveryMgr.claimMaterial(player, mat, requestedAmount)
        
        if (result.error != null) {
            player.sendMessage("${ChatColor.RED}[TheEndex] ${result.error}")
            return true
        }
        
        if (result.delivered > 0) {
            val prettyName = mat.name.lowercase().split('_').joinToString(" ") { 
                it.replaceFirstChar { c -> c.titlecase() } 
            }
            player.sendMessage("${ChatColor.GREEN}[TheEndex] Claimed ${ChatColor.GOLD}${result.delivered} $prettyName${ChatColor.GREEN}!")
        }
        
        if (result.remainingPending > 0) {
            player.sendMessage("${ChatColor.YELLOW}[TheEndex] ${result.remainingPending} items still pending (inventory full).")
            player.sendMessage("${ChatColor.GRAY}Make space and claim again to get the rest.")
        }
        
        return true
    }
    
    private fun handleDeliveryClaimAll(player: Player, deliveryMgr: org.lokixcz.theendex.delivery.DeliveryManager): Boolean {
        val result = deliveryMgr.claimAll(player)
        
        if (result.error != null) {
            player.sendMessage("${ChatColor.RED}[TheEndex] ${result.error}")
            return true
        }
        
        if (result.delivered.isEmpty()) {
            player.sendMessage("${ChatColor.GRAY}[TheEndex] No items were claimed (inventory may be full).")
            return true
        }
        
        val totalClaimed = result.delivered.values.sum()
        player.sendMessage("${ChatColor.GREEN}[TheEndex] Claimed ${ChatColor.GOLD}$totalClaimed ${ChatColor.GREEN}items!")
        
        result.delivered.entries.sortedByDescending { it.value }.take(5).forEach { (material, count) ->
            val prettyName = material.name.lowercase().split('_').joinToString(" ") { 
                it.replaceFirstChar { c -> c.titlecase() } 
            }
            player.sendMessage("${ChatColor.GRAY}  • ${ChatColor.AQUA}$prettyName: ${ChatColor.GOLD}$count")
        }
        
        if (result.delivered.size > 5) {
            player.sendMessage("${ChatColor.DARK_GRAY}  ... and ${result.delivered.size - 5} more materials")
        }
        
        if (result.totalRemaining > 0) {
            player.sendMessage("${ChatColor.YELLOW}[TheEndex] ${result.totalRemaining} items still pending (inventory full).")
            player.sendMessage("${ChatColor.GRAY}Make space and use ${ChatColor.YELLOW}/market delivery claim-all ${ChatColor.GRAY}again.")
        }
        
        return true
    }
    
    // ═══════════════════════════════════════════════════════════════════════════
    // VIRTUAL HOLDINGS SYSTEM
    // ═══════════════════════════════════════════════════════════════════════════
    
    private fun handleHoldings(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}Only players can view holdings.")
            return true
        }
        
        val db = plugin.marketManager.sqliteStore()
        if (db == null || !plugin.config.getBoolean("holdings.enabled", true)) {
            sender.sendMessage("${ChatColor.RED}Holdings system is not enabled.")
            return true
        }
        
        val holdings = db.listHoldings(sender.uniqueId.toString())
        
        if (holdings.isEmpty()) {
            sender.sendMessage("${ChatColor.GOLD}[TheEndex] ${ChatColor.GRAY}You have no items in holdings.")
            sender.sendMessage("${ChatColor.DARK_GRAY}Buy items with /market buy to add them to holdings.")
            return true
        }
        
        val totalItems = holdings.values.sumOf { it.first }
        val maxHoldings = plugin.config.getInt("holdings.max-total-per-player", 100000)
        
        sender.sendMessage("${ChatColor.GOLD}═══════════════════════════════════════")
        sender.sendMessage("${ChatColor.LIGHT_PURPLE}${ChatColor.BOLD}Your Holdings ${ChatColor.GRAY}($totalItems / $maxHoldings items)")
        sender.sendMessage("${ChatColor.GOLD}═══════════════════════════════════════")
        
        var totalValue = 0.0
        var totalCost = 0.0
        
        holdings.entries.sortedByDescending { it.value.first }.take(15).forEach { (material, pair) ->
            val (qty, avgCost) = pair
            val marketItem = plugin.marketManager.get(material)
            val currentPrice = marketItem?.currentPrice ?: 0.0
            val value = currentPrice * qty
            val cost = avgCost * qty
            totalValue += value
            totalCost += cost
            
            val pnl = value - cost
            val pnlColor = when {
                pnl > 0.01 -> ChatColor.GREEN
                pnl < -0.01 -> ChatColor.RED
                else -> ChatColor.GRAY
            }
            val pnlSign = if (pnl >= 0) "+" else ""
            
            sender.sendMessage("${ChatColor.AQUA}${prettyName(material)}: ${ChatColor.YELLOW}$qty ${ChatColor.GRAY}@ ${ChatColor.GREEN}${format(currentPrice)} ${pnlColor}(${pnlSign}${format(pnl)})")
        }
        
        if (holdings.size > 15) {
            sender.sendMessage("${ChatColor.DARK_GRAY}  ... and ${holdings.size - 15} more materials")
        }
        
        val totalPnl = totalValue - totalCost
        val pnlColor = when {
            totalPnl > 0.01 -> ChatColor.GREEN
            totalPnl < -0.01 -> ChatColor.RED
            else -> ChatColor.GRAY
        }
        val pnlSign = if (totalPnl >= 0) "+" else ""
        
        sender.sendMessage("${ChatColor.GOLD}═══════════════════════════════════════")
        sender.sendMessage("${ChatColor.GRAY}Total Value: ${ChatColor.GREEN}${format(totalValue)} ${pnlColor}(${pnlSign}${format(totalPnl)} P/L)")
        sender.sendMessage("${ChatColor.GRAY}Use ${ChatColor.YELLOW}/market withdraw <material> [amount] ${ChatColor.GRAY}to claim items")
        sender.sendMessage("${ChatColor.GRAY}Or open ${ChatColor.YELLOW}/market ${ChatColor.GRAY}GUI and click ${ChatColor.LIGHT_PURPLE}Holdings")
        
        return true
    }
    
    private fun handleWithdraw(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}Only players can withdraw from holdings.")
            return true
        }
        
        val db = plugin.marketManager.sqliteStore()
        if (db == null || !plugin.config.getBoolean("holdings.enabled", true)) {
            sender.sendMessage("${ChatColor.RED}Holdings system is not enabled.")
            return true
        }
        
        // /market withdraw (no args) - show help or withdraw all
        if (args.size == 1) {
            sender.sendMessage("${ChatColor.GOLD}[TheEndex] ${ChatColor.AQUA}Withdraw Commands:")
            sender.sendMessage("${ChatColor.YELLOW}/market withdraw <material> ${ChatColor.GRAY}- Withdraw all of a material")
            sender.sendMessage("${ChatColor.YELLOW}/market withdraw <material> <amount> ${ChatColor.GRAY}- Withdraw specific amount")
            sender.sendMessage("${ChatColor.YELLOW}/market withdraw all ${ChatColor.GRAY}- Withdraw everything possible")
            sender.sendMessage("${ChatColor.GRAY}Use ${ChatColor.YELLOW}/market holdings ${ChatColor.GRAY}to see what you have.")
            return true
        }
        
        // /market withdraw all
        if (args[1].equals("all", ignoreCase = true)) {
            return handleWithdrawAll(sender, db)
        }
        
        // /market withdraw <material> [amount]
        val mat = Material.matchMaterial(args[1].uppercase())
        if (mat == null) {
            sender.sendMessage("${ChatColor.RED}Unknown material: ${args[1]}")
            return true
        }
        
        val holding = db.getHolding(sender.uniqueId.toString(), mat)
        if (holding == null || holding.first <= 0) {
            sender.sendMessage("${ChatColor.RED}You don't have any ${prettyName(mat)} in holdings.")
            return true
        }
        
        val (available, _) = holding
        val requestedAmount = if (args.size >= 3) {
            args[2].toIntOrNull()?.takeIf { it > 0 } ?: run {
                sender.sendMessage("${ChatColor.RED}Invalid amount: ${args[2]}")
                return true
            }
        } else {
            available // Withdraw all by default
        }
        
        val toWithdraw = minOf(requestedAmount, available)
        val capacity = calculateInventoryCapacity(sender, mat)
        
        if (capacity <= 0) {
            sender.sendMessage("${ChatColor.RED}Your inventory is full. Please make space and try again.")
            return true
        }
        
        val actualWithdraw = minOf(toWithdraw, capacity)
        
        // Remove from holdings
        val removed = db.removeFromHoldings(sender.uniqueId.toString(), mat, actualWithdraw)
        if (removed <= 0) {
            sender.sendMessage("${ChatColor.RED}Failed to withdraw items. Please try again.")
            return true
        }
        
        // Give items to player
        var remaining = removed
        while (remaining > 0) {
            val toGive = minOf(remaining, mat.maxStackSize)
            val stack = ItemStack(mat, toGive)
            val leftovers = sender.inventory.addItem(stack)
            if (leftovers.isNotEmpty()) {
                // Should not happen since we checked capacity, but safety fallback
                leftovers.values.forEach { sender.world.dropItemNaturally(sender.location, it) }
            }
            remaining -= toGive
        }
        
        sender.sendMessage("${ChatColor.GREEN}[TheEndex] Withdrew ${ChatColor.GOLD}$removed ${prettyName(mat)}${ChatColor.GREEN} to inventory!")
        
        val newHolding = db.getHolding(sender.uniqueId.toString(), mat)
        val stillHave = newHolding?.first ?: 0
        if (stillHave > 0) {
            sender.sendMessage("${ChatColor.YELLOW}[TheEndex] $stillHave ${prettyName(mat)} still in holdings (inventory was full).")
        }
        
        return true
    }
    
    private fun handleWithdrawAll(player: Player, db: org.lokixcz.theendex.market.SqliteStore): Boolean {
        val holdings = db.listHoldings(player.uniqueId.toString())
        
        if (holdings.isEmpty()) {
            player.sendMessage("${ChatColor.GRAY}[TheEndex] No items in holdings to withdraw.")
            return true
        }
        
        var totalWithdrawn = 0
        val withdrawn = mutableMapOf<Material, Int>()
        var totalRemaining = 0
        
        for ((mat, pair) in holdings) {
            val (available, _) = pair
            if (available <= 0) continue
            
            val capacity = calculateInventoryCapacity(player, mat)
            if (capacity <= 0) {
                totalRemaining += available
                continue
            }
            
            val toWithdraw = minOf(available, capacity)
            val removed = db.removeFromHoldings(player.uniqueId.toString(), mat, toWithdraw)
            
            if (removed > 0) {
                // Give items
                var remaining = removed
                while (remaining > 0) {
                    val toGive = minOf(remaining, mat.maxStackSize)
                    val stack = ItemStack(mat, toGive)
                    val leftovers = player.inventory.addItem(stack)
                    if (leftovers.isNotEmpty()) {
                        leftovers.values.forEach { player.world.dropItemNaturally(player.location, it) }
                    }
                    remaining -= toGive
                }
                
                totalWithdrawn += removed
                withdrawn[mat] = removed
                
                // Track remaining
                val stillHave = available - removed
                if (stillHave > 0) totalRemaining += stillHave
            } else {
                totalRemaining += available
            }
        }
        
        if (totalWithdrawn == 0) {
            player.sendMessage("${ChatColor.GRAY}[TheEndex] No items were withdrawn (inventory may be full).")
            return true
        }
        
        player.sendMessage("${ChatColor.GREEN}[TheEndex] Withdrew ${ChatColor.GOLD}$totalWithdrawn ${ChatColor.GREEN}items!")
        
        withdrawn.entries.sortedByDescending { it.value }.take(5).forEach { (mat, count) ->
            player.sendMessage("${ChatColor.GRAY}  • ${ChatColor.AQUA}${prettyName(mat)}: ${ChatColor.GOLD}$count")
        }
        
        if (withdrawn.size > 5) {
            player.sendMessage("${ChatColor.DARK_GRAY}  ... and ${withdrawn.size - 5} more materials")
        }
        
        if (totalRemaining > 0) {
            player.sendMessage("${ChatColor.YELLOW}[TheEndex] $totalRemaining items still in holdings (inventory full).")
            player.sendMessage("${ChatColor.GRAY}Make space and use ${ChatColor.YELLOW}/market withdraw all ${ChatColor.GRAY}again.")
        }
        
        return true
    }
}
