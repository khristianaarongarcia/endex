package org.lokixcz.theendex.commands

import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class MarketTabCompleter : TabCompleter {
    private val sub = listOf("buy", "sell", "price", "top", "event")
    private val amounts = listOf("1", "8", "16", "32", "64")

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        return when (args.size) {
            1 -> sub.filter { it.startsWith(args[0], ignoreCase = true) }.toMutableList()
            2 -> when (args[0].lowercase()) {
                "buy", "sell", "price" -> materialCompletions(args[1])
                "event" -> eventCompletions(sender, args.drop(1).joinToString(" "))
                else -> mutableListOf()
            }
            3 -> when (args[0].lowercase()) {
                "buy", "sell" -> amounts.filter { it.startsWith(args[2]) }.toMutableList()
                else -> mutableListOf()
            }
            else -> mutableListOf()
        }
    }

    private fun materialCompletions(prefix: String): MutableList<String> {
        val p = prefix.uppercase()
        return Material.entries
            .asSequence()
            .filter { !it.isAir && !it.name.startsWith("LEGACY_") }
            .map { it.name }
            .filter { it.startsWith(p) }
            .take(50)
            .toMutableList()
    }

    private fun eventCompletions(sender: CommandSender, prefix: String): MutableList<String> {
        if (!sender.hasPermission("theendex.admin")) return mutableListOf()
        val plugin = org.bukkit.Bukkit.getPluginManager().getPlugin("TheEndex") as? org.lokixcz.theendex.Endex
        val names = plugin?.eventManager?.listEvents()?.map { it.name } ?: emptyList()
        return names.filter { it.startsWith(prefix, ignoreCase = true) }.take(50).toMutableList()
    }
}
