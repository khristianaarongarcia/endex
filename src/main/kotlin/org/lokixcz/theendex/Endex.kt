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

    private var priceTask: BukkitTask? = null
    private var backupTask: BukkitTask? = null
    private var eventsTask: BukkitTask? = null
    private val expectedConfigVersion = 1

    override fun onEnable() {
        // Ensure config exists
        saveDefaultConfig()

        // Migrate config if needed, then warn if mismatch persists
        val migrated = checkAndMigrateConfig()
        if (migrated) reloadConfig()
        try {
            val got = config.getInt("config-version", -1)
            if (got != expectedConfigVersion) {
                logger.warning("The Endex config-version mismatch (expected $expectedConfigVersion, found $got). Consider backing up and regenerating config.yml.")
            }
        } catch (_: Throwable) {}

        // Initialize market manager and load data
        val useSqlite = config.getBoolean("storage.sqlite", false)
        marketManager = if (useSqlite) MarketManager(this, org.lokixcz.theendex.market.SqliteStore(this)) else MarketManager(this)
        marketManager.load()

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
    }

    override fun onDisable() {
        // Save market state on shutdown
        if (this::marketManager.isInitialized) {
            try {
                marketManager.save()
            } catch (t: Throwable) {
                logger.severe("Failed to save market on disable: ${t.message}")
            }
        }
    }

    private fun setupEconomy() {
        val pm = server.pluginManager
        if (pm.getPlugin("Vault") == null) {
            logger.warning("Vault not found. Economy features (buy/sell) will be disabled.")
            economy = null
            return
        }
        val rsp = server.servicesManager.getRegistration(Economy::class.java)
        if (rsp == null) {
            logger.warning("Vault Economy provider not found. Economy features will be disabled.")
            economy = null
        } else {
            economy = rsp.provider
            logger.info("Hooked into Vault economy: ${economy?.name}")
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
                if (config.getBoolean("save-on-each-update", true)) {
                    marketManager.save()
                }
            } catch (t: Throwable) {
                logger.severe("Price update failed: ${t.message}")
            }
        }, 20L * seconds, 20L * seconds)

        backupTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, Runnable {
            try {
                marketManager.backup()
            } catch (t: Throwable) {
                logger.severe("Market backup failed: ${t.message}")
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
                logger.warning("The Endex config-version mismatch after reload (expected $expectedConfigVersion, found $got). Consider backing up and regenerating config.yml.")
            }
        } catch (_: Throwable) {}
        try { eventManager.load() } catch (t: Throwable) { logger.warning("Failed to reload events: ${t.message}") }

        // Reload market data from disk to apply any edits
        try { marketManager.load() } catch (t: Throwable) { logger.warning("Failed to reload market: ${t.message}") }

        // Reschedule tasks with new settings
        scheduleTasks()
        sender?.sendMessage("§6[The Endex] §aReload complete.")
        logger.info("Reloaded The Endex configuration and tasks.")
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
                logger.info("Backed up existing config.yml to ${backup.name}")
            } catch (t: Throwable) {
                logger.warning("Failed to backup config.yml: ${t.message}")
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
            logger.info("Migrated config.yml from version $got to $expectedConfigVersion")
            true
        } catch (t: Throwable) {
            logger.severe("Config migration failed: ${t.message}")
            false
        }
    }
}
