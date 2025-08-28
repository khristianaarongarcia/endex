package org.lokixcz.theendex.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class EndexTabCompleter : TabCompleter {
    private val base = listOf("help", "market")
    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        val sub = if (sender.hasPermission("theendex.admin")) base + "reload" else base
        return when (args.size) {
            1 -> sub.filter { it.startsWith(args[0], ignoreCase = true) }.toMutableList()
            else -> mutableListOf()
        }
    }
}
