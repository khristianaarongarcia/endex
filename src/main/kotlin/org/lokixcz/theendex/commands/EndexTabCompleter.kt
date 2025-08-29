package org.lokixcz.theendex.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.command.PluginCommand

class EndexTabCompleter : TabCompleter {
    private val base = listOf("help", "market")
    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        val addonSubs = try {
            val pc = command as? PluginCommand
            val endex = pc?.plugin as? org.lokixcz.theendex.Endex
            val routerField = endex?.javaClass?.getDeclaredField("addonCommandRouter"); routerField?.isAccessible = true
            val router = routerField?.get(endex) as? org.lokixcz.theendex.addon.AddonCommandRouter
            router?.registeredSubcommands()?.toList() ?: emptyList()
        } catch (_: Throwable) { emptyList() }
        
        // Build command list based on permissions
        val commands = mutableListOf<String>()
        commands.addAll(base)
        
        if (sender.hasPermission("theendex.admin")) {
            commands.add("reload")
        }
        
        if (sender.hasPermission("theendex.web")) {
            commands.add("web")
        }
        
        val sub = commands + addonSubs
        when (args.size) {
            1 -> return sub.filter { it.startsWith(args[0], ignoreCase = true) }.toMutableList()
            else -> {
                // Delegate to addon completer if the first arg is a registered addon subcommand
                val first = args[0].lowercase()
                return try {
                    val pc = command as? PluginCommand
                    val endex = pc?.plugin as? org.lokixcz.theendex.Endex
                    val routerField = endex?.javaClass?.getDeclaredField("addonCommandRouter"); routerField?.isAccessible = true
                    val router = routerField?.get(endex) as? org.lokixcz.theendex.addon.AddonCommandRouter
                    if (router != null && router.registeredSubcommands().contains(first)) {
                        (router.complete(sender, first, args.copyOfRange(1, args.size)) ?: emptyList()).toMutableList()
                    } else mutableListOf()
                } catch (_: Throwable) { mutableListOf() }
            }
        }
    }
}
