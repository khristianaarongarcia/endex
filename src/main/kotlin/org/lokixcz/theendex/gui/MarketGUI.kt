package org.lokixcz.theendex.gui

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.lokixcz.theendex.Endex
import org.lokixcz.theendex.market.PricePoint
import org.lokixcz.theendex.market.MarketItem
import java.util.*

class MarketGUI(private val plugin: Endex) : Listener {
    private val titleBase = "${ChatColor.DARK_PURPLE}The Endex"
    private val pageSize = 45 // 5 rows for items, last row for controls

    private val amounts = listOf(1, 8, 16, 32, 64)
    private enum class SortBy { NAME, PRICE, CHANGE }
    private enum class Category { ALL, ORES, FARMING, MOB_DROPS, BLOCKS }
    private data class State(
        var page: Int = 0,
        var amountIdx: Int = 0,
        var sort: SortBy = SortBy.NAME, // default A–Z
        var category: Category = Category.ALL, // default show all categories
        var groupBy: Boolean = true, // default grouped view in GUI as well
        var search: String = "",
        var inDetails: Boolean = false,
        var detailOf: Material? = null
    )
    private val states: MutableMap<UUID, State> = mutableMapOf()
    private val awaitingSearchInput: MutableSet<UUID> = mutableSetOf()

