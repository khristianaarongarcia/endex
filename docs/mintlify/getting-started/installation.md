---
title: "Installation"
description: "Get The Endex up and running on your server in minutes."
---

# Installation

***

## Requirements

Before installing, ensure you have:

* **Minecraft Server:** Paper, Spigot, or Purpur (1.20.1 - 1.21.x)
* **Java:** Version 17 or higher
* **Vault:** For economy integration (optional but recommended)
* **Economy Provider:** EssentialsX, CMI, or similar (requires Vault)

***

## Step 1: Download

Download the latest release from one of these platforms:

* [**Modrinth**](https://modrinth.com/plugin/theendex) (Recommended)
* [**SpigotMC**](https://www.spigotmc.org/resources/theendex)
* [**GitHub Releases**](https://github.com/khristianaarongarcia/endex/releases)

***

## Step 2: Install the Plugin

1. **Stop your server** (recommended for clean installation)
2.  **Place the JAR file** in your server's `plugins/` folder:

    ```
    server/
    └── plugins/
        └── TheEndex.jar
    ```
3. **Start your server**
4.  The plugin will generate default configuration files in:

    ```
    plugins/TheEndex/
    ├── config.yml
    ├── market.yml
    ├── events.yml
    └── data/
    ```

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

{% hint style="info" %}
The Endex automatically migrates configs between versions. Always backup before updating!
{% endhint %}

***

## Next Steps

* [Quick Start Guide](quick-start.md) — Learn the basics
* [Configuration](../reference/configuration.md) — Customize everything
* [Commands](../reference/commands.md) — All available commands
