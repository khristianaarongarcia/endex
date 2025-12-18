package org.lokixcz.theendex.shop

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.lokixcz.theendex.Endex
import org.lokixcz.theendex.gui.MarketActions
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import java.util.*

/**
 * Custom Shop GUI - EconomyShopGUI-style category-based shop interface.
 * 
 * IMPORTANT: This GUI provides a DIFFERENT LAYOUT but IDENTICAL FUNCTIONALITY
 * to the default MarketGUI. All buy/sell operations go through MarketActions
 * which uses the market command system for holdings integration.
 * 
 * Only items that exist in the market (marketManager.get(mat) != null) are tradeable.
 */
class CustomShopGUI(private val plugin: Endex) : Listener {
    
    companion object {
        private const val SHOP_TITLE_PREFIX = "§8"  // Dark gray prefix for shop titles
        private const val ADMIN_PERMISSION = "endex.shop.admin"
    }
    
    // GUI type enum to track what the player is viewing
    private enum class GuiType { MAIN_MENU, CATEGORY, DETAILS, NONE }
    
    // Track player states: shopId -> categoryId -> page
    private val playerStates: MutableMap<UUID, ShopPageState> = mutableMapOf()
    
    // Track which GUI type each player has open (UUID-based, reliable across MC versions)
    private val openGuis: MutableMap<UUID, GuiType> = mutableMapOf()
    
    // Track which shop/category player is viewing
    private val viewingShop: MutableMap<UUID, String> = mutableMapOf()
    
    // Track players waiting for search input
    private val awaitingSearchInput: MutableSet<UUID> = mutableSetOf()
    
    /**
     * Get the CustomShopManager from plugin.
     */
    private fun shopManager(): CustomShopManager? = plugin.customShopManager
    
    // Helper to get inventory view title as String (MC 1.20.1 - 1.21+ compatibility)
    private fun getViewTitleFromView(view: Any): String {
        try {
            val titleMethod = view.javaClass.getMethod("title")
            val component = titleMethod.invoke(view)
            if (component != null) {
                val result = PlainTextComponentSerializer.plainText().serialize(component as net.kyori.adventure.text.Component)
                if (result.isNotEmpty()) return result
            }
        } catch (_: Exception) {}
        
        try {
            val legacyMethod = view.javaClass.getMethod("getTitle")
            val result = legacyMethod.invoke(view)
            if (result is String && result.isNotEmpty()) return result
        } catch (_: Exception) {}
        
        return ""
    }
    
    /**
     * Check if a title belongs to our custom shop GUI.
     */
    private fun isOurShop(title: String): Boolean {
        val stripped = ChatColor.stripColor(title) ?: title
        // Check for our shop title patterns
        val manager = shopManager() ?: return false
        
        for ((_, shop) in manager.all()) {
            val shopTitle = ChatColor.stripColor(shop.menuTitle) ?: shop.menuTitle
            if (stripped.contains(shopTitle) || stripped.startsWith(shopTitle)) return true
            
            for ((_, cat) in shop.categories) {
                val catTitle = ChatColor.stripColor(cat.pageTitle) ?: cat.pageTitle
                if (stripped.contains(catTitle) || stripped.startsWith(catTitle)) return true
            }
        }
        
        return false
    }
    
