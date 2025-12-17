# Configuration

Complete reference for all The Endex configuration options.

---

## Configuration Files

The Endex uses multiple configuration files:

| File | Purpose |
|------|---------|
| `config.yml` | Main configuration |
| `market.yml` | Item prices and data |
| `events.yml` | Market events |

All files are in `plugins/TheEndex/`.

---

## Main Configuration (config.yml)

### Core Settings

```yaml
# Config version (do not modify)
config-version: 15

# Price update interval in seconds
update-interval: 60

# Price sensitivity (0.0 - 1.0)
# Higher = more volatile prices
sensitivity: 0.1

# Transaction tax percentage (0.0 - 100.0)
tax: 2.5

# History length for price tracking
history-length: 100

# Autosave interval in minutes
autosave-interval: 5
```

### Storage

```yaml
storage:
  # Storage type: yaml or sqlite
  type: sqlite
  
  # SQLite database file
  database: market.db
```

**Recommendations:**
- Use `sqlite` for servers with holdings/deliveries
- Use `yaml` for simple setups without persistence needs

### Holdings System

```yaml
holdings:
  # Enable virtual holdings
  enabled: true
  
  # Maximum total items per player
  max-per-player: 10000
  
  # Maximum different materials per player
  max-materials-per-player: 100
  
  # Mode: VIRTUAL or LEGACY
  mode: VIRTUAL
```

**Modes:**
- `VIRTUAL` — Items go to holdings, withdraw manually
- `LEGACY` — Items go directly to inventory

### Delivery System

```yaml
delivery:
  # Enable delivery queue
  enabled: true
  
  # Auto-claim on player login
  auto-claim-on-login: false
  
  # Maximum pending items per player
  max-pending-per-player: 100000
```

### Price Smoothing

```yaml
smoothing:
  # Enable EMA smoothing
  enabled: true
  
  # Smoothing factor (0.0 - 1.0)
  # Higher = more responsive
  factor: 0.3
```

### Inventory-Aware Pricing

```yaml
price-inventory:
  # Enable inventory influence on prices
  enabled: true
  
  # How sensitive prices are to inventory
  sensitivity: 0.02
  
  # Baseline items per player
  per-player-baseline: 64
  
  # Maximum price impact percentage
  max-impact-percent: 10.0
```

### World Storage Scanner

```yaml
price-world-storage:
  # Enable world storage scanning
  enabled: true
  
  # Scan interval in seconds
  scan-interval-seconds: 300
  
  # Price sensitivity
  sensitivity: 0.01
  
  # Baseline items globally
  global-baseline: 1000
  
  # Maximum price impact
  max-impact-percent: 5.0
  
  # Chunks to scan per tick
  chunks-per-tick: 50
  
  # Container types to scan
  containers:
    chests: true
    barrels: true
    shulker-boxes: true
    hoppers: false
    droppers: false
    dispensers: false
    furnaces: false
    brewing-stands: false
  
  # Scan items inside shulker boxes
  scan-shulker-contents: true
  
  # Worlds to exclude from scanning
  excluded-worlds: []
  
  # Anti-manipulation settings
  anti-manipulation:
    # Max items per chunk
    per-chunk-item-cap: 10000
    
    # Max of single material per chunk
    per-material-chunk-cap: 5000
    
    # Skip scanning if TPS below this
    min-tps: 18.0
    
    # Log suspicious activity
    log-suspicious: true
```

### Investments

```yaml
investments:
  # Enable investment system
  enabled: true
  
  # Annual Percentage Rate
  apr: 5.0
  
  # Minimum hold time (seconds)
  min-hold-time: 3600
  
  # Max investments per player
  max-per-player: 10
  
  # Max value per investment
  max-value: 100000
```

### Web Dashboard

```yaml
web:
  # Enable web dashboard
  enabled: true
  
  # Bind address
  host: "0.0.0.0"
  
  # Port number
  port: 8080
  
  # Session settings
  session:
    timeout-minutes: 60
  
  # Real-time updates
  sse:
    enabled: false
  websocket:
    enabled: true
  
  # Rate limiting
  rate-limit:
    enabled: true
    requests: 30
    per-seconds: 10
    exempt-ui: true
  
  # Resource pack icons
  icons:
    enabled: true
    source: ""
  
  # API authentication
  api:
    tokens: []
    token-hashes: []
  
  # Role configuration
  roles:
    default: TRADER
    trader-permission: endex.web.trade
    admin-view-permission: endex.web.admin
  
  # Holdings display
  holdings:
    inventory:
      enabled: true
      include-enderchest: false
      cache-seconds: 15
  
  # Custom UI override
  custom:
    enabled: false
    root-dir: "webui"
```

