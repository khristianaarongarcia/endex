
---
title: "Installation"
description: "Install The Endex, start the server, and verify it's working."
---

## Requirements

- A Paper/Spigot-compatible server (Paper/Purpur recommended)
- Java **17+**
- Minecraft **1.20.1 - 1.21.x**

Optional (recommended for buy/sell):

- **Vault**
- An economy provider (EssentialsX, CMI, etc.)

## Download

Get the latest `TheEndex-*.jar` from:

- [Modrinth](https://modrinth.com/plugin/theendex)
- [SpigotMC](https://www.spigotmc.org/resources/theendex)
- [GitHub Releases](https://github.com/khristianaarongarcia/endex/releases)

## Install

1. Stop your server.
2. Copy the jar into your server's `plugins/` folder.
3. Start the server.

## Verify

Run one of these commands in-game or in console:

```text
/endex
/market
```

If you see help output (or the market GUI opens), you're installed.

## Files created

After the first boot, the plugin creates files under `plugins/TheEndex/`:

```text
plugins/
└── TheEndex/
    ├── config.yml
    ├── market.yml
    ├── events.yml
    └── data/
```

<Info>
The Endex migrates many config values between versions, but you should still back up `plugins/TheEndex/` before updating.
</Info>


***

## Step 3: Set Up Economy (Optional)

For buy/sell functionality, you need an economy system:

### Install Vault

1. Download [Vault](https://www.spigotmc.org/resources/vault.34315/)
2. Place in `plugins/` folder
3. Restart server

### Install an Economy Provider

Choose one of these popular options:

| Plugin                                              | Description                |
| --------------------------------------------------- | -------------------------- |
| [EssentialsX](https://essentialsx.net/)             | Most popular, feature-rich |
| [CMI](https://www.spigotmc.org/resources/cmi.3742/) | All-in-one management      |
| [LuckPerms Vault](https://luckperms.net/)           | Lightweight option         |

***

## Step 4: Verify Installation

1. Join your server
2.  Run the version command:

    ```
    /endex version
    ```
3. You should see version info and status
4.  Open the market GUI:

    ```
    /market
    ```

***

## Step 5: Initial Configuration

Edit `plugins/TheEndex/config.yml` to customize:

```yaml
# Basic settings to review
update-interval: 60        # How often prices update (seconds)
sensitivity: 0.1           # Price volatility (0.0 - 1.0)
tax: 2.5                   # Transaction tax percentage

# Storage (sqlite recommended)
storage:
  type: sqlite
  database: market.db

# Enable web dashboard
web:
  enabled: true
  port: 8080
```

After editing, reload the config:

```
/endex reload
```

***

## Installing Addons

Addons extend The Endex with additional features.

1. Download the addon JAR
2.  Place in `plugins/TheEndex/addons/`:

    ```
    plugins/TheEndex/
    └── addons/
        └── CryptoAddon.jar
    ```
3.  Restart server or reload:

    ```
    /endex reload
    ```

***

## Updating

To update The Endex:

1. Download the new version
2. Replace the old JAR in `plugins/`
3. Restart server
4. Check for config migrations in the changelog

<Info>
The Endex automatically migrates configs between versions. Always backup before updating!
</Info>

***

## Next Steps

* [Quick Start Guide](quick-start.md) — Learn the basics
* [Configuration](../reference/configuration.md) — Customize everything
* [Commands](../reference/commands.md) — All available commands
