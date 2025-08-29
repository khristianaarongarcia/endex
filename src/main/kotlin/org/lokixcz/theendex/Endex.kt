package org.lokixcz.theendex

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.Bukkit
import org.lokixcz.theendex.market.MarketManager
import org.lokixcz.theendex.commands.MarketCommand
import org.lokixcz.theendex.commands.EndexCommand
import org.lokixcz.theendex.commands.MarketTabCompleter
import org.lokixcz.theendex.commands.EndexTabCompleter
import net.milkbowl.vault.economy.Economy
import org.lokixcz.theendex.gui.MarketGUI
import org.lokixcz.theendex.gui.PlayerPrefsStore
import org.lokixcz.theendex.events.EventManager
import org.lokixcz.theendex.web.WebServer
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.time.format.DateTimeFormatter
import java.time.ZonedDateTime
import org.bukkit.scheduler.BukkitTask

class Endex : JavaPlugin() {

    lateinit var marketManager: MarketManager
        private set
    var economy: Economy? = null
        private set
    lateinit var marketGUI: MarketGUI
        private set
    lateinit var prefsStore: PlayerPrefsStore
        private set
    lateinit var eventManager: EventManager
        private set
    private var addonManager: org.lokixcz.theendex.addon.AddonManager? = null
    private var addonCommandRouter: org.lokixcz.theendex.addon.AddonCommandRouter? = null
    private var resourceTracker: org.lokixcz.theendex.tracking.ResourceTracker? = null
    private var inventorySnapshots: org.lokixcz.theendex.tracking.InventorySnapshotService? = null
    private var webServer: WebServer? = null

    private var priceTask: BukkitTask? = null
    private var backupTask: BukkitTask? = null
    private var eventsTask: BukkitTask? = null
    private var trackingSaveTask: BukkitTask? = null
    private val expectedConfigVersion = 1
    private lateinit var logx: org.lokixcz.theendex.util.EndexLogger

    override fun onEnable() {
        // Ensure config exists
        saveDefaultConfig()
        // Initialize logger utility with config
        logx = org.lokixcz.theendex.util.EndexLogger(this)
        logx.verbose = try { config.getBoolean("logging.verbose", false) } catch (_: Throwable) { false }
        // Print banner
        runCatching {
            val stream = getResource("banner.txt")
            if (stream != null) {
                val ver = description.version
                val date = java.time.ZonedDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                val lines = stream.bufferedReader().readLines().map { it.replace("%VERSION%", ver).replace("%DATE%", date) }
                logx.banner(lines)
            }
        }

        // Migrate config if needed, then warn if mismatch persists
        val migrated = checkAndMigrateConfig()
        if (migrated) reloadConfig()
        try {
            val got = config.getInt("config-version", -1)
            if (got != expectedConfigVersion) {
                logx.warn("Config-version mismatch (expected $expectedConfigVersion, found $got). Consider backing up and regenerating config.yml.")
            }
        } catch (_: Throwable) {}

        // Initialize market manager and load data
        val useSqlite = config.getBoolean("storage.sqlite", false)
    marketManager = if (useSqlite) MarketManager(this, org.lokixcz.theendex.market.SqliteStore(this)) else MarketManager(this)
        marketManager.load()
    logx.info("Market loaded (storage=${if (useSqlite) "sqlite" else "yaml"})")

        // Register public API service for other plugins
        try {
            server.servicesManager.register(org.lokixcz.theendex.api.EndexAPI::class.java, org.lokixcz.theendex.api.EndexAPIImpl(this), this, org.bukkit.plugin.ServicePriority.Normal)
        } catch (_: Throwable) {}

        // Setup Vault economy if present
    setupEconomy()

        // Schedule periodic tasks
        scheduleTasks()

        // Commands
        getCommand("endex")?.apply {
            setExecutor(EndexCommand(this@Endex))
            tabCompleter = EndexTabCompleter()
        }
        getCommand("market")?.apply {
            setExecutor(MarketCommand(this@Endex))
            tabCompleter = MarketTabCompleter()
        }
        // Listeners
        prefsStore = PlayerPrefsStore(this)
        marketGUI = MarketGUI(this)
        server.pluginManager.registerEvents(marketGUI, this)

        // Events
    eventManager = EventManager(this)
        eventManager.load()
        eventsTask = Bukkit.getScheduler().runTaskTimer(this, Runnable { eventManager.tickExpire() }, 20L, 20L * 30) // every 30s
    logx.debug("Event manager initialized and tick task scheduled")

        // Resource tracking
        try {
            if (config.getBoolean("tracking.resources.enabled", true)) {
                val rt = org.lokixcz.theendex.tracking.ResourceTracker(this)
                rt.applyToMarket = config.getBoolean("tracking.resources.apply-to-market", false)
                rt.blockBreak = config.getBoolean("tracking.resources.sources.block-break", true)
                rt.mobDrops = config.getBoolean("tracking.resources.sources.mob-drops", true)
                rt.fishing = config.getBoolean("tracking.resources.sources.fishing", true)
                rt.enabled = true
                // Load previous totals
                runCatching { rt.loadFromDisk() }
                rt.start()
                resourceTracker = rt
                logx.info("Resource tracking enabled (sources: block-break=${rt.blockBreak}, mob-drops=${rt.mobDrops}, fishing=${rt.fishing})")
                // Schedule periodic persistence (every 5 minutes)
                trackingSaveTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, Runnable {
                    try { resourceTracker?.saveToDisk() } catch (t: Throwable) { logx.warn("Failed saving tracking.yml: ${t.message}") }
                }, 20L * 60L * 5, 20L * 60L * 5)
            } else {
                logx.debug("Resource tracking disabled by config")
            }
        } catch (t: Throwable) { logx.warn("Failed to initialize resource tracking: ${t.message}") }

