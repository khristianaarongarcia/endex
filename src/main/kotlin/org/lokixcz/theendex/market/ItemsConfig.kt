package org.lokixcz.theendex.market

import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

/**
 * Static item configuration - defines which items exist in the market
 * and their pricing rules (base, min, max prices).
 * 
 * This is stored in items.yml and can be edited by server owners.
 * Dynamic data (current price, supply/demand) is stored in market.db.
 */
data class ItemConfig(
    val material: Material,
    var basePrice: Double,
    var minPrice: Double,
    var maxPrice: Double,
    var enabled: Boolean = true
) {
    companion object {
        /**
         * Create an ItemConfig with sensible defaults based on material type.
         */
        fun createDefault(material: Material, basePrice: Double): ItemConfig {
            val min = (basePrice * 0.30).coerceAtLeast(1.0)
            val max = (basePrice * 6.0).coerceAtLeast(min + 1.0)
            return ItemConfig(material, basePrice, min, max, true)
        }
    }
}

/**
 * Manager for items.yml - the static item configuration file.
 * 
 * Responsibilities:
 * - Load/save items.yml
 * - Provide item configs to MarketManager
 * - Sync with admin commands (add, remove, setprice)
 * - Auto-export from existing market.db on first run
 */
class ItemsConfigManager(private val plugin: JavaPlugin) {
    
    private val items: MutableMap<Material, ItemConfig> = mutableMapOf()
    private val itemsFile: File get() = File(plugin.dataFolder, "items.yml")
    
    /**
     * Get all configured items.
     */
    fun all(): Collection<ItemConfig> = items.values
    
    /**
     * Get all enabled items.
     */
    fun allEnabled(): Collection<ItemConfig> = items.values.filter { it.enabled }
    
    /**
     * Get config for a specific material.
     */
    fun get(material: Material): ItemConfig? = items[material]
    
    /**
     * Check if an item is configured and enabled.
     */
    fun isEnabled(material: Material): Boolean = items[material]?.enabled ?: false
    
    /**
     * Add or update an item configuration.
     * Returns true if item was added, false if updated.
     */
    fun set(config: ItemConfig): Boolean {
        val isNew = !items.containsKey(config.material)
        items[config.material] = config
        return isNew
    }
    
    /**
     * Add a new item with the given parameters.
     */
    fun addItem(material: Material, basePrice: Double, minPrice: Double? = null, maxPrice: Double? = null): ItemConfig {
        val min = minPrice ?: (basePrice * 0.30).coerceAtLeast(1.0)
        val max = maxPrice ?: (basePrice * 6.0).coerceAtLeast(min + 1.0)
        val config = ItemConfig(material, basePrice, min, max, true)
        items[material] = config
        return config
    }
    
    /**
     * Remove an item from the configuration.
     */
    fun remove(material: Material): Boolean {
        return items.remove(material) != null
    }
    
    /**
     * Disable an item (keeps config but won't appear in market).
     */
    fun disable(material: Material): Boolean {
        val config = items[material] ?: return false
        config.enabled = false
        return true
    }
    
    /**
     * Enable a previously disabled item.
     */
    fun enable(material: Material): Boolean {
        val config = items[material] ?: return false
        config.enabled = true
        return true
    }
    
    /**
     * Update base price for an item.
     */
    fun setBasePrice(material: Material, price: Double): Boolean {
        val config = items[material] ?: return false
        config.basePrice = price
        return true
    }
    
    /**
     * Update min price for an item.
     */
    fun setMinPrice(material: Material, price: Double): Boolean {
        val config = items[material] ?: return false
        config.minPrice = price.coerceAtMost(config.maxPrice - 0.01)
        return true
    }
    
    /**
     * Update max price for an item.
     */
    fun setMaxPrice(material: Material, price: Double): Boolean {
        val config = items[material] ?: return false
        config.maxPrice = price.coerceAtLeast(config.minPrice + 0.01)
        return true
    }
    
    /**
     * Load items from items.yml.
     * If file doesn't exist, returns false (caller should seed defaults).
     */
    fun load(): Boolean {
        items.clear()
        
        if (!itemsFile.exists()) {
            return false
        }
        
        val yaml = YamlConfiguration.loadConfiguration(itemsFile)
        val itemsSection = yaml.getConfigurationSection("items") ?: return false
        
        for (key in itemsSection.getKeys(false)) {
            val mat = Material.matchMaterial(key.uppercase()) ?: continue
            val section = itemsSection.getConfigurationSection(key) ?: continue
            
            val basePrice = section.getDouble("base-price", 100.0)
            val minPrice = section.getDouble("min-price", basePrice * 0.3)
            val maxPrice = section.getDouble("max-price", basePrice * 6.0)
            val enabled = section.getBoolean("enabled", true)
            
            items[mat] = ItemConfig(mat, basePrice, minPrice, maxPrice, enabled)
        }
        
        plugin.logger.info("Loaded ${items.size} items from items.yml")
        return true
    }
    
