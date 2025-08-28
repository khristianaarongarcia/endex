package org.lokixcz.theendex.gui

import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.lokixcz.theendex.Endex
import java.io.File
import java.util.*

class PlayerPrefsStore(private val plugin: Endex) {
    private val file = File(plugin.dataFolder, "players.yml")
    private val yaml = YamlConfiguration()

    init {
        if (!plugin.dataFolder.exists()) plugin.dataFolder.mkdirs()
        if (file.exists()) {
            try { yaml.load(file) } catch (_: Throwable) {}
        }
    }

    fun load(uuid: UUID): Map<String, Any?> {
        val base = "players.${uuid}"
        return mapOf(
            "amountIdx" to yaml.getInt("$base.amountIdx", 0),
            "sort" to yaml.getString("$base.sort", "NAME"),
            "category" to yaml.getString("$base.category", "ALL"),
            "search" to yaml.getString("$base.search", ""),
            "page" to yaml.getInt("$base.page", 0)
        )
    }

    fun save(uuid: UUID, prefs: Map<String, Any?>) {
        val base = "players.${uuid}"
        prefs.forEach { (k, v) -> yaml.set("$base.$k", v) }
        saveAsync()
    }

    private fun saveAsync() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            try { yaml.save(file) } catch (_: Throwable) {}
        })
    }
}
