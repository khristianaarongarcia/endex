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
                    sender.sendMessage("${ChatColor.AQUA}/market event [list|<name>|end <name>|clear]${ChatColor.GRAY} — Manage events")
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
        }
        sender.sendMessage("${ChatColor.RED}Unknown subcommand. Use /endex help")
        return true
    }
}