    fun open(player: Player, page: Int = 0) {
        val state = states[player.uniqueId] ?: load(player)
        state.page = page.coerceAtLeast(0)

        val itemsRaw = plugin.marketManager.allItems().toList()
        val filtered = itemsRaw.filter { mi ->
            val matchesCategory = when (state.category) {
                Category.ALL -> true
                Category.ORES -> mi.material.name.contains("_ORE") || mi.material.name.endsWith("_INGOT") || mi.material.name.endsWith("_BLOCK")
                Category.FARMING -> mi.material.name.contains("WHEAT") || mi.material.name.contains("SEEDS") || mi.material.name.contains("CARROT") || mi.material.name.contains("POTATO") || mi.material.name.contains("BEETROOT") || mi.material.name.contains("CROP")
                Category.MOB_DROPS -> mi.material.name in listOf("ROTTEN_FLESH","BONE","STRING","SPIDER_EYE","ENDER_PEARL","GUNPOWDER","BLAZE_ROD","GHAST_TEAR","SLIME_BALL")
                Category.BLOCKS -> mi.material.isBlock
            }
            val matchesSearch = state.search.isBlank() || mi.material.name.contains(state.search, ignoreCase = true)
            matchesCategory && matchesSearch
        }
        val items = filtered.sortedWith { a, b ->
            when (state.sort) {
                SortBy.NAME -> prettyName(a.material).lowercase().compareTo(prettyName(b.material).lowercase())
                SortBy.PRICE -> a.currentPrice.compareTo(b.currentPrice)
                SortBy.CHANGE -> changePercent(a.history).compareTo(changePercent(b.history))
            }
        }

        // Build entries, optionally with category headers when viewing ALL
        data class Entry(val header: String? = null, val item: MarketItem? = null)
        val entries: List<Entry> = if (state.groupBy && state.category == Category.ALL) {
            val grouped = items.groupBy { catNameFor(it.material) }
            val cats = grouped.keys.sortedWith(String.CASE_INSENSITIVE_ORDER)
            val list = mutableListOf<Entry>()
            for (c in cats) {
                val sorted = grouped[c]?.sortedBy { prettyName(it.material).lowercase() } ?: emptyList()
                if (sorted.isEmpty()) continue
                list += Entry(header = c)
                list += sorted.map { Entry(item = it) }
            }
            list
        } else {
            items.map { Entry(item = it) }
        }

        val totalPages = if (entries.isEmpty()) 1 else ((entries.size - 1) / pageSize + 1)
        if (state.page > totalPages - 1) state.page = totalPages - 1
        val from = state.page * pageSize
        val to = (from + pageSize).coerceAtMost(entries.size)
        val pageEntries = if (from in 0..entries.size) entries.subList(from, to) else emptyList()

        val inv: Inventory = Bukkit.createInventory(player, 54, "$titleBase ${ChatColor.DARK_GRAY}[${state.sort.name}] ${ChatColor.GRAY}(${state.page + 1}/$totalPages)")

        pageEntries.forEachIndexed { idx, en ->
            if (en.header != null) {
                val display = ItemStack(Material.PURPLE_STAINED_GLASS_PANE)
                val meta: ItemMeta = display.itemMeta
                meta.setDisplayName("${ChatColor.LIGHT_PURPLE}${en.header}")
                meta.lore = listOf("${ChatColor.DARK_GRAY}Category section")
                display.itemMeta = meta
                inv.setItem(idx, display)
            } else {
                val mi = en.item ?: return@forEachIndexed
                val display = ItemStack(mi.material.takeIf { it != Material.AIR } ?: Material.PAPER)
                val meta: ItemMeta = display.itemMeta
                meta.setDisplayName("${ChatColor.AQUA}${prettyName(mi.material)}")

                val mul = plugin.eventManager.multiplierFor(mi.material)
                val current = mi.currentPrice
                val list = mi.history.toList()
                val prev = if (list.size >= 2) list[list.lastIndex - 1].price else current
                val diff = current - prev
                val pct = if (prev != 0.0) (diff / prev * 100.0) else 0.0
                val arrow = when {
                    diff > 0.0001 -> "${ChatColor.GREEN}↑"
                    diff < -0.0001 -> "${ChatColor.RED}↓"
                    else -> "${ChatColor.YELLOW}→"
                }
                // Show last cycle demand/supply in percent impact terms
                val ds = mi.lastDemand - mi.lastSupply
                val sens = plugin.config.getDouble("price-sensitivity", 0.05)
                val estPct = ds * sens * 100.0

                val last5 = mi.history.takeLast(5).map { String.format("%.2f", it.price) }
                val bal = plugin.economy?.getBalance(player) ?: 0.0
                val invCount = player.inventory.contents.filterNotNull().filter { it.type == mi.material }.sumOf { it.amount }
                val loreCore = mutableListOf<String>()
                loreCore += "${ChatColor.GRAY}Price: ${ChatColor.GREEN}${format(current)} ${ChatColor.GRAY}(${arrow} ${format(diff)}, ${formatPct(pct)})"
                loreCore += "${ChatColor.DARK_GRAY}Last cycle: ${ChatColor.GRAY}Demand ${format(mi.lastDemand)} / Supply ${format(mi.lastSupply)} (${formatPct(estPct)})"
                if (mul != 1.0) loreCore += "${ChatColor.DARK_AQUA}Event: x${format(mul)} ${ChatColor.GRAY}Eff: ${ChatColor.GREEN}${format(current*mul)}"
                loreCore += "${ChatColor.DARK_GRAY}Min ${format(mi.minPrice)}  Max ${format(mi.maxPrice)}"
                loreCore += "${ChatColor.GRAY}History: ${last5.joinToString(" ${ChatColor.DARK_GRAY}| ${ChatColor.GRAY}")}"
                loreCore += "${ChatColor.DARK_GRAY}Left: Buy  Right: Sell  Amount: ${amounts[state.amountIdx]}"
                loreCore += "${ChatColor.DARK_GRAY}Shift/Middle-click: Details"
                loreCore += "${ChatColor.GRAY}You have: ${ChatColor.AQUA}$invCount ${mi.material}"
                loreCore += "${ChatColor.GRAY}Balance: ${ChatColor.GOLD}${format(bal)}"
                meta.lore = loreCore
                display.itemMeta = meta
                inv.setItem(idx, display)
            }
        }

        // Controls (last row)
        inv.setItem(45, namedItem(Material.ARROW, "${ChatColor.YELLOW}Previous Page"))
        inv.setItem(46, namedItem(Material.BOOK, "${ChatColor.AQUA}Category: ${state.category.name} ${ChatColor.GRAY}(click)"))
        inv.setItem(47, namedItem(Material.LECTERN, "${ChatColor.LIGHT_PURPLE}Group: ${if (state.groupBy) "Category A–Z (ON)" else "Off"} ${ChatColor.GRAY}(click)"))
        inv.setItem(48, namedItem(Material.OAK_SIGN, "${ChatColor.AQUA}Search: ${if (state.search.isBlank()) "${ChatColor.DARK_GRAY}<none>" else state.search} ${ChatColor.GRAY}(Left: set, Right: clear)"))
        inv.setItem(49, namedItem(Material.HOPPER, "${ChatColor.AQUA}Amount: ${amounts[state.amountIdx]} ${ChatColor.GRAY}(click)"))
        inv.setItem(50, namedItem(Material.COMPARATOR, "${ChatColor.LIGHT_PURPLE}Sort: ${state.sort.name} ${ChatColor.GRAY}(click)"))
        
        // Deliveries button
        val deliveryMgr = plugin.getDeliveryManager()
        if (deliveryMgr != null && plugin.config.getBoolean("delivery.enabled", true)) {
            val pending = deliveryMgr.listPending(player.uniqueId)
            val totalCount = pending.values.sum()
            val deliveryIcon = ItemStack(Material.ENDER_CHEST)
            val deliveryMeta = deliveryIcon.itemMeta
            deliveryMeta.setDisplayName("${ChatColor.LIGHT_PURPLE}Pending Deliveries ${ChatColor.GRAY}(click)")
            val deliveryLore = mutableListOf<String>()
            if (totalCount > 0) {
                deliveryLore += "${ChatColor.GOLD}You have ${ChatColor.YELLOW}$totalCount ${ChatColor.GOLD}item(s) pending"
                deliveryLore += "${ChatColor.DARK_GRAY}Click to view and claim"
            } else {
                deliveryLore += "${ChatColor.GRAY}No pending deliveries"
            }
            deliveryMeta.lore = deliveryLore
            deliveryIcon.itemMeta = deliveryMeta
            inv.setItem(51, deliveryIcon)
        }
        
        inv.setItem(53, namedItem(Material.ARROW, "${ChatColor.YELLOW}Next Page"))

        player.openInventory(inv)
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        val title = e.view.title
        if (!title.startsWith(titleBase)) return
        e.isCancelled = true
        val state = states.getOrPut(player.uniqueId) { State() }

        val slot = e.rawSlot
        if (slot in 0 until pageSize) {
            val clicked = e.currentItem ?: return
            val mat = clicked.type.takeIf { it != Material.AIR } ?: return
            if (plugin.marketManager.get(mat) == null) return
            val amount = amounts[state.amountIdx]
            // Details view open on shift-left or middle click
            if (e.isShiftClick && e.isLeftClick || e.click == ClickType.MIDDLE) {
                openDetails(player, mat)
            } else if (e.isLeftClick) {
                Bukkit.getScheduler().runTask(plugin, Runnable {
                    player.performCommand("market buy ${mat.name} $amount")
                    open(player, state.page)
                })
            } else if (e.isRightClick) {
                Bukkit.getScheduler().runTask(plugin, Runnable {
                    player.performCommand("market sell ${mat.name} $amount")
                    open(player, state.page)
                })
            }
            return
        }

        when (slot) {
            45 -> { // prev
                state.page = (state.page - 1).coerceAtLeast(0)
                open(player, state.page)
            }
            46 -> { // category
                state.category = when (state.category) {
                    Category.ALL -> Category.ORES
                    Category.ORES -> Category.FARMING
                    Category.FARMING -> Category.MOB_DROPS
                    Category.MOB_DROPS -> Category.BLOCKS
                    Category.BLOCKS -> Category.ALL
                }
                persist(player, state)
                open(player, 0)
            }
            47 -> { // grouping toggle
                state.groupBy = !state.groupBy
                persist(player, state)
                open(player, 0)
            }
            48 -> { // search prompt or clear
                if (e.isRightClick) {
                    state.search = ""
                    persist(player, state)
                    open(player, 0)
                } else {
                    player.closeInventory()
                    player.sendMessage("${ChatColor.GOLD}[TheEndex] ${ChatColor.YELLOW}Type your search query in chat. Send empty message to clear.")
                    awaitingSearchInput.add(player.uniqueId)
                }
            }
            49 -> { // amount cycle
                state.amountIdx = (state.amountIdx + 1) % amounts.size
                persist(player, state)
                open(player, state.page)
            }
            50 -> { // sort cycle
                state.sort = when (state.sort) {
                    SortBy.NAME -> SortBy.PRICE
                    SortBy.PRICE -> SortBy.CHANGE
                    SortBy.CHANGE -> SortBy.NAME
                }
                persist(player, state)
                open(player, 0)
            }
            51 -> { // deliveries
                openDeliveries(player)
            }
            53 -> { // next
                state.page += 1
                open(player, state.page)
            }
        }
    }

