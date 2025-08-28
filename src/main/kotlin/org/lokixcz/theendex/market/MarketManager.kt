package org.lokixcz.theendex.market

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileWriter
import java.time.Instant
import java.util.concurrent.atomic.AtomicBoolean

class MarketManager(private val plugin: JavaPlugin, private val db: SqliteStore? = null) {

    private val items: MutableMap<Material, MarketItem> = mutableMapOf()
    private val saving = AtomicBoolean(false)

    private fun dataFolder(): File = File(plugin.dataFolder, "market.yml")
    private fun backupFile(): File = File(plugin.dataFolder, "market_backup.yml")
    private fun historyDir(): File = File(plugin.dataFolder, plugin.config.getString("history-export.folder", "history")!!)

    fun allItems(): Collection<MarketItem> = items.values

    fun get(material: Material): MarketItem? = items[material]

    fun addDemand(material: Material, amount: Double) {
        items[material]?.let { it.demand += amount }
    }

    fun addSupply(material: Material, amount: Double) {
        items[material]?.let { it.supply += amount }
    }

    fun load() {
        if (!plugin.dataFolder.exists()) plugin.dataFolder.mkdirs()

        items.clear()
        if (db != null) {
            db.init()
            items.putAll(db.loadAll())
            applyBlacklist()
            if (items.isEmpty()) {
                // Try migrating from YAML if present
                val yamlFile = dataFolder()
                if (yamlFile.exists()) {
                    loadFromYaml(yamlFile)
                    applyBlacklist()
                    // Persist into DB
                    for ((_, item) in items) {
                        db.upsertItem(item)
                        // persist history
                        for (p in item.history) db.appendHistory(item.material, p)
                    }
                    // Move old YAML to history folder as a backup snapshot
                    try {
                        if (!historyDir().exists()) historyDir().mkdirs()
                        val ts = java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").withZone(java.time.ZoneId.systemDefault()).format(java.time.Instant.now())
                        val dest = File(historyDir(), "market_yaml_${ts}.yml")
                        yamlFile.copyTo(dest, overwrite = true)
                        yamlFile.delete()
                    } catch (t: Throwable) {
                        plugin.logger.warning("Failed to archive old market.yml: ${t.message}")
                    }
                } else {
                    // No prior data, seed a fresh market
                    seedDefaults()
                    save()
                }
            }
            return
        }

        val file = dataFolder()
        if (!file.exists()) {
            // Seed with a small default market based on config defaults
            seedDefaults()
            save()
            return
        }
        val yaml = YamlConfiguration.loadConfiguration(file)

        for (key in yaml.getKeys(false)) {
            val mat = Material.matchMaterial(key) ?: continue
            val section = yaml.getConfigurationSection(key) ?: continue
            val base = section.getDouble("base_price", 100.0)
            val min = section.getDouble("min_price", 20.0)
            val max = section.getDouble("max_price", 500.0)
            val current = section.getDouble("current_price", base)
            val demand = section.getDouble("demand", 0.0)
            val supply = section.getDouble("supply", 0.0)

            val item = MarketItem(
                material = mat,
                basePrice = base,
                minPrice = min,
                maxPrice = max,
                currentPrice = current,
                demand = demand,
                supply = supply,
                history = ArrayDeque()
            )

            val histList = section.getMapList("history")
            for (entry in histList) {
                val timeStr = entry["time"]?.toString()
                val priceNum = entry["price"]?.toString()?.toDoubleOrNull()
                if (timeStr != null && priceNum != null) {
                    runCatching { Instant.parse(timeStr) }.getOrNull()?.let {
                        item.history.addLast(PricePoint(it, priceNum))
                    }
                }
            }

            items[mat] = item
        }
        applyBlacklist()
    }

    private fun loadFromYaml(file: File) {
        val yaml = YamlConfiguration.loadConfiguration(file)
        for (key in yaml.getKeys(false)) {
            val mat = Material.matchMaterial(key) ?: continue
            val section = yaml.getConfigurationSection(key) ?: continue
            val base = section.getDouble("base_price", 100.0)
            val min = section.getDouble("min_price", 20.0)
            val max = section.getDouble("max_price", 500.0)
            val current = section.getDouble("current_price", base)
            val demand = section.getDouble("demand", 0.0)
            val supply = section.getDouble("supply", 0.0)

            val item = MarketItem(
                material = mat,
                basePrice = base,
                minPrice = min,
                maxPrice = max,
                currentPrice = current,
                demand = demand,
                supply = supply,
                history = ArrayDeque()
            )

            val histList = section.getMapList("history")
            for (entry in histList) {
                val timeStr = entry["time"]?.toString()
                val priceNum = entry["price"]?.toString()?.toDoubleOrNull()
                if (timeStr != null && priceNum != null) {
                    runCatching { Instant.parse(timeStr) }.getOrNull()?.let {
                        item.history.addLast(PricePoint(it, priceNum))
                    }
                }
            }

            items[mat] = item
        }
    }