        // Optional inventory snapshots for web holdings (online-only)
        try {
            val invSvc = org.lokixcz.theendex.tracking.InventorySnapshotService(this)
            if (invSvc.enabled()) {
                inventorySnapshots = invSvc
                logx.info("Inventory snapshot service enabled (web.holdings.inventory.*)")
            } else {
                logx.debug("Inventory snapshot service disabled by config (web.holdings.inventory.enabled=false)")
            }
        } catch (t: Throwable) {
            logx.warn("Failed to initialize inventory snapshot service: ${t.message}")
        }

        // Initialize Web Server instance EARLY so addons can register web routes
        try {
            if (webServer == null) {
                webServer = WebServer(this) // don't start yet; allows addons to register routes
            }
        } catch (t: Throwable) {
            logx.warn("Failed to initialize web server instance: ${t.message}")
        }

        // Addons
        try {
            // Initialize router FIRST so addons can register their commands/aliases during init()
            addonCommandRouter = org.lokixcz.theendex.addon.AddonCommandRouter(this)
            addonManager = org.lokixcz.theendex.addon.AddonManager(this).also {
                it.ensureFolder()
                it.loadAll()
            }
            logx.info("Addons loaded and command router ready")
        } catch (t: Throwable) {
            logx.warn("Addon loading failed: ${t.message}")
        }