    /**
     * Open the main shop menu for a player.
     */
    fun openMainMenu(player: Player, shopId: String? = null) {
        val manager = shopManager() ?: run {
            player.sendMessage("${ChatColor.RED}Custom shop system is not loaded.")
            return
        }
        
        val shop = if (shopId != null) manager.get(shopId) else manager.getMainShop()
        if (shop == null) {
            player.sendMessage("${ChatColor.RED}Shop not found: ${shopId ?: manager.mainShopId}")
            return
        }
        
        val inv = Bukkit.createInventory(null, shop.menuSize, shop.menuTitle)
        
        // Fill decoration if enabled
        if (shop.decoration.fillEmpty) {
            val filler = createFillerItem(shop.decoration.emptyMaterial)
            for (i in 0 until shop.menuSize) {
                inv.setItem(i, filler)
            }
        }
        
        // Place border if configured
        shop.decoration.borderMaterial?.let { borderMat ->
            val border = createFillerItem(borderMat)
            for (slot in shop.decoration.borderSlots) {
                if (slot in 0 until shop.menuSize) {
                    inv.setItem(slot, border)
                }
            }
        }
        
        // Process menu layout
        for (slotConfig in shop.menuLayout) {
            if (slotConfig.slot !in 0 until shop.menuSize) continue
            
            val item = when (slotConfig.type) {
                MenuSlotType.CATEGORY -> {
                    val category = shop.categories[slotConfig.categoryId]
                    if (category != null) {
                        createCategoryIcon(category)
                    } else null
                }
                MenuSlotType.DECORATION -> {
                    createDecorItem(slotConfig.material ?: Material.STONE, slotConfig.name, slotConfig.lore)
                }
                MenuSlotType.CLOSE -> {
                    createCloseButton()
                }
                MenuSlotType.INFO -> {
                    createInfoItem(player, slotConfig.name, slotConfig.lore)
                }
                MenuSlotType.EMPTY -> null
            }
            
            inv.setItem(slotConfig.slot, item)
        }
        
        // If no layout defined, auto-arrange categories
        if (shop.menuLayout.isEmpty()) {
            val categories = shop.categories.values.sortedBy { it.sortOrder }
            val startSlot = 10  // Start from second row
            categories.forEachIndexed { idx, cat ->
                val slot = startSlot + idx + (idx / 7) * 2  // Skip borders
                if (slot < shop.menuSize) {
                    inv.setItem(slot, createCategoryIcon(cat))
                }
            }
        }
        
        // Track state BEFORE opening inventory (critical for event handling)
        playerStates[player.uniqueId] = ShopPageState.mainMenu(shop.id)
        viewingShop[player.uniqueId] = shop.id
        openGuis[player.uniqueId] = GuiType.MAIN_MENU
        
        // Play sound
        playSound(player, shop.sounds.openMenu)
        
        player.openInventory(inv)
    }
    
