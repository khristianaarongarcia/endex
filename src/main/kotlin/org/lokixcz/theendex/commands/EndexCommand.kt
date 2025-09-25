package org.lokixcz.theendex.commands

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.lokixcz.theendex.Endex
import org.lokixcz.theendex.gui.MarketGUI

class EndexCommand(private val plugin: Endex) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage("${ChatColor.GOLD}[The Endex] ${ChatColor.AQUA}Dynamic item market")
            sender.sendMessage("${ChatColor.GRAY}Use ${ChatColor.AQUA}/endex help${ChatColor.GRAY} or ${ChatColor.AQUA}/endex market${ChatColor.GRAY} to open the GUI.")
            return true
        }
        
        when (args[0].lowercase()) {
            "help" -> {
                sender.sendMessage("${ChatColor.GOLD}[The Endex] ${ChatColor.AQUA}Commands:")
                val base = listOf(
                    "${ChatColor.AQUA}/endex${ChatColor.GRAY} — Show plugin info",
                    "${ChatColor.AQUA}/endex market${ChatColor.GRAY} — Open the market GUI",
                    "${ChatColor.AQUA}/market price <material>${ChatColor.GRAY} — Price info",
                    "${ChatColor.AQUA}/market top${ChatColor.GRAY} — Top movers",
                    "${ChatColor.AQUA}/market buy <mat> <amt>${ChatColor.GRAY} — Buy",
                    "${ChatColor.AQUA}/market sell <mat> <amt>${ChatColor.GRAY} — Sell",
                    "${ChatColor.AQUA}/market invest [buy|list|redeem-all]${ChatColor.GRAY} — Passive investments"
                )
                base.forEach { sender.sendMessage(it) }
                if (sender.hasPermission("theendex.admin")) {
                    sender.sendMessage("${ChatColor.AQUA}/endex reload${ChatColor.GRAY} — Reload configs and market data")
                    sender.sendMessage("${ChatColor.AQUA}/endex webui export${ChatColor.GRAY} — Export (overwrite) embedded web UI to custom folder")
                    sender.sendMessage("${ChatColor.AQUA}/endex webui reload${ChatColor.GRAY} — Force next load to re-read custom index.html")
                    sender.sendMessage("${ChatColor.AQUA}/market event [list|<name>|end <name>|clear]${ChatColor.GRAY} — Manage events")
                }
                if (sender.hasPermission("theendex.web")) {
                    sender.sendMessage("${ChatColor.AQUA}/endex web${ChatColor.GRAY} — Open web trading interface")
                }
                return true
            }
            
            "market" -> {
                if (sender is Player) {
                    plugin.marketGUI.open(sender)
                } else {
                    sender.sendMessage("${ChatColor.RED}Only players can open the market GUI.")
                }
                return true
            }
            
            "version" -> {
                val ver = plugin.description.version ?: "unknown"
                val useSqlite = plugin.config.getBoolean("storage.sqlite", false)
                val storage = if (useSqlite) "SQLite" else "YAML"
                sender.sendMessage("${ChatColor.GOLD}[The Endex] ${ChatColor.AQUA}v$ver ${ChatColor.GRAY}(storage: $storage)")
                sender.sendMessage("${ChatColor.GRAY}Use ${ChatColor.AQUA}/endex help${ChatColor.GRAY} for commands.")
                return true
            }
            
            "reload" -> {
                if (!sender.hasPermission("theendex.admin")) {
                    sender.sendMessage("${ChatColor.RED}No permission.")
                    return true
                }
                plugin.reloadEndex(sender)
                return true
            }

            "webui" -> {
                if (!sender.hasPermission("theendex.admin")) {
                    sender.sendMessage("${ChatColor.RED}No permission.")
                    return true
                }
                val sub = args.getOrNull(1)?.lowercase()
                val webServer = plugin.getWebServer()
                if (webServer == null) {
                    sender.sendMessage("${ChatColor.RED}Web server not available.")
                    return true
                }
                when (sub) {
                    "export" -> {
                        try {
                            val m = webServer.javaClass.getDeclaredMethod("exportDefaultWebUi", Boolean::class.javaPrimitiveType)
                            m.isAccessible = true
                            m.invoke(webServer, true)
                            sender.sendMessage("${ChatColor.GOLD}[The Endex] ${ChatColor.GREEN}Exported embedded UI to custom folder (force overwrite).")
                        } catch (t: Throwable) {
                            sender.sendMessage("${ChatColor.RED}Failed: ${t.message}")
                        }
                        return true
                    }
                    "reload" -> {
                        try {
                            val f = webServer.javaClass.getDeclaredField("customReload"); f.isAccessible = true
                            val current = f.getBoolean(webServer)
                            f.setBoolean(webServer, true)
                            // Force re-read of index next request by clearing cache
                            val cField = webServer.javaClass.getDeclaredField("cachedCustomIndex"); cField.isAccessible = true; cField.set(webServer, null)
                            sender.sendMessage("${ChatColor.GOLD}[The Endex] ${ChatColor.AQUA}Custom web UI will be reloaded on next page load (temporary override; config controls persistence). Previous reload=$current")
                        } catch (t: Throwable) {
                            sender.sendMessage("${ChatColor.RED}Failed: ${t.message}")
                        }
                        return true
                    }
                    else -> {
                        sender.sendMessage("${ChatColor.GOLD}[The Endex] ${ChatColor.AQUA}/endex webui export ${ChatColor.GRAY}- Export (overwrite) embedded UI to custom root")
                        sender.sendMessage("${ChatColor.GOLD}[The Endex] ${ChatColor.AQUA}/endex webui reload ${ChatColor.GRAY}- Force next request to reload custom index.html")
                        return true
                    }
                }
            }
            
            "track" -> {
                if (!sender.hasPermission("theendex.admin")) {
                    sender.sendMessage("${ChatColor.RED}No permission.")
                    return true
                }
                val sub = args.getOrNull(1)?.lowercase()
                if (sub == "dump") {
                    try {
                        val f = plugin.javaClass.getDeclaredField("resourceTracker"); f.isAccessible = true
                        val rt = f.get(plugin) as? org.lokixcz.theendex.tracking.ResourceTracker
                        if (rt == null) {
                            sender.sendMessage("${ChatColor.RED}Resource tracking is disabled.")
                        } else {
                            val top = rt.top(15)
                            sender.sendMessage("${ChatColor.GOLD}[The Endex] ${ChatColor.AQUA}Top gathered materials (since startup):")
                            if (top.isEmpty()) sender.sendMessage("${ChatColor.GRAY}(no data yet)")
                            for ((mat, amt) in top) {
                                sender.sendMessage("${ChatColor.AQUA}${mat.name}${ChatColor.GRAY}: ${amt}")
                            }
                            sender.sendMessage("${ChatColor.GRAY}Totals persisted in tracking.yml (updated periodically)")
                        }
                    } catch (_: Throwable) {
                        sender.sendMessage("${ChatColor.RED}Unable to read tracker.")
                    }
                    return true
                } else {
                    sender.sendMessage("${ChatColor.RED}Usage: /endex track dump")
                    return true
                }
            }
            
            "web" -> {
                if (sender !is Player) {
                    sender.sendMessage("${ChatColor.RED}This command can only be used by players.")
                    return true
                }
                
                if (!sender.hasPermission("theendex.web")) {
                    sender.sendMessage("${ChatColor.RED}You don't have permission to use the web interface.")
                    return true
                }
                
                val webServer = plugin.getWebServer()
                if (webServer == null) {
                    sender.sendMessage("${ChatColor.RED}Web server is not available.")
                    return true
                }
                
                val url = webServer.createSession(sender)
                val clickableLink = net.kyori.adventure.text.Component.text("Click here to open The Endex Trading Interface")
                    .color(net.kyori.adventure.text.format.NamedTextColor.AQUA)
                    .decorate(net.kyori.adventure.text.format.TextDecoration.UNDERLINED)
                    .clickEvent(net.kyori.adventure.text.event.ClickEvent.openUrl(url))
                    .hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(
                        net.kyori.adventure.text.Component.text("Open trading interface in your browser")
                    ))
                
                sender.sendMessage("${ChatColor.GOLD}[The Endex] ${ChatColor.AQUA}Your personal trading session:")
                sender.sendMessage(clickableLink)
                sender.sendMessage("${ChatColor.GRAY}Session expires in 2 hours. URL: ${ChatColor.WHITE}$url")
                
                return true
            }
            
            else -> {
                // Try addon router
                val routed = try {
                    val routerField = plugin.javaClass.getDeclaredField("addonCommandRouter"); routerField.isAccessible = true
                    val router = routerField.get(plugin) as? org.lokixcz.theendex.addon.AddonCommandRouter
                    router?.dispatch(sender, label, args)
                } catch (_: Throwable) { null }
                if (routed == true) return true
                sender.sendMessage("${ChatColor.RED}Unknown subcommand. Use /endex help")
                return true
            }
        }
    }
}
