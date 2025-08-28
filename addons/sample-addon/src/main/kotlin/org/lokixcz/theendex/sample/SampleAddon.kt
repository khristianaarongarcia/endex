package org.lokixcz.theendex.sample

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.lokixcz.theendex.Endex
import org.lokixcz.theendex.addon.EndexAddon
import org.lokixcz.theendex.api.events.PreBuyEvent
import java.io.File
import java.util.Properties

class SampleAddon : EndexAddon, Listener {
    override fun id() = "sample-addon"
    override fun name() = "Sample Addon"
    override fun version() = "1.0.0"
    private lateinit var host: Endex

    override fun init(plugin: Endex) {
        host = plugin
        host.logger.info("[SampleAddon] init")
        host.server.pluginManager.registerEvents(this, host)

        // Demonstrate using the settings directory: plugins/TheEndex/addons/settings/sample-addon/
        try {
            val settingsDir = File(host.dataFolder, "addons/settings/${id()}")
            if (!settingsDir.exists()) settingsDir.mkdirs()
            val file = File(settingsDir, "config.properties")
            if (!file.exists()) {
                val props = Properties()
                props["emeraldDiscount"] = "0.05"
                file.outputStream().use { props.store(it, "SampleAddon settings") }
            }
        } catch (t: Throwable) {
            host.logger.warning("[SampleAddon] Failed to initialize settings: ${t.message}")
        }
    }

    override fun onDisable() {
        // nothing to clean
    }

    @EventHandler
    fun onPreBuy(e: PreBuyEvent) {
        if (e.material == Material.EMERALD) {
            // small discount for demo, optionally read from settings
            val discount = try {
                val settings = File(host.dataFolder, "addons/settings/${id()}/config.properties")
                if (settings.exists()) {
                    val props = Properties(); settings.inputStream().use { props.load(it) }
                    props.getProperty("emeraldDiscount", "0.05").toDoubleOrNull() ?: 0.05
                } else 0.05
            } catch (_: Throwable) { 0.05 }
            e.unitPrice = e.unitPrice * (1.0 - discount)
        }
    }
}
