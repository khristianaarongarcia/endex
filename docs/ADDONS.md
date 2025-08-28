# Addons for The Endex

The Endex supports loading external addon jars at runtime from the `plugins/TheEndex/addons/` folder.

How it works:
- On startup, The Endex scans `plugins/TheEndex/addons/` for `*.jar` files.
- Each jar is loaded with its own classloader and scanned via Java `ServiceLoader` for implementations of `org.lokixcz.theendex.addon.EndexAddon`.
- For each discovered addon, `init(plugin)` is called so it can register listeners/commands and access the Endex API.

Settings:
- A settings root is created at `plugins/TheEndex/addons/settings/`.
- Each addon has a dedicated folder by its id, e.g. `plugins/TheEndex/addons/settings/<addon-id>/`.
- Store your addon configuration files inside that folder.

Authoring an addon:
1) Add a dependency on The Endex API (compileOnly is fine). Implement the interface:
   - `org.lokixcz.theendex.addon.EndexAddon`
2) Provide a service descriptor file inside your jar:
   - `META-INF/services/org.lokixcz.theendex.addon.EndexAddon`
   - Content: The fully qualified class name of your implementation
3) Place your built jar into `plugins/TheEndex/addons/` and restart/reload the server.

Notes:
- Each addon jar is isolated via a URLClassLoader with The Endex classloader as parent.
- Duplicate addon ids are skipped with a warning.
- On plugin disable/reload, addons receive `onDisable()` and their classloaders are closed.

## Command integration for addons

Addons can surface commands to players through the host plugin without a plugin.yml.

Three helper hooks are available on the host plugin instance:
- `registerAddonSubcommand(name: String, handler: AddonSubcommandHandler)`
- `registerAddonAlias(alias: String, targetSubcommand: String): Boolean`
- `registerCompleter(name: String, completer: AddonTabCompleter)`

How it works:
- Register a top-level subcommand name (e.g., `"crypto"`). Players then use `/endex crypto ...`.
- Optionally register one or more alias commands that directly invoke your subcommand. For example, registering alias `"cc"` for target subcommand `"crypto"` lets players use `/cc ...`.
- Provide a tab completer for your subcommand so `/endex crypto <TAB>` and alias commands like `/cc <TAB>` offer suggestions.

Kotlin sketch:
```kotlin
host.registerAddonSubcommand("crypto", AddonSubcommandHandler { sender, _, args ->
   when (args.firstOrNull()?.lowercase()) {
      null, "help" -> { /* show help */; true }
      "buy" -> { /* ... */; true }
      else -> false
   }
})

// Optional alias commands mapped to this subcommand
listOf("cc", "crypto").forEach { host.registerAddonAlias(it, "crypto") }

// Tab completion
host.javaClass.getDeclaredField("addonCommandRouter").apply { isAccessible = true }
   .get(host)
   .let { it as? org.lokixcz.theendex.addon.AddonCommandRouter }
   ?.registerCompleter("crypto", AddonTabCompleter { _, _, args ->
      val base = listOf("help","buy","sell","transfer","shop","admin")
      when (args.size) {
         0 -> base
         1 -> base.filter { it.startsWith(args[0], ignoreCase = true) }
         else -> emptyList()
      }
   })
```

Tab completion propagation:
- The Endex routes addon subcommand completions into the main `/endex` command.
- Alias commands registered via `registerAddonAlias` also delegate their tab completion to your registered completer, so `/cc <TAB>` yields the same suggestions as `/endex crypto <TAB>`.