    @EventHandler
    fun onClose(e: InventoryCloseEvent) {
        val player = e.player as? Player ?: return
        val title = e.view.title
        if (!title.startsWith(titleBase)) return
        states[player.uniqueId]?.let { persist(player, it) }
    }

    @EventHandler
    fun onChat(e: AsyncPlayerChatEvent) {
        val player = e.player
        val uuid = player.uniqueId
        if (!awaitingSearchInput.contains(uuid)) return
        e.isCancelled = true
        val state = states.getOrPut(uuid) { load(player) }
        state.search = e.message.trim()
        awaitingSearchInput.remove(uuid)
        persist(player, state)
        Bukkit.getScheduler().runTask(plugin, Runnable { open(player, 0) })
    }

    private fun changePercent(history: Collection<PricePoint>): Double {
        if (history.size < 2) return 0.0
        val list = history.toList()
        val prev = list[list.lastIndex - 1].price
        val last = list.last().price
        if (prev == 0.0) return 0.0
        return (last - prev) / prev * 100.0
    }

    private fun namedItem(mat: Material, name: String): ItemStack = ItemStack(mat).apply {
        itemMeta = itemMeta.apply { setDisplayName(name) }
    }

    private fun format(n: Double): String = String.format("%.2f", n)
    private fun formatPct(n: Double): String = String.format("%.2f%%", n)
    private fun prettyName(mat: Material): String = mat.name.lowercase().split('_').joinToString(" ") { it.replaceFirstChar { c -> c.titlecase() } }
    private fun catNameFor(mat: Material): String {
        val n = mat.name
        return when {
            n.contains("_ORE") || n.endsWith("_INGOT") || n.endsWith("_BLOCK") -> "Ores"
            listOf("WHEAT","SEEDS","CARROT","POTATO","BEETROOT","MELON","PUMPKIN","SUGAR","BAMBOO","COCOA").any { n.contains(it) } -> "Farming"
            n in setOf("ROTTEN_FLESH","BONE","STRING","SPIDER_EYE","ENDER_PEARL","GUNPOWDER","BLAZE_ROD","GHAST_TEAR","SLIME_BALL","LEATHER","FEATHER") -> "Mob Drops"
            mat.isBlock -> "Blocks"
            else -> "Misc"
        }
    }