    /**
     * Open a category page.
     */
    fun openCategory(player: Player, shopId: String, categoryId: String, page: Int = 0, amountIdx: Int? = null, search: String? = null, sort: SortBy? = null) {
        val manager = shopManager() ?: return
        val shop = manager.get(shopId) ?: return
        val category = shop.categories[categoryId] ?: return
        
        // Preserve state from existing state if not specified
        val existingState = playerStates[player.uniqueId]
        val actualAmountIdx = amountIdx ?: existingState?.amountIdx ?: 0
        val actualSearch = search ?: existingState?.search ?: ""
        val actualSort = sort ?: existingState?.sort ?: SortBy.NAME
        
        // Get items from market based on filter - all categories auto-populate from items.yml
        var materials: List<Material> = MarketActions.getMarketItemsByFilter(plugin, category.filter)
        
        // Apply search filter if set
        if (actualSearch.isNotBlank()) {
            materials = materials.filter { 
                it.name.contains(actualSearch, ignoreCase = true) ||
                MarketActions.prettyName(it).contains(actualSearch, ignoreCase = true)
            }
        }
        
        // Apply sorting
        materials = when (actualSort) {
            SortBy.NAME -> materials.sortedBy { MarketActions.prettyName(it).lowercase() }
            SortBy.PRICE -> materials.sortedByDescending { plugin.marketManager.get(it)?.currentPrice ?: 0.0 }
            SortBy.CHANGE -> materials.sortedByDescending { 
                val mi = plugin.marketManager.get(it) ?: return@sortedByDescending 0.0
                val history = mi.history.toList()
                if (history.size < 2) 0.0
                else {
                    val prev = history[history.lastIndex - 1].price
                    val curr = history.last().price
                    if (prev != 0.0) (curr - prev) / prev * 100.0 else 0.0
                }
            }
        }
        
        val itemsPerPage = manager.itemsPerPage
        val totalPages = if (materials.isEmpty()) 1 else ((materials.size - 1) / itemsPerPage + 1)
        val safePage = page.coerceIn(0, totalPages - 1)
        
        // Calculate page items
        val from = safePage * itemsPerPage
        val to = (from + itemsPerPage).coerceAtMost(materials.size)
        val pageItems = if (from < materials.size) materials.subList(from, to) else emptyList()
        
        // Create title with page indicator, sort, and search info
        val sortInfo = "[${actualSort.name}]"
        val searchInfo = if (actualSearch.isNotBlank()) " ${ChatColor.YELLOW}[${actualSearch}]" else ""
        val title = "${category.pageTitle} ${ChatColor.DARK_GRAY}$sortInfo (${safePage + 1}/$totalPages)$searchInfo"
        val inv = Bukkit.createInventory(null, category.pageSize, title)
        
        // Fill empty slots if enabled
        if (category.fillEmpty) {
            val filler = createFillerItem(category.emptyMaterial)
            for (i in 0 until category.pageSize) {
                inv.setItem(i, filler)
            }
        }
        
        // Create state now so item creation can access amount
        val state = ShopPageState.category(shopId, categoryId, safePage, actualAmountIdx, actualSearch, actualSort)
        
        // Place items - use MarketActions.createMarketItem for consistent display
        val slots = category.itemSlots.toList()
        pageItems.forEachIndexed { idx, material ->
            val slot = if (idx < slots.size) {
                slots[idx]
            } else {
                return@forEachIndexed
            }
            
            // Use MarketActions for consistent item display (same as default MarketGUI)
            val itemStack = MarketActions.createMarketItem(plugin, player, material, state.getAmount())
            inv.setItem(slot, itemStack)
        }
        
        // Navigation buttons
        val config = plugin.config
        val showBack = config.getBoolean("shop.custom.show-back-button", true)
        val showPagination = config.getBoolean("shop.custom.show-pagination", true)
        val showSearch = config.getBoolean("shop.custom.show-search-button", true)
        val backSlot = config.getInt("shop.custom.back-button-slot", 49)
        val prevSlot = config.getInt("shop.custom.prev-page-slot", 48)
        val nextSlot = config.getInt("shop.custom.next-page-slot", 50)
        val searchSlot = config.getInt("shop.custom.search-button-slot", 45)
        val sortSlot = config.getInt("shop.custom.sort-button-slot", 46)
        val holdingsSlot = config.getInt("shop.custom.holdings-button-slot", 53)
        
        // Back button
        if (showBack) {
            inv.setItem(backSlot, createNavButton(
                Material.BARRIER,
                "${ChatColor.RED}« Back to Menu",
                listOf("${ChatColor.GRAY}Return to shop categories")
            ))
        }
        
        // Search button
        if (showSearch) {
            val searchLore = if (actualSearch.isBlank()) {
                listOf(
                    "${ChatColor.GRAY}Click to search for items",
                    "",
                    "${ChatColor.YELLOW}Left-click: ${ChatColor.WHITE}Enter search",
                    "${ChatColor.DARK_GRAY}Type in chat to filter items"
                )
            } else {
                listOf(
                    "${ChatColor.GRAY}Current: ${ChatColor.AQUA}$actualSearch",
                    "${ChatColor.GRAY}Found: ${ChatColor.WHITE}${materials.size} items",
                    "",
                    "${ChatColor.YELLOW}Left-click: ${ChatColor.WHITE}New search",
                    "${ChatColor.RED}Right-click: ${ChatColor.WHITE}Clear search"
                )
            }
            inv.setItem(searchSlot, createNavButton(
                Material.COMPASS,
                "${ChatColor.AQUA}🔍 Search${if (actualSearch.isNotBlank()) ": $actualSearch" else ""}",
                searchLore
            ))
        }
        
        // Sort button
        inv.setItem(sortSlot, createNavButton(
            Material.COMPARATOR,
            "${ChatColor.LIGHT_PURPLE}Sort: ${actualSort.name} ${ChatColor.GRAY}(click)",
            listOf(
                "${ChatColor.GRAY}Current: ${ChatColor.WHITE}${actualSort.name}",
                "",
                "${ChatColor.YELLOW}Click to cycle:",
                "${ChatColor.WHITE}NAME → PRICE → CHANGE"
            )
        ))
        
        // Holdings button
        val holdingsEnabled = plugin.config.getBoolean("holdings.enabled", true)
        val db = plugin.marketManager.sqliteStore()
        
        if (holdingsEnabled && db != null) {
            val holdings = db.listHoldings(player.uniqueId.toString())
            val totalCount = holdings.values.sumOf { it.first }
            val maxHoldings = plugin.config.getInt("holdings.max-total-per-player", 100000)
            
            val holdingsLore = mutableListOf<String>()
            if (totalCount > 0) {
                holdingsLore += "${ChatColor.GOLD}$totalCount ${ChatColor.YELLOW}/ $maxHoldings items"
                holdingsLore += "${ChatColor.GRAY}${holdings.size} different materials"
                holdingsLore += "${ChatColor.DARK_GRAY}Click to view and withdraw"
            } else {
                holdingsLore += "${ChatColor.GRAY}No items in holdings"
                holdingsLore += "${ChatColor.DARK_GRAY}Buy items to add them here"
            }
            
            inv.setItem(holdingsSlot, createNavButton(
                Material.CHEST,
                "${ChatColor.LIGHT_PURPLE}My Holdings ${ChatColor.GRAY}(click)",
                holdingsLore
            ))
        }
        
        // Pagination
        if (showPagination && totalPages > 1) {
            if (safePage > 0) {
                inv.setItem(prevSlot, createNavButton(
                    Material.ARROW,
                    "${ChatColor.YELLOW}« Previous Page",
                    listOf("${ChatColor.GRAY}Go to page ${safePage}")
                ))
            }
            if (safePage < totalPages - 1) {
                inv.setItem(nextSlot, createNavButton(
                    Material.ARROW,
                    "${ChatColor.YELLOW}Next Page »",
                    listOf("${ChatColor.GRAY}Go to page ${safePage + 2}")
                ))
            }
        }
        
        // Track state BEFORE opening inventory (critical for event handling)
        playerStates[player.uniqueId] = state
        viewingShop[player.uniqueId] = shopId
        openGuis[player.uniqueId] = GuiType.CATEGORY
        
        // Play sound
        playSound(player, shop.sounds.openCategory)
        
        player.openInventory(inv)
    }
    