    fun save() {
        if (!plugin.dataFolder.exists()) plugin.dataFolder.mkdirs()
        if (db != null) {
            for ((_, item) in items) {
                db.upsertItem(item)
            }
            return
        }
        val file = dataFolder()
        val yaml = YamlConfiguration()

        for ((mat, item) in items) {
            val path = mat.name
            yaml.set("$path.base_price", item.basePrice)
            yaml.set("$path.min_price", item.minPrice)
            yaml.set("$path.max_price", item.maxPrice)
            yaml.set("$path.current_price", item.currentPrice)
            yaml.set("$path.demand", item.demand)
            yaml.set("$path.supply", item.supply)

            val hist = item.history.map { mapOf("time" to it.time.toString(), "price" to it.price) }
            yaml.set("$path.history", hist)
        }

        yaml.save(file)
    }

    fun saveAsync() {
        if (db != null) {
            // For SQLite, just call save() on the main thread (fast upserts) or keep it simple
            save(); return
        }
        if (!saving.compareAndSet(false, true)) return
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            try { save() } catch (t: Throwable) { plugin.logger.warning("Async save failed: ${t.message}") } finally { saving.set(false) }
        })
    }

    fun backup() {
        if (!plugin.dataFolder.exists()) return
        if (db != null) {
            db.backupDb()
        } else {
            val src = dataFolder()
            if (!src.exists()) return
            val yaml = YamlConfiguration.loadConfiguration(src)
            yaml.save(backupFile())
        }
        if (plugin.config.getBoolean("history-export.enabled", true)) exportHistoryCsv()
    }

    fun updatePrices(sensitivity: Double, historyLength: Int) {
        val clampedSensitivity = sensitivity.coerceIn(0.0, 1.0)
        for (item in items.values) {
            val delta = (item.demand - item.supply) * clampedSensitivity
            val newPrice = (item.currentPrice * (1.0 + delta)).coerceIn(item.minPrice, item.maxPrice)

            // record history
            val point = PricePoint(Instant.now(), newPrice)
            item.history.addLast(point)
            if (db != null) db.appendHistory(item.material, point)
            while (item.history.size > historyLength) item.history.removeFirst()

            item.currentPrice = newPrice
            // reset demand/supply after each cycle
            item.demand = 0.0
            item.supply = 0.0
        }
    }

    private fun seedDefaults() {
        val seedAll = plugin.config.getBoolean("seed-all-materials", false)
        val cfgNames = plugin.config.getStringList("seed-items") // legacy support if present in user config
        val addCurated = plugin.config.getBoolean("include-default-important-items", true)
        val blacklistNames = plugin.config.getStringList("blacklist-items").map { it.uppercase() }.toSet()

        val allNames = if (seedAll) {
            Material.entries
                .asSequence()
                .filter { !it.isAir && !it.name.startsWith("LEGACY_") }
                .map { it.name }
                .toList()
        } else {
            (cfgNames.map { it.uppercase() } + if (addCurated) curatedImportantMaterialNames() else emptyList())
        }
            .distinct()
            .filterNot { it in blacklistNames }

        val defaults = allNames.mapNotNull { Material.matchMaterial(it) }
        for (mat in defaults) {
            val base = defaultBasePriceFor(mat)
            val min = (base * 0.30).coerceAtLeast(1.0)
            val max = (base * 6.0).coerceAtLeast(min + 1.0)
            items[mat] = MarketItem(
                material = mat,
                basePrice = base,
                minPrice = min,
                maxPrice = max,
                currentPrice = base,
                demand = 0.0,
                supply = 0.0,
                history = ArrayDeque()
            )
        }
    }

    private fun curatedImportantMaterialNames(): List<String> = listOf(
        // Ores, ingots, gems
        "COAL", "IRON_INGOT", "GOLD_INGOT", "COPPER_INGOT", "NETHERITE_INGOT",
        "DIAMOND", "EMERALD", "LAPIS_LAZULI", "REDSTONE", "QUARTZ",
        // Wood/logs
        "OAK_LOG", "SPRUCE_LOG", "BIRCH_LOG", "JUNGLE_LOG", "ACACIA_LOG", "DARK_OAK_LOG",
        "MANGROVE_LOG", "CHERRY_LOG", "CRIMSON_STEM", "WARPED_STEM", "BAMBOO",
        // Stone/building basics
        "COBBLESTONE", "STONE", "DEEPSLATE", "SAND", "GRAVEL", "GLASS",
        // Crops and plants
        "WHEAT", "CARROT", "POTATO", "BEETROOT", "SUGAR_CANE", "PUMPKIN", "MELON_SLICE",
        "CACTUS", "NETHER_WART", "COCOA_BEANS",
        // Foods (processed)
        "BREAD", "COOKED_BEEF", "COOKED_PORKCHOP", "COOKED_CHICKEN", "COOKED_MUTTON",
        "COOKED_RABBIT", "COOKED_COD", "COOKED_SALMON",
        // Mob drops / rares
        "ROTTEN_FLESH", "BONE", "STRING", "GUNPOWDER", "ENDER_PEARL", "BLAZE_ROD",
        "GHAST_TEAR", "SLIME_BALL", "SPIDER_EYE", "LEATHER", "FEATHER", "INK_SAC", "SHULKER_SHELL",
        // Misc commodities
        "PAPER", "BOOK", "SUGAR", "HONEY_BOTTLE", "HONEYCOMB", "CLAY_BALL", "BRICK"
    )

    private fun defaultBasePriceFor(mat: Material): Double {
        val name = mat.name
        // High-tier and rares
        return when {
            name == "NETHERITE_INGOT" -> 2000.0
            name == "DIAMOND" -> 800.0
            name == "EMERALD" -> 400.0
            name == "LAPIS_LAZULI" -> 80.0
            name == "REDSTONE" -> 30.0
            name == "QUARTZ" -> 60.0
            name.endsWith("_INGOT") && name.startsWith("GOLD") -> 200.0
            name.endsWith("_INGOT") && name.startsWith("IRON") -> 120.0
            name.endsWith("_INGOT") && name.startsWith("COPPER") -> 60.0
            name.endsWith("_LOG") || name.endsWith("_STEM") || name == "BAMBOO" -> 25.0
            name in listOf("COBBLESTONE") -> 4.0
            name in listOf("STONE", "DEEPSLATE") -> 6.0
            name in listOf("SAND", "GRAVEL") -> 8.0
            name == "GLASS" -> 12.0
            name in listOf("WHEAT", "CARROT", "POTATO", "BEETROOT", "SUGAR_CANE") -> 12.0
            name in listOf("PUMPKIN", "MELON_SLICE", "CACTUS", "NETHER_WART", "COCOA_BEANS") -> 14.0
            name.startsWith("COOKED_") -> 20.0
            name in listOf("ROTTEN_FLESH") -> 2.0
            name in listOf("BONE", "FEATHER", "SPIDER_EYE") -> 6.0
            name in listOf("STRING", "INK_SAC", "CLAY_BALL", "BRICK") -> 8.0
            name == "SLIME_BALL" -> 20.0
            name == "GUNPOWDER" -> 25.0
            name == "ENDER_PEARL" -> 80.0
            name == "BLAZE_ROD" -> 90.0
            name == "GHAST_TEAR" -> 400.0
            name == "SHULKER_SHELL" -> 300.0
            name == "PAPER" -> 6.0
            name == "BOOK" -> 20.0
            name == "SUGAR" -> 6.0
            name == "HONEY_BOTTLE" -> 25.0
            name == "HONEYCOMB" -> 12.0
            else -> 100.0
        }
    }

    private fun exportHistoryCsv() {
        try {
            if (!historyDir().exists()) historyDir().mkdirs()
            for ((mat, item) in items) {
                val f = File(historyDir(), "${mat.name}.csv")
                FileWriter(f, false).use { w ->
                    w.appendLine("time,price")
                    for (p in item.history) {
                        w.appendLine("${p.time},${p.price}")
                    }
                }
            }
        } catch (t: Throwable) {
            plugin.logger.warning("Failed to export history CSV: ${t.message}")
        }
    }

    private fun applyBlacklist() {
        val blacklist = plugin.config.getStringList("blacklist-items").map { it.uppercase() }.toSet()
        if (blacklist.isEmpty()) return
        val toRemove = items.filterKeys { it.name in blacklist }.keys
        toRemove.forEach { items.remove(it) }
        if (toRemove.isNotEmpty()) plugin.logger.info("Excluded ${toRemove.size} blacklisted items from market.")
    }
}