    private fun persist(player: Player, state: State) {
        plugin.prefsStore.save(player.uniqueId, mapOf(
            "amountIdx" to state.amountIdx,
            "sort" to state.sort.name,
            "category" to state.category.name,
            "groupBy" to state.groupBy,
            "search" to state.search,
            "page" to state.page
        ))
    }

    private fun load(player: Player): State {
        val m = plugin.prefsStore.load(player.uniqueId)
        val amountIdx = (m["amountIdx"] as? Int) ?: 0
        val sort = runCatching { SortBy.valueOf((m["sort"] as? String ?: "NAME")) }.getOrDefault(SortBy.NAME)
        val category = runCatching { Category.valueOf((m["category"] as? String ?: "ALL")) }.getOrDefault(Category.ALL)
        val groupBy = (m["groupBy"] as? Boolean) ?: true
        val search = (m["search"] as? String) ?: ""
        val page = (m["page"] as? Int) ?: 0
        val s = State(page, amountIdx, sort, category, groupBy, search)
        states[player.uniqueId] = s
        return s
    }

    // Ensure state is loaded before opening
    override fun toString(): String = "MarketGUI"

    // Public: Refresh GUI for a player if our GUI is open
    fun refreshOpenFor(player: Player) {
        val title = player.openInventory.title
        val state = states[player.uniqueId] ?: return
        Bukkit.getScheduler().runTask(plugin, Runnable {
            when {
                title.startsWith(titleBase) -> open(player, state.page)
                state.inDetails && state.detailOf != null && title.contains("Endex:") -> openDetails(player, state.detailOf!!)
            }
        })
    }