### Blacklist

```yaml
# Items that cannot be traded
blacklist:
  - BEDROCK
  - BARRIER
  - COMMAND_BLOCK
  - STRUCTURE_BLOCK
  - SPAWNER
  - END_PORTAL_FRAME
```

### Categories

```yaml
categories:
  ores:
    icon: DIAMOND
    items:
      - DIAMOND
      - EMERALD
      - GOLD_INGOT
      - IRON_INGOT
      - COAL
      
  crops:
    icon: WHEAT
    items:
      - WHEAT
      - CARROT
      - POTATO
      - BEETROOT
      
  mob_drops:
    icon: BONE
    items:
      - BONE
      - STRING
      - GUNPOWDER
      - ROTTEN_FLESH
```

### Logging

```yaml
logging:
  # Verbose logging
  verbose: false
  
  # Log file
  file: "endex.log"
```

---

## Events Configuration (events.yml)

```yaml
events:
  ore_rush:
    # Display name with color codes
    display-name: "&6⛏ Ore Rush!"
    
    # Description for players
    description: "Mining materials are in high demand!"
    
    # Duration in seconds
    duration: 3600
    
    # Broadcast start/end
    broadcast: true
    
    # Price multipliers
    multipliers:
      DIAMOND: 1.5
      EMERALD: 1.5
      GOLD_INGOT: 1.3
      IRON_INGOT: 1.2
      COAL: 1.1

  market_crash:
    display-name: "&c📉 Market Crash!"
    description: "Economic downturn!"
    duration: 1800
    broadcast: true
    multipliers:
      "*": 0.8  # All items
```

---

## Market Data (market.yml)

Auto-generated, but you can set base prices:

```yaml
items:
  DIAMOND:
    base-price: 100.0
    min-price: 10.0
    max-price: 1000.0
    
  EMERALD:
    base-price: 50.0
    min-price: 5.0
    max-price: 500.0
```

---

## Full Example Config

```yaml
# ===========================================
# THE ENDEX - CONFIGURATION
# ===========================================

config-version: 15

# ===========================================
# CORE
# ===========================================
update-interval: 60
sensitivity: 0.1
tax: 2.5
history-length: 100
autosave-interval: 5

# ===========================================
# STORAGE
# ===========================================
storage:
  type: sqlite
  database: market.db

# ===========================================
# HOLDINGS
# ===========================================
holdings:
  enabled: true
  max-per-player: 10000
  max-materials-per-player: 100
  mode: VIRTUAL

# ===========================================
# DELIVERY
# ===========================================
delivery:
  enabled: true
  auto-claim-on-login: false
  max-pending-per-player: 100000

# ===========================================
# PRICING
# ===========================================
smoothing:
  enabled: true
  factor: 0.3

price-inventory:
  enabled: true
  sensitivity: 0.02
  per-player-baseline: 64
  max-impact-percent: 10.0

price-world-storage:
  enabled: true
  scan-interval-seconds: 300
  sensitivity: 0.01
  global-baseline: 1000
  max-impact-percent: 5.0

# ===========================================
# WEB
# ===========================================
web:
  enabled: true
  host: "0.0.0.0"
  port: 8080
  session:
    timeout-minutes: 60
  websocket:
    enabled: true
  rate-limit:
    enabled: true
    requests: 30
    per-seconds: 10

# ===========================================
# INVESTMENTS
# ===========================================
investments:
  enabled: true
  apr: 5.0
  min-hold-time: 3600

# ===========================================
# BLACKLIST
# ===========================================
blacklist:
  - BEDROCK
  - BARRIER
  - COMMAND_BLOCK

# ===========================================
# LOGGING
# ===========================================
logging:
  verbose: false
```

---

## Reloading Configuration

After editing configs:

```
/endex reload
```

Some changes may require a server restart.

---

## Related Pages

- [Installation](../getting-started/installation.md) — Initial setup
- [Dynamic Pricing](../features/pricing.md) — Pricing config details
- [Web Dashboard](../web-api/dashboard.md) — Web config details