    /**
     * Save all items to items.yml.
     */
    fun save() {
        if (!plugin.dataFolder.exists()) plugin.dataFolder.mkdirs()
        
        val yaml = YamlConfiguration()
        
        // Add header comment
        yaml.options().setHeader(listOf(
            "═══════════════════════════════════════════════════════════════════════════════",
            "The Endex - Market Items Configuration",
            "═══════════════════════════════════════════════════════════════════════════════",
            "",
            "This file defines which items are tradeable in the market and their pricing rules.",
            "",
            "For each item:",
            "  base-price: The 'natural' price that the market gravitates toward",
            "  min-price: Floor price - items cannot go below this",
            "  max-price: Ceiling price - items cannot exceed this",
            "  enabled: Whether the item appears in the market (true/false)",
            "",
            "Dynamic data (current price, supply/demand) is stored in market.db",
            "",
            "Admin commands:",
            "  /market add <item> <base> [min] [max] - Add item to market",
            "  /market remove <item> - Remove item from market",
            "  /market setbase <item> <price> - Set base price",
            "  /market setmin <item> <price> - Set minimum price",
            "  /market setmax <item> <price> - Set maximum price",
            "  /market enable <item> - Enable a disabled item",
            "  /market disable <item> - Disable an item (keeps config)",
            "",
            "═══════════════════════════════════════════════════════════════════════════════"
        ))
        
        // Sort items alphabetically for easier editing
        val sortedItems = items.entries.sortedBy { it.key.name }
        
        for ((mat, config) in sortedItems) {
            val path = "items.${mat.name}"
            yaml.set("$path.base-price", config.basePrice)
            yaml.set("$path.min-price", config.minPrice)
            yaml.set("$path.max-price", config.maxPrice)
            yaml.set("$path.enabled", config.enabled)
        }
        
        yaml.save(itemsFile)
        plugin.logger.info("Saved ${items.size} items to items.yml")
    }
    
    /**
     * Import items from an existing MarketManager (for migration from market.db).
     * Only imports items that don't already exist in items.yml.
     */
    fun importFromMarketManager(marketManager: MarketManager): Int {
        var imported = 0
        
        for (item in marketManager.allItems()) {
            if (!items.containsKey(item.material)) {
                items[item.material] = ItemConfig(
                    material = item.material,
                    basePrice = item.basePrice,
                    minPrice = item.minPrice,
                    maxPrice = item.maxPrice,
                    enabled = true
                )
                imported++
            }
        }
        
        if (imported > 0) {
            plugin.logger.info("Imported $imported items from market data to items.yml")
        }
        
        return imported
    }
    
    /**
     * Sync item configs to MarketManager.
     * Updates base/min/max prices in market.db to match items.yml.
     * Adds new items to market.db, removes items not in items.yml.
     */
    fun syncToMarketManager(marketManager: MarketManager, db: SqliteStore?): SyncResult {
        var added = 0
        var updated = 0
        var removed = 0
        
        val enabledItems = allEnabled()
        val existingMaterials = marketManager.allItems().map { it.material }.toSet()
        val configuredMaterials = enabledItems.map { it.material }.toSet()
        
        // Add or update items from config
        for (config in enabledItems) {
            val existing = marketManager.get(config.material)
            
            if (existing == null) {
                // New item - add to market
                val newItem = MarketItem(
                    material = config.material,
                    basePrice = config.basePrice,
                    minPrice = config.minPrice,
                    maxPrice = config.maxPrice,
                    currentPrice = config.basePrice,  // Start at base price
                    demand = 0.0,
                    supply = 0.0,
                    history = ArrayDeque()
                )
                // Add to internal market map via reflection or direct db insert
                db?.upsertItem(newItem)
                added++
            } else {
                // Existing item - update pricing rules if changed
                var changed = false
                if (existing.basePrice != config.basePrice) {
                    existing.basePrice = config.basePrice
                    changed = true
                }
                if (existing.minPrice != config.minPrice) {
                    existing.minPrice = config.minPrice
                    changed = true
                }
                if (existing.maxPrice != config.maxPrice) {
                    existing.maxPrice = config.maxPrice
                    changed = true
                }
                
                if (changed) {
                    // Ensure current price is within new bounds
                    existing.currentPrice = existing.currentPrice.coerceIn(config.minPrice, config.maxPrice)
                    db?.upsertItem(existing)
                    updated++
                }
            }
        }
        
        // Note: We don't remove items from market.db automatically
        // This preserves historical data. Items just won't appear in GUI.
        
        return SyncResult(added, updated, removed)
    }
    
    /**
     * Get the count of configured items.
     */
    fun count(): Int = items.size
    
    /**
     * Get the count of enabled items.
     */
    fun enabledCount(): Int = items.values.count { it.enabled }
    
    data class SyncResult(val added: Int, val updated: Int, val removed: Int)
}