    // Public: Refresh all viewers who currently have the Endex GUI open
    fun refreshAllOpen() {
        plugin.server.onlinePlayers.forEach { p -> refreshOpenFor(p) }
    }

    // Details view
    private fun openDetails(player: Player, mat: Material) {
        val state = states[player.uniqueId] ?: load(player)
        state.inDetails = true
        state.detailOf = mat
        val inv = Bukkit.createInventory(player, 27, "${ChatColor.DARK_PURPLE}Endex: ${ChatColor.AQUA}${prettyName(mat)}")

        val mi = plugin.marketManager.get(mat) ?: run {
            open(player, state.page); return
        }
        val itemDisplay = ItemStack(mat)
        val meta = itemDisplay.itemMeta
    val mul = plugin.eventManager.multiplierFor(mat)
    val current = mi.currentPrice
        val list = mi.history.toList()
        val prev = if (list.size >= 2) list[list.lastIndex - 1].price else current
        val diff = current - prev
        val pct = if (prev != 0.0) (diff / prev * 100.0) else 0.0
        val arrow = when {
            diff > 0.0001 -> "${ChatColor.GREEN}↑"
            diff < -0.0001 -> "${ChatColor.RED}↓"
            else -> "${ChatColor.YELLOW}→"
        }
        val bal = plugin.economy?.getBalance(player) ?: 0.0
        val invCount = player.inventory.contents.filterNotNull().filter { it.type == mat }.sumOf { it.amount }
        meta.setDisplayName("${ChatColor.AQUA}${prettyName(mat)}")
        val lore = mutableListOf<String>()
        lore += "${ChatColor.GRAY}Price: ${ChatColor.GREEN}${format(current)} ${ChatColor.GRAY}(${arrow} ${format(diff)}, ${formatPct(pct)})"
        if (mul != 1.0) lore += "${ChatColor.DARK_AQUA}Event: x${format(mul)} ${ChatColor.GRAY}Eff: ${ChatColor.GREEN}${format(current*mul)}"
        lore += "${ChatColor.DARK_GRAY}Min ${format(mi.minPrice)}  Max ${format(mi.maxPrice)}"
        if (plugin.config.getBoolean("gui.details-chart", true)) {
            val last = mi.history.takeLast(12)
            if (last.isNotEmpty()) {
                val arr = last.map { it.price }
                val min = arr.minOrNull() ?: 0.0
                val max = arr.maxOrNull() ?: 0.0
                val span = (max - min).takeIf { it > 1e-9 } ?: 1.0
                val bars = arr.map {
                    val pct2 = (it - min) / span
                    when {
                        pct2 >= 0.85 -> "█"
                        pct2 >= 0.70 -> "▇"
                        pct2 >= 0.55 -> "▆"
                        pct2 >= 0.40 -> "▅"
                        pct2 >= 0.25 -> "▃"
                        pct2 >= 0.10 -> "▂"
                        else -> "▁"
                    }
                }.joinToString("")
                lore += "${ChatColor.DARK_GRAY}Chart: ${ChatColor.GRAY}${bars}"
            }
        }
        lore += "${ChatColor.GRAY}You have: ${ChatColor.AQUA}$invCount"
        lore += "${ChatColor.GRAY}Balance: ${ChatColor.GOLD}${format(bal)}"
        meta.lore = lore
        itemDisplay.itemMeta = meta
        inv.setItem(13, itemDisplay)

        // Buttons
    inv.setItem(18, namedItem(Material.LIME_DYE, "${ChatColor.GREEN}Buy 1"))
    inv.setItem(20, namedItem(Material.EMERALD_BLOCK, "${ChatColor.GREEN}Buy 64"))
    inv.setItem(24, namedItem(Material.RED_DYE, "${ChatColor.RED}Sell 1"))
    inv.setItem(26, namedItem(Material.BARREL, "${ChatColor.RED}Sell All ($invCount)"))
        inv.setItem(22, namedItem(Material.ARROW, "${ChatColor.YELLOW}Back"))

        player.openInventory(inv)
    }