        // Web Server
        try {
            if (config.getBoolean("web.enabled", true)) {
                val port = config.getInt("web.port", 3434)
                if (webServer == null) webServer = WebServer(this)
                webServer?.start(port)
                // Dynamically register loaded addons into the web Addons tab
                try {
                    val names = addonManager?.loadedAddonNames() ?: emptyList()
                    if (names.isNotEmpty()) {
                        names.forEach { n -> runCatching { webServer?.registerAddonNav(n) } }
                    }
                } catch (_: Throwable) {}
                logx.info("Web server started on port $port")
            } else {
                logx.debug("Web server disabled by config")
            }
        } catch (t: Throwable) {
            logx.warn("Failed to start web server: ${t.message}")
        }
    }

    override fun onDisable() {
        // Stop web server
        try { 
            webServer?.stop() 
            webServer = null
            logx.info("Web server stopped")
        } catch (t: Throwable) { 
            logx.warn("Failed to stop web server: ${t.message}")
        }
        
        // Unregister API service
    try { server.servicesManager.unregister(org.lokixcz.theendex.api.EndexAPI::class.java) } catch (_: Throwable) {}
        // Disable addons
    try { addonManager?.disableAll() } catch (_: Throwable) {}
        addonCommandRouter = null
        // Save market state on shutdown
        if (this::marketManager.isInitialized) {
            try {
                marketManager.save()
            } catch (t: Throwable) {
                logx.error("Failed to save market on disable: ${t.message}")
            }
        }
        // Persist resource tracking
        try { trackingSaveTask?.cancel(); trackingSaveTask = null } catch (_: Throwable) {}
        try { resourceTracker?.saveToDisk() } catch (_: Throwable) {}
    }

    // API for addons to register subcommands and aliases
    fun registerAddonSubcommand(name: String, handler: org.lokixcz.theendex.addon.AddonSubcommandHandler) {
        val ok = try { addonCommandRouter?.registerSubcommand(name, handler); true } catch (_: Throwable) { false }
        if (ok) logx.debug("Registered addon subcommand '$name'") else logx.warn("Failed to register addon subcommand '$name' (router not ready)")
    }
    fun registerAddonAlias(alias: String, targetSubcommand: String): Boolean {
        val registered = addonCommandRouter?.registerAlias(alias, targetSubcommand) ?: false
        if (registered) logx.debug("Registered addon alias '/$alias' -> '$targetSubcommand'") else logx.warn("Failed to register addon alias '/$alias'")
        return registered
    }

    // Getter for web server
    fun getWebServer(): WebServer? = webServer
    fun getInventorySnapshotService(): org.lokixcz.theendex.tracking.InventorySnapshotService? = inventorySnapshots

    private fun setupEconomy() {
        val pm = server.pluginManager
        if (pm.getPlugin("Vault") == null) {
            logx.warn("Vault not found. Economy features (buy/sell) will be disabled.")
            economy = null
            return
        }
        val rsp = server.servicesManager.getRegistration(Economy::class.java)
        if (rsp == null) {
            logx.warn("Vault Economy provider not found. Economy features will be disabled.")
            economy = null
        } else {
            economy = rsp.provider
            logx.info("Hooked into Vault economy: ${economy?.name}")
        }
    }

    private fun scheduleTasks() {
        // Cancel existing tasks if any
        priceTask?.cancel(); priceTask = null
        backupTask?.cancel(); backupTask = null

        val seconds = config.getInt("update-interval-seconds", 60).coerceAtLeast(5)
        val sensitivity = config.getDouble("price-sensitivity", 0.05)
        val historyLength = config.getInt("history-length", 5).coerceAtLeast(1)
        val autosaveMinutes = config.getInt("autosave-minutes", 5).coerceAtLeast(1)

        priceTask = Bukkit.getScheduler().runTaskTimer(this, Runnable {
            try {
                marketManager.updatePrices(sensitivity, historyLength)
                logx.debug("Prices updated with sensitivity=$sensitivity history=$historyLength")
                if (config.getBoolean("save-on-each-update", true)) {
                    marketManager.save()
                    logx.debug("Market saved after update")
                }
                // Refresh open Market GUIs so players see updated data
                try { marketGUI.refreshAllOpen() } catch (_: Throwable) {}
            } catch (t: Throwable) {
                logx.error("Price update failed: ${t.message}")
            }
        }, 20L * seconds, 20L * seconds)

        backupTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, Runnable {
            try {
                marketManager.backup()
                logx.debug("Market backup created")
            } catch (t: Throwable) {
                logx.error("Market backup failed: ${t.message}")
            }
        }, 20L * 60L * autosaveMinutes, 20L * 60L * autosaveMinutes)
    }

    fun reloadEndex(sender: CommandSender?) {
        try {
            // Save current state
            marketManager.save()
        } catch (_: Throwable) {}

        // Reload configs and resources
        reloadConfig()
        // Migrate on reload too (in case an older file is restored)
        val migrated = checkAndMigrateConfig()
        if (migrated) reloadConfig()
        try {
            val got = config.getInt("config-version", -1)
            if (got != expectedConfigVersion) {
                logx.warn("Config-version mismatch after reload (expected $expectedConfigVersion, found $got). Consider backing up and regenerating config.yml.")
            }
        } catch (_: Throwable) {}
        try { eventManager.load() } catch (t: Throwable) { logx.warn("Failed to reload events: ${t.message}") }

        // Reload market data from disk to apply any edits
    try { marketManager.load() } catch (t: Throwable) { logx.warn("Failed to reload market: ${t.message}") }

        // Reschedule tasks with new settings
        scheduleTasks()
        
        // Restart web server with new config (reuse same instance to preserve addon routes)
        try {
            webServer?.stop()
            if (config.getBoolean("web.enabled", true)) {
                val port = config.getInt("web.port", 3434)
                if (webServer == null) webServer = WebServer(this)
                webServer?.start(port)
                // Re-register addon nav entries dynamically after restart
                try {
                    val names = addonManager?.loadedAddonNames() ?: emptyList()
                    if (names.isNotEmpty()) {
                        names.forEach { n -> runCatching { webServer?.registerAddonNav(n) } }
                    }
                } catch (_: Throwable) {}
                logx.info("Web server restarted on port $port")
            }
        } catch (t: Throwable) {
            logx.warn("Failed to restart web server during reload: ${t.message}")
        }
        
        sender?.sendMessage("§6[The Endex] §aReload complete.")
        logx.info("Reloaded The Endex configuration and tasks.")
    }

    private fun checkAndMigrateConfig(): Boolean {
        return try {
            val got = config.getInt("config-version", -1)
            if (got >= expectedConfigVersion) return false

            val cfgFile = File(dataFolder, "config.yml")
            if (!cfgFile.exists()) return false

            // Backup existing config
            val ts = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
            val backup = File(dataFolder, "config.yml.bak-$ts")
            try {
                Files.copy(cfgFile.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING)
                logx.info("Backed up existing config.yml to ${backup.name}")
            } catch (t: Throwable) {
                logx.warn("Failed to backup config.yml: ${t.message}")
            }

            // Load defaults from resource
            val fresh = YamlConfiguration()
            val res = this.getResource("config.yml")
            if (res != null) {
                InputStreamReader(res, StandardCharsets.UTF_8).use { reader ->
                    fresh.load(reader)
                }
            } else {
                // No resource? Use current config as baseline
                fresh.load(cfgFile)
            }

            // Merge user values from old into fresh where keys match
            val oldCfg = YamlConfiguration.loadConfiguration(cfgFile)
            for (path in oldCfg.getKeys(true)) {
                if (oldCfg.isConfigurationSection(path)) continue
                if (fresh.contains(path)) {
                    fresh.set(path, oldCfg.get(path))
                }
            }
            // Set new version
            fresh.set("config-version", expectedConfigVersion)
            fresh.options().header("The Endex configuration (auto-migrated on $ts). Some comments may be lost during migration.")

            // Save merged config
            fresh.save(cfgFile)
            logx.info("Migrated config.yml from version $got to $expectedConfigVersion")
            true
        } catch (t: Throwable) {
            logx.error("Config migration failed: ${t.message}")
            false
        }
    }
}
