---
title: "Addons"
description: "Extend The Endex with drop-in addon JARs loaded via ServiceLoader."
---

Addons are drop-in JARs loaded from:

```text
plugins/TheEndex/addons/
```

They are not full Bukkit plugins (no plugin.yml). The Endex loads them using Java ServiceLoader.

## What addons can do

- Add custom commands
- Add or extend API routes
- Add custom trading behavior

## How loading works

On startup, The Endex:

1. Scans plugins/TheEndex/addons/*.jar
2. Uses ServiceLoader to find implementations of org.lokixcz.theendex.addon.EndexAddon
3. Calls the addon init hook

## Minimal addon skeleton (Kotlin)

```kotlin
import org.lokixcz.theendex.TheEndex
import org.lokixcz.theendex.addon.EndexAddon

class MyAddon : EndexAddon {
    override fun id() = "my-addon"
    override fun name() = "My Addon"

    override fun init(plugin: TheEndex) {
        // register routes, commands, listeners, etc.
    }
}
```

## Service descriptor

Inside your addon JAR, include:

```text
src/main/resources/META-INF/services/org.lokixcz.theendex.addon.EndexAddon
```

That file should contain your implementation class name (one per line), for example:

```text
com.example.myaddon.MyAddon
```

## Addon settings

If an addon needs config files, a common convention is:

```text
plugins/TheEndex/addons/settings/addon-id/
```

Example:

```text
plugins/TheEndex/addons/settings/crypto/crypto.yml
```

## Verify it loaded

If your build includes an addons list command, you can check:

```text
/endex addons
```

## Permissions

Addons can use The Endex permission system:

| Pattern | Description |
| --- | --- |
| endex.addon.name | Base addon access |
| endex.addon.name.admin | Addon admin features |
| endex.addon.name.trade | Trading via addon |

## Troubleshooting

- Ensure the JAR is inside plugins/TheEndex/addons/
- Check console logs for ServiceLoader errors
- Verify the service descriptor path and class names match exactly

## Related Pages

- [Developer API](api)
- [REST API](../web-api/rest-api)
- [Commands](../reference/commands)