    @EventHandler
    fun onDetailsClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        val state = states[player.uniqueId] ?: return
        if (!state.inDetails) return
        val mat = state.detailOf ?: return
        val title = e.view.title
        if (!title.contains("Endex:")) return
        e.isCancelled = true
        when (e.rawSlot) {
            18 -> Bukkit.getScheduler().runTask(plugin, Runnable {
                player.performCommand("market buy ${mat.name} 1")
                openDetails(player, mat)
            })
            20 -> Bukkit.getScheduler().runTask(plugin, Runnable {
                player.performCommand("market buy ${mat.name} 64")
                openDetails(player, mat)
            })
            24 -> Bukkit.getScheduler().runTask(plugin, Runnable {
                player.performCommand("market sell ${mat.name} 1")
                openDetails(player, mat)
            })
            26 -> {
                val total = player.inventory.contents.filterNotNull().filter { it.type == mat }.sumOf { it.amount }
                Bukkit.getScheduler().runTask(plugin, Runnable {
                    if (total > 0) player.performCommand("market sell ${mat.name} $total")
                    openDetails(player, mat)
                })
            }
            22 -> { state.inDetails = false; state.detailOf = null; open(player, state.page) }
        }
    }

    // Deliveries panel
    private fun openDeliveries(player: Player) {
        val deliveryMgr = plugin.getDeliveryManager() ?: run {
            player.sendMessage("${ChatColor.RED}[TheEndex] Delivery system is not available.")
            return
        }
        
        val pending = deliveryMgr.listPending(player.uniqueId)
        val inv = Bukkit.createInventory(player, 54, "${ChatColor.DARK_PURPLE}Pending Deliveries")
        
        if (pending.isEmpty()) {
            val noItems = ItemStack(Material.BARRIER)
            val meta = noItems.itemMeta
            meta.setDisplayName("${ChatColor.GRAY}No pending deliveries")
            meta.lore = listOf("${ChatColor.DARK_GRAY}All items have been claimed!")
            noItems.itemMeta = meta
            inv.setItem(22, noItems)
        } else {
            // Display pending materials (up to 45 slots, 5 rows)
            val materials = pending.keys.toList().take(45)
            materials.forEachIndexed { idx, mat ->
                val count = pending[mat] ?: 0
                val display = ItemStack(mat)
                val meta = display.itemMeta
                meta.setDisplayName("${ChatColor.AQUA}${prettyName(mat)}")
                val capacity = deliveryMgr.calculateInventoryCapacity(player, mat)
                meta.lore = listOf(
                    "${ChatColor.GOLD}Pending: ${ChatColor.YELLOW}$count",
                    "${ChatColor.GRAY}Inventory space: ${ChatColor.GREEN}$capacity",
                    "",
                    "${ChatColor.GREEN}Left-click: ${ChatColor.GRAY}Claim as much as fits",
                    "${ChatColor.YELLOW}Right-click: ${ChatColor.GRAY}Claim 1 stack (${mat.maxStackSize})"
                )
                display.itemMeta = meta
                inv.setItem(idx, display)
            }
        }
        
        // Control buttons (last row)
        val claimAllBtn = ItemStack(Material.EMERALD_BLOCK)
        val claimAllMeta = claimAllBtn.itemMeta
        claimAllMeta.setDisplayName("${ChatColor.GREEN}Claim All")
        claimAllMeta.lore = listOf(
            "${ChatColor.GRAY}Claim all pending deliveries",
            "${ChatColor.DARK_GRAY}(as much as fits in inventory)"
        )
        claimAllBtn.itemMeta = claimAllMeta
        inv.setItem(49, claimAllBtn)
        
        val backBtn = ItemStack(Material.ARROW)
        val backMeta = backBtn.itemMeta
        backMeta.setDisplayName("${ChatColor.YELLOW}Back to Market")
        backBtn.itemMeta = backMeta
        inv.setItem(53, backBtn)
        
        player.openInventory(inv)
    }
    
    @EventHandler
    fun onDeliveriesClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        val title = e.view.title
        if (title != "${ChatColor.DARK_PURPLE}Pending Deliveries") return
        e.isCancelled = true
        
        val deliveryMgr = plugin.getDeliveryManager() ?: return
        val slot = e.rawSlot
        val state = states[player.uniqueId] ?: State()
        
        when (slot) {
            in 0..44 -> { // Material slot
                val clicked = e.currentItem ?: return
                val mat = clicked.type.takeIf { it != Material.AIR && it != Material.BARRIER } ?: return
                
                if (e.isLeftClick) {
                    // Claim as much as fits
                    val result = deliveryMgr.claimMaterial(player, mat, Int.MAX_VALUE)
                    if (result.delivered > 0) {
                        player.sendMessage("${ChatColor.GREEN}[TheEndex] Claimed ${ChatColor.GOLD}${result.delivered} ${mat.name}${ChatColor.GREEN}!")
                    }
                    if (result.remainingPending > 0) {
                        player.sendMessage("${ChatColor.YELLOW}[TheEndex] ${result.remainingPending} ${mat.name} still pending (inventory full).")
                    }
                } else if (e.isRightClick) {
                    // Claim 1 stack
                    val result = deliveryMgr.claimMaterial(player, mat, mat.maxStackSize)
                    if (result.delivered > 0) {
                        player.sendMessage("${ChatColor.GREEN}[TheEndex] Claimed ${ChatColor.GOLD}${result.delivered} ${mat.name}${ChatColor.GREEN}!")
                    }
                    if (result.remainingPending > 0) {
                        player.sendMessage("${ChatColor.YELLOW}[TheEndex] ${result.remainingPending} ${mat.name} still pending.")
                    }
                }
                Bukkit.getScheduler().runTask(plugin, Runnable { openDeliveries(player) })
            }
            49 -> { // Claim All
                val result = deliveryMgr.claimAll(player)
                if (result.delivered.isNotEmpty()) {
                    val totalClaimed = result.delivered.values.sum()
                    player.sendMessage("${ChatColor.GREEN}[TheEndex] Claimed ${ChatColor.GOLD}$totalClaimed ${ChatColor.GREEN}items!")
                    result.delivered.forEach { (mat, count) ->
                        player.sendMessage("${ChatColor.GRAY}  • ${ChatColor.AQUA}${prettyName(mat)}: ${ChatColor.GOLD}$count")
                    }
                }
                if (result.totalRemaining > 0) {
                    player.sendMessage("${ChatColor.YELLOW}[TheEndex] ${result.totalRemaining} items still pending (inventory full).")
                }
                Bukkit.getScheduler().runTask(plugin, Runnable { openDeliveries(player) })
            }
            53 -> { // Back
                open(player, state.page)
            }
        }
    }
}