    /**
     * Create a category icon ItemStack.
     * Shows item count from market based on filter.
     */
    private fun createCategoryIcon(category: ShopCategory): ItemStack {
        val item = ItemStack(category.icon)
        val meta = item.itemMeta ?: return item
        meta.setDisplayName(category.iconName)
        
        // Get item count based on filter (all categories use market items)
        val itemCount = MarketActions.getMarketItemsByFilter(plugin, category.filter).size
        
        val lore = mutableListOf<String>()
        lore.addAll(category.iconLore)
        lore.add("")
        lore.add("${ChatColor.GRAY}$itemCount items")
        lore.add("${ChatColor.YELLOW}Click to browse")
        meta.lore = lore
        
        item.itemMeta = meta
        return item
    }
    
    /**
     * Create a shop item with price info in lore.
     * Uses MarketActions for IDENTICAL lore to default MarketGUI.
     * All items are sourced from items.yml via market manager.
     */
    private fun createShopItem(player: Player, material: Material, state: ShopPageState): ItemStack {
        val currentAmount = state.getAmount()
        
        // Use MarketActions for consistent display (same as default MarketGUI)
        return MarketActions.createMarketItem(plugin, player, material, currentAmount)
    }
    
    /**
     * Get buy and sell prices for a material.
     * Prices always come from items.yml via marketManager.
     */
    private fun getItemPrices(material: Material): Pair<Double, Double> {
        // Get market price from items.yml via marketManager
        val marketItem = plugin.marketManager.get(material)
        if (marketItem == null) {
            // Not in market - return 0
            return 0.0 to 0.0
        }
        
        val basePrice = marketItem.currentPrice
        val eventMultiplier = plugin.eventManager.multiplierFor(material)
        val effectivePrice = basePrice * eventMultiplier
        
        // Apply spread
        val spreadEnabled = plugin.config.getBoolean("spread.enabled", true)
        val buyMarkup = if (spreadEnabled) plugin.config.getDouble("spread.buy-markup-percent", 1.5) / 100.0 else 0.0
        val sellMarkdown = if (spreadEnabled) plugin.config.getDouble("spread.sell-markdown-percent", 1.5) / 100.0 else 0.0
        
        val buyPrice = effectivePrice * (1 + buyMarkup)
        val sellPrice = effectivePrice * (1 - sellMarkdown)
        
        return buyPrice to sellPrice
    }
    
