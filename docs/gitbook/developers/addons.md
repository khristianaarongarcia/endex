# Addons

Extend The Endex with addon plugins.

---

## What Are Addons?

Addons are separate plugin JAR files that integrate with The Endex:

- **Custom commands** — Add trading commands
- **API routes** — Extend the REST API
- **Custom items** — Non-vanilla tradeable assets
- **Business logic** — Custom trading behavior

---

## Installing Addons

### 1. Download

Get addon JARs from:
- Official releases
- Third-party developers
- Build from source

### 2. Place in Folder

Put addon JARs in:

```
plugins/TheEndex/addons/
```

Create the folder if it doesn't exist.

### 3. Restart Server

Addons load automatically on startup.

### 4. Verify

Check addon loaded:

```
/endex addons
```

---

## Official Addons

### Crypto Addon

Trade virtual cryptocurrencies with simulated markets.

**Features:**
- 20+ cryptocurrencies (BTC, ETH, DOGE, etc.)
- Realistic volatility simulation
- Web dashboard integration
- Holdings with P/L tracking

**Installation:**
1. Download `TheEndex-Crypto-*.jar`
2. Place in `plugins/TheEndex/addons/`
3. Restart server
4. Configure `plugins/TheEndex/crypto.yml`

---

## Configuration

Addons create their own config files:

```
plugins/TheEndex/
├── config.yml          # Main config
├── crypto.yml          # Crypto addon config
├── custom-addon.yml    # Custom addon config
└── addons/
    ├── TheEndex-Crypto.jar
    └── custom-addon.jar
```

---

## Managing Addons

### List Loaded

```
/endex addons
```

### Addon Commands

Access addon commands:

```
/endex <addon> <command> [args]
```

Example for crypto:
```
/endex crypto prices
/endex crypto buy BTC 0.5
```

### API Routes

Addons register under `/api/addon/{name}/`:

```
GET /api/addon/crypto/prices
POST /api/addon/crypto/buy
```

---

## Developing Addons

See the [Developer API](api.md) for creating custom addons.

### Quick Start

1. Add The Endex as dependency
2. Implement `EndexAddon` interface
3. Register via `META-INF/services`
4. Package as JAR

### Example Structure

```
my-addon/
├── build.gradle.kts
└── src/main/
    ├── kotlin/com/example/MyAddon.kt
    └── resources/META-INF/services/
        └── org.lokixcz.theendex.addon.EndexAddon
```

---

## Permissions

Addons can use The Endex permission system:

| Pattern | Description |
|---------|-------------|
| `endex.addon.<name>` | Base addon access |
| `endex.addon.<name>.admin` | Addon admin features |
| `endex.addon.<name>.trade` | Trading via addon |

---

## Troubleshooting

### Addon Not Loading

**Check:**
- JAR is in correct folder
- Compatible with your Endex version
- No errors in console

**Debug:**
```
# Check for load errors
grep -i "addon" logs/latest.log
```

### Command Not Found

Ensure addon registered successfully:
```
/endex addons
```

### API Route 404

Verify addon exposes API:
```
GET /api/addons
```

---

## Related Pages

- [Developer API](api.md) — Build custom addons
- [REST API](../web-api/rest-api.md) — API integration
- [Commands](../reference/commands.md) — Command reference