    /**
     * Create a navigation button.
     */
    private fun createNavButton(material: Material, name: String, lore: List<String>): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta ?: return item
        meta.setDisplayName(name)
        meta.lore = lore
        item.itemMeta = meta
        return item
    }
    
    /**
     * Create a filler/decoration item.
     */
    private fun createFillerItem(material: Material): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta ?: return item
        meta.setDisplayName(" ")
        item.itemMeta = meta
        return item
    }
    
    /**
     * Create a decoration item.
     */
    private fun createDecorItem(material: Material, name: String?, lore: List<String>?): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta ?: return item
        meta.setDisplayName(name ?: " ")
        if (lore != null) meta.lore = lore
        item.itemMeta = meta
        return item
    }
    
    /**
     * Create a close button.
     */
    private fun createCloseButton(): ItemStack {
        return createNavButton(
            Material.BARRIER,
            "${ChatColor.RED}Close",
            listOf("${ChatColor.GRAY}Close this menu")
        )
    }
    
    /**
     * Create an info display item.
     */
    private fun createInfoItem(player: Player, name: String?, lore: List<String>?): ItemStack {
        val bal = plugin.economy?.getBalance(player) ?: 0.0
        val actualName = name?.replace("%balance%", formatPrice(bal)) ?: "${ChatColor.GOLD}Balance: ${formatPrice(bal)}"
        val actualLore = lore?.map { it.replace("%balance%", formatPrice(bal)) } ?: emptyList()
        
        return createDecorItem(Material.GOLD_INGOT, actualName, actualLore)
    }
    
    /**
     * Handle inventory click events.
     * Uses the same logic as default MarketGUI for consistent behavior.
     */
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        
        // Check if this player has our GUI open using UUID tracking (reliable across MC versions)
        val guiType = openGuis[player.uniqueId]
        if (guiType == null || guiType == GuiType.NONE) return
        
        // Cancel the event FIRST to prevent item taking/moving in all cases
        event.isCancelled = true
        
        // Only block if clicking in player's bottom inventory (not the GUI)
        if (event.rawSlot >= event.view.topInventory.size) {
            return
        }
        
        // Block all item movement actions EXCEPT shift-left-click (for details view)
        // This matches the default MarketGUI behavior exactly
        if (event.click == ClickType.SHIFT_RIGHT ||
            event.click == ClickType.NUMBER_KEY || event.click == ClickType.DOUBLE_CLICK ||
            event.click == ClickType.DROP || event.click == ClickType.CONTROL_DROP) {
            return
        }
        
        val state = playerStates[player.uniqueId] ?: return
        
        val clickedItem = event.currentItem ?: return
        if (clickedItem.type == Material.AIR) return
        
        // Handle based on current GUI type
        when (guiType) {
            GuiType.MAIN_MENU -> handleMainMenuClick(player, state, event.rawSlot, clickedItem, event.click)
            GuiType.CATEGORY -> handleCategoryClick(player, state, event.rawSlot, clickedItem, event.click)
            GuiType.DETAILS -> handleDetailsClick(player, state, event.rawSlot)
            GuiType.NONE -> { /* Do nothing */ }
        }
    }
    
    /**
     * Handle main menu click.
     */
    private fun handleMainMenuClick(player: Player, state: ShopPageState, slot: Int, item: ItemStack, click: ClickType) {
        val manager = shopManager() ?: return
        val shop = manager.get(state.shopId) ?: return
        
        // Check if this slot is a category in the layout
        val layoutSlot = shop.menuLayout.find { it.slot == slot }
        
        if (layoutSlot != null) {
            when (layoutSlot.type) {
                MenuSlotType.CATEGORY -> {
                    val categoryId = layoutSlot.categoryId ?: return
                    openCategory(player, state.shopId, categoryId, 0)
                }
                MenuSlotType.CLOSE -> {
                    player.closeInventory()
                }
                else -> { /* Decoration, do nothing */ }
            }
        } else {
            // Auto-layout mode - check if clicked on a category icon
            val categoryByIcon = shop.categories.values.find { it.icon == item.type }
            if (categoryByIcon != null) {
                openCategory(player, state.shopId, categoryByIcon.id, 0)
            }
        }
    }
    
    /**
     * Handle category page click.
     * Uses MarketActions for buy/sell/details - IDENTICAL to default MarketGUI.
     */
    private fun handleCategoryClick(player: Player, state: ShopPageState, slot: Int, item: ItemStack, click: ClickType) {
        val manager = shopManager() ?: return
        val shop = manager.get(state.shopId) ?: return
        val category = shop.categories[state.categoryId] ?: return
        
        val config = plugin.config
        val backSlot = config.getInt("shop.custom.back-button-slot", 49)
        val prevSlot = config.getInt("shop.custom.prev-page-slot", 48)
        val nextSlot = config.getInt("shop.custom.next-page-slot", 50)
        val searchSlot = config.getInt("shop.custom.search-button-slot", 45)
        val sortSlot = config.getInt("shop.custom.sort-button-slot", 46)
        val holdingsSlot = config.getInt("shop.custom.holdings-button-slot", 53)
        val clearSearchSlot = config.getInt("shop.custom.clear-search-slot", 53)
        
        // Get current items count from market filter for pagination
        val itemCount = MarketActions.getMarketItemsByFilter(plugin, category.filter).size
        
        // Navigation handling
        when (slot) {
            backSlot -> {
                openMainMenu(player, state.shopId)
                return
            }
            prevSlot -> {
                if (state.page > 0) {
                    openCategory(player, state.shopId, state.categoryId!!, state.page - 1, state.amountIdx, state.search, state.sort)
                    playSound(player, shop.sounds.pageChange)
                }
                return
            }
            nextSlot -> {
                val totalPages = ((itemCount - 1) / manager.itemsPerPage + 1).coerceAtLeast(1)
                if (state.page < totalPages - 1) {
                    openCategory(player, state.shopId, state.categoryId!!, state.page + 1, state.amountIdx, state.search, state.sort)
                    playSound(player, shop.sounds.pageChange)
                }
                return
            }
            searchSlot -> {
                if (click == ClickType.RIGHT || click.isRightClick) {
                    // Right-click: Clear search (if there is an active search)
                    if (state.search.isNotEmpty()) {
                        openCategory(player, state.shopId, state.categoryId!!, 0, state.amountIdx, "", state.sort)
                        player.sendMessage("${ChatColor.YELLOW}Search cleared.")
                        playSound(player, Sound.UI_BUTTON_CLICK.name)
                    }
                } else {
                    // Left-click: Open search input
                    awaitingSearchInput.add(player.uniqueId)
                    player.closeInventory()
                    player.sendMessage("${ChatColor.GREEN}✎ ${ChatColor.YELLOW}Type your search query in chat, or type ${ChatColor.RED}cancel ${ChatColor.YELLOW}to cancel.")
                    playSound(player, Sound.UI_BUTTON_CLICK.name)
                }
                return
            }
            sortSlot -> {
                // Cycle sort order
                val newSort = when (state.sort) {
                    SortBy.NAME -> SortBy.PRICE
                    SortBy.PRICE -> SortBy.CHANGE
                    SortBy.CHANGE -> SortBy.NAME
                }
                openCategory(player, state.shopId, state.categoryId!!, 0, state.amountIdx, state.search, newSort)
                playSound(player, Sound.UI_BUTTON_CLICK.name)
                return
            }
            holdingsSlot -> {
                // Open holdings via MarketGUI (same as default GUI)
                plugin.marketGUI.openHoldings(player)
                return
            }
        }
        
        // Get the material from the clicked item
        val mat = item.type
        if (mat == Material.AIR) return
        
        // CRITICAL: Only handle items that exist in the market
        // This matches the default MarketGUI behavior exactly
        if (!MarketActions.isInMarket(plugin, mat)) {
            return
        }
        
        val amount = state.getAmount()
        
        // Handle click actions - Use our custom details view for shift-click
        when {
            click == ClickType.SHIFT_LEFT || click == ClickType.MIDDLE -> {
                // Open our custom details view (NOT MarketGUI.openDetails)
                // This ensures back button returns to our custom shop
                openDetails(player, state.shopId, state.categoryId!!, mat, state.page, state.search, state.sort)
            }
            click == ClickType.LEFT || click.isLeftClick -> {
                // Buy - use MarketActions
                MarketActions.buy(plugin, player, mat, amount) {
                    openCategory(player, state.shopId, state.categoryId!!, state.page, state.amountIdx, state.search, state.sort)
                }
            }
            click == ClickType.RIGHT || click.isRightClick -> {
                // Sell - use MarketActions
                MarketActions.sell(plugin, player, mat, amount) {
                    openCategory(player, state.shopId, state.categoryId!!, state.page, state.amountIdx, state.search, state.sort)
                }
            }
        }
    }
    
    /**
     * Open item details view - Custom implementation with correct back navigation.
     * Matches the EXACT SAME layout as default MarketGUI.openDetails().
     * When back button is clicked, returns to our CustomShopGUI category view.
     */
    fun openDetails(player: Player, shopId: String, categoryId: String, material: Material, page: Int = 0, search: String = "", sort: SortBy = SortBy.NAME) {
        val manager = shopManager() ?: return
        val shop = manager.get(shopId) ?: return
        val category = shop.categories[categoryId] ?: return
        
        // Get market data for this item
        val mi = plugin.marketManager.get(material) ?: return
        
        // Create inventory - SAME as MarketGUI (3 rows = 27 slots)
        val inv = Bukkit.createInventory(null, 27, "${ChatColor.DARK_PURPLE}${shop.menuTitle}: ${ChatColor.AQUA}${prettyName(material)}")
        
        // Calculate price info - SAME as MarketGUI
        val mul = plugin.eventManager.multiplierFor(material)
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
        
        // Calculate buy/sell prices with spread - SAME as MarketGUI
        val spreadEnabled = plugin.config.getBoolean("spread.enabled", true)
        val buyMarkupPct = if (spreadEnabled) plugin.config.getDouble("spread.buy-markup-percent", 1.5).coerceAtLeast(0.0) else 0.0
        val sellMarkdownPct = if (spreadEnabled) plugin.config.getDouble("spread.sell-markdown-percent", 1.5).coerceAtLeast(0.0) else 0.0
        val effectivePrice = current * mul
        val buyPrice = effectivePrice * (1.0 + buyMarkupPct / 100.0)
        val sellPrice = effectivePrice * (1.0 - sellMarkdownPct / 100.0)
        
        val bal = plugin.economy?.getBalance(player) ?: 0.0
        val invCount = player.inventory.contents.filterNotNull().filter { it.type == material }.sumOf { it.amount }
        
        // Item display - SAME slot and lore as MarketGUI (slot 13)
        val displayItem = ItemStack(material).apply {
            itemMeta = itemMeta?.apply {
                setDisplayName("${ChatColor.AQUA}${prettyName(material)}")
                val loreList = mutableListOf<String>()
                loreList += "${ChatColor.GRAY}Price: ${ChatColor.GREEN}${format(current)} ${ChatColor.GRAY}(${arrow} ${format(diff)}, ${formatPct(pct)})"
                if (spreadEnabled && (buyMarkupPct > 0 || sellMarkdownPct > 0)) {
                    loreList += "${ChatColor.GREEN}Buy: ${format(buyPrice)} ${ChatColor.GRAY}| ${ChatColor.YELLOW}Sell: ${format(sellPrice)}"
                }
                if (mul != 1.0) loreList += "${ChatColor.DARK_AQUA}Event: x${format(mul)} ${ChatColor.GRAY}Eff: ${ChatColor.GREEN}${format(current*mul)}"
                loreList += "${ChatColor.DARK_GRAY}Min ${format(mi.minPrice)}  Max ${format(mi.maxPrice)}"
                
                // Chart - SAME as MarketGUI
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
                        loreList += "${ChatColor.DARK_GRAY}Chart: ${ChatColor.GRAY}${bars}"
                    }
                }
                
                loreList += "${ChatColor.GRAY}You have: ${ChatColor.AQUA}$invCount"
                loreList += "${ChatColor.GRAY}Balance: ${ChatColor.GOLD}${format(bal)}"
                lore = loreList
            }
        }
        inv.setItem(13, displayItem)  // SAME slot as MarketGUI
        
        // Buttons - SAME slots and materials as MarketGUI
        inv.setItem(18, namedItem(Material.LIME_DYE, "${ChatColor.GREEN}Buy 1"))
        inv.setItem(20, namedItem(Material.EMERALD_BLOCK, "${ChatColor.GREEN}Buy 64"))
        inv.setItem(24, namedItem(Material.RED_DYE, "${ChatColor.RED}Sell 1"))
        inv.setItem(26, namedItem(Material.BARREL, "${ChatColor.RED}Sell All ($invCount)"))
        inv.setItem(22, namedItem(Material.ARROW, "${ChatColor.YELLOW}Back"))  // Back goes to custom shop, not default market
        
        // Update state
        val state = ShopPageState.detailsView(shopId, categoryId, page, search, material, sort)
        playerStates[player.uniqueId] = state
        openGuis[player.uniqueId] = GuiType.DETAILS
        viewingShop[player.uniqueId] = shopId
        
        player.openInventory(inv)
        playSound(player, shop.sounds.openCategory)
    }
    
    /**
     * Create a named item (helper, same as MarketGUI).
     */
    private fun namedItem(mat: Material, name: String): ItemStack {
        val item = ItemStack(mat)
        val meta = item.itemMeta
        meta?.setDisplayName(name)
        item.itemMeta = meta
        return item
    }
    
    /**
     * Handle details view click.
     * Uses SAME slot layout as MarketGUI.onDetailsClick().
     */
    private fun handleDetailsClick(player: Player, state: ShopPageState, slot: Int) {
        val material = state.detailOf ?: return
        
        when (slot) {
            18 -> { // Buy 1 - SAME as MarketGUI
                MarketActions.buy(plugin, player, material, 1) {
                    openDetails(player, state.shopId, state.categoryId!!, material, state.page, state.search, state.sort)
                }
            }
            20 -> { // Buy 64 - SAME as MarketGUI
                MarketActions.buy(plugin, player, material, 64) {
                    openDetails(player, state.shopId, state.categoryId!!, material, state.page, state.search, state.sort)
                }
            }
            24 -> { // Sell 1 - SAME as MarketGUI
                MarketActions.sell(plugin, player, material, 1) {
                    openDetails(player, state.shopId, state.categoryId!!, material, state.page, state.search, state.sort)
                }
            }
            26 -> { // Sell All - SAME as MarketGUI
                MarketActions.sellAll(plugin, player, material) {
                    openDetails(player, state.shopId, state.categoryId!!, material, state.page, state.search, state.sort)
                }
            }
            22 -> { // Back button - Returns to CustomShopGUI category (NOT MarketGUI!)
                openCategory(player, state.shopId, state.categoryId!!, state.page, state.amountIdx, state.search, state.sort)
            }
        }
    }
    
    /**
     * Handle chat input for search functionality.
     */
    @EventHandler
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        
        // Check if this player is awaiting search input
        if (!awaitingSearchInput.contains(player.uniqueId)) return
        
        // Cancel the chat message (don't broadcast search query)
        event.isCancelled = true
        awaitingSearchInput.remove(player.uniqueId)
        
        val message = event.message.trim()
        
        // Check for cancel
        if (message.equals("cancel", ignoreCase = true)) {
            // Get previous state and reopen category
            val state = playerStates[player.uniqueId]
            if (state != null && state.categoryId != null) {
                // Run on main thread (chat event is async)
                Bukkit.getScheduler().runTask(plugin, Runnable {
                    player.sendMessage("${ChatColor.YELLOW}Search cancelled.")
                    openCategory(player, state.shopId, state.categoryId!!, state.page, state.amountIdx, state.search, state.sort)
                })
            }
            return
        }
        
        // Apply search filter
        val state = playerStates[player.uniqueId]
        if (state != null && state.categoryId != null) {
            // Run on main thread (chat event is async)
            Bukkit.getScheduler().runTask(plugin, Runnable {
                player.sendMessage("${ChatColor.GREEN}✔ ${ChatColor.GRAY}Searching for: ${ChatColor.WHITE}$message")
                // Reset to page 0 when applying new search
                openCategory(player, state.shopId, state.categoryId!!, 0, state.amountIdx, message, state.sort)
            })
        }
    }
    
    /**
     * Handle inventory drag events.
     */
    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        val player = event.whoClicked as? Player ?: return
        
        // Check if this player has our GUI open using UUID tracking
        val guiType = openGuis[player.uniqueId]
        if (guiType == null || guiType == GuiType.NONE) return
        
        // Cancel all drag events in our GUI
        event.isCancelled = true
    }
    
    /**
     * Handle inventory close.
     */
    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val player = event.player as? Player ?: return
        
        // We need to handle this carefully:
        // When switching between our GUIs (main menu -> category), the close event fires 
        // AFTER we've already set the new GUI type in openCategory/openMainMenu.
        // So we check on next tick if the player still has a GUI open with us.
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            // If player's inventory is no longer our custom inventory, clean up
            val guiType = openGuis[player.uniqueId]
            if (guiType != null) {
                // Check if player's current open inventory matches our expected state
                // If they have no inventory open or it's not ours, clean up
                val topInv = player.openInventory.topInventory
                if (topInv.size == 0) {
                    // No GUI open - clean up
                    openGuis.remove(player.uniqueId)
                    playerStates.remove(player.uniqueId)
                    viewingShop.remove(player.uniqueId)
                }
            }
        }, 1L)
    }
    
    /**
     * Play a sound effect.
     */
    private fun playSound(player: Player, soundName: String?) {
        if (soundName.isNullOrBlank()) return
        
        try {
            val sound = Sound.valueOf(soundName.uppercase())
            player.playSound(player.location, sound, 1.0f, 1.0f)
        } catch (e: IllegalArgumentException) {
            // Invalid sound name, ignore
        }
    }
    
    /**
     * Format a number for display (2 decimal places).
     */
    private fun format(n: Double): String = String.format("%.2f", n)
    
    /**
     * Format a percentage for display.
     */
    private fun formatPct(n: Double): String = String.format("%.2f%%", n)
    
    /**
     * Format a price for display with currency symbol.
     */
    private fun formatPrice(price: Double): String = String.format("$%.2f", price)
    
    /**
     * Get stock level as string.
     */
    private fun getStock(material: Material): String {
        val item = plugin.marketManager.get(material) ?: return "N/A"
        return String.format("%.0f", item.supply)
    }
    
    /**
     * Get demand level as string.
     */
    private fun getDemand(material: Material): String {
        val item = plugin.marketManager.get(material) ?: return "N/A"
        return String.format("%.0f", item.demand)
    }
    
    /**
     * Pretty format material name.
     */
    private fun prettyName(material: Material): String {
        return material.name.lowercase()
            .split('_')
            .joinToString(" ") { it.replaceFirstChar { c -> c.titlecase() } }
    }
    
    /**
     * Refresh GUI for a player if viewing our shop.
     */
    fun refreshOpenFor(player: Player) {
        val state = playerStates[player.uniqueId] ?: return
        
        Bukkit.getScheduler().runTask(plugin, Runnable {
            if (state.isMainMenu()) {
                openMainMenu(player, state.shopId)
            } else {
                openCategory(player, state.shopId, state.categoryId!!, state.page)
            }
        })
    }
    
    /**
     * Refresh all viewers of custom shop.
     */
    fun refreshAllOpen() {
        plugin.server.onlinePlayers.forEach { p ->
            val guiType = openGuis[p.uniqueId]
            if (guiType != null && guiType != GuiType.NONE) {
                refreshOpenFor(p)
            }
        }
    }
}
