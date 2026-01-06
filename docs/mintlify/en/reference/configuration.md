------

title: "Configuration"title: "Configuration"

description: "Complete reference for all config.yml settings with performance tips."description: "Key settings in config.yml and common tuning tips."

------



The main configuration file is located at:The main configuration file is:



```text```text

plugins/TheEndex/config.ymlplugins/TheEndex/config.yml

``````



After making changes, run `/endex reload` or restart your server.After changes, run:



<Info>```text

Each setting is tagged with its performance impact:/endex reload

- **[PERF: MINIMAL]** — Negligible impact, safe for any server```

- **[PERF: LOW]** — Low CPU/memory usage, safe for all servers

- **[PERF: MEDIUM]** — Moderate impact, adjust based on server size## Core pricing

- **[PERF: HIGH]** — Significant impact, optimize for large servers (50+ players)

- **[PERF: CRITICAL]** — Heavy resource usage, careful tuning recommended```yaml

</Info>update-interval-seconds: 60

price-sensitivity: 0.05

---history-length: 5

autosave-minutes: 5

## Language Settingssave-on-each-update: true

```

Set the language for all plugin messages.

## Storage mode

```yaml

language:```yaml

  locale: enstorage:

```  sqlite: false

```

**Available languages:**

| Code | Language |When enabling SQLite, restart the server so the plugin can migrate data if needed.

|------|----------|

| `en` | English (default) |## Update Checker

| `pl` | Polish (Polski) |

| `zh_CN` | Chinese Simplified (简体中文) |```yaml

| `ru` | Russian (Русский) |update-checker:

| `es` | Spanish (Español) |  enabled: true      # Check for updates on startup

| `de` | German (Deutsch) |  notify-ops: true   # Notify OP players on join

| `fr` | French (Français) |```

| `pt` | Portuguese (Português) |

| `ja` | Japanese (日本語) |## Investments

| `ko` | Korean (한국어) |

```yaml

Language files are located in `plugins/TheEndex/lang/`. To add a custom translation, copy `lang/en.yml` and translate it.investments:

  enabled: true

---  apr-percent: 5.0

```

## Core Pricing Settings

## Events

### Price Update Cycle [PERF: MEDIUM]

```yaml

```yamlevents:

update-interval-seconds: 120  multiplier-cap: 10.0

```  stacking:

    mode: multiplicative

How often (in seconds) the market recalculates prices.    default-weight: 1.0

    max-active: 0

| Value | Use Case |```

|-------|----------|

| 30-60s | Small servers, responsive market |See: [Events](../features/events)

| 120s | Default, balanced |

| 300-600s | Large servers (50+ players) |## Inventory-aware pricing (optional)



### Price Sensitivity [PERF: MINIMAL]```yaml

price-inventory:

```yaml  enabled: false

price-sensitivity: 0.05  sensitivity: 0.02

```  per-player-baseline: 64

  max-impact-percent: 10.0

Controls how much demand/supply imbalance affects prices per cycle. Formula: `new_price = old_price × (1 + (demand - supply) × sensitivity)````



| Value | Effect |## Web settings (high-level)

|-------|--------|

| 0.01-0.03 | Gradual, stable prices |```yaml

| 0.04-0.07 | Balanced responsiveness |web:

| 0.08-0.15 | Volatile, active trading |  roles:

    default: TRADER

### History Tracking [PERF: LOW]    trader-permission: endex.web.trade

    admin-view-permission: endex.web.admin

```yaml  api:

history-length: 5    tokens: []

```    token-hashes: []

```

Number of past price points kept for charts/sparklines. Uses ~0.5KB RAM per item × history-length.

## Custom web UI

---

To serve your own dashboard bundle:

## Price Smoothing & Volatility Protection [PERF: MINIMAL]

```yaml

Prevents dramatic price swings from manipulation or sudden spikes.web:

  custom:

```yaml    enabled: false

price-smoothing:    root: webui

  enabled: true    reload: false

  ema-alpha: 0.3    export-default: true

  max-change-percent: 15.0```

```

## Inflation System

| Setting | Description |

|---------|-------------|Control gradual price changes over time:

| `ema-alpha` | EMA factor (0.0-1.0). Lower = smoother prices |

| `max-change-percent` | Maximum % change per cycle. Set to 0 to disable |```yaml

inflation:

---  enabled: false

  base-rate: 0.001          # 0.1% per cycle (positive = inflation, negative = deflation)

## Inflation / Deflation System [PERF: MINIMAL]  variance: 0.0005          # Random variance ±0.05%

  apply-to-base: false      # Permanently adjust base prices

Automatically adjusts all market prices over time.  category-rates:           # Per-category overrides

    ORES: 0.002             # Ore prices inflate faster

```yaml    FOOD: -0.0005           # Food prices deflate slightly

inflation:    BUILDING: 0.0           # Building materials stable

  enabled: false```

  rate-per-cycle: 0.01

  category-rates:<Info>

    ores: 0.02Inflation is applied during each price update cycle. Set `apply-to-base: true` to make changes permanent, or leave it `false` for temporary price drift that resets on restart.

    food: 0.0</Info>

    building: 0.01

    mob-drops: 0.01## GUI Customization

  require-players: true

  max-deviation-percent: 200.0GUI layouts are configured in separate files under `guis/`:

```

- `guis/market.yml` — Main market interface

| Setting | Description |- `guis/details.yml` — Item details panel

|---------|-------------|- `guis/holdings.yml` — Virtual holdings panel

| `rate-per-cycle` | % change per update cycle. Positive = inflation, negative = deflation |- `guis/deliveries.yml` — Delivery queue panel

| `category-rates` | Override rates for specific categories |

| `require-players` | Only apply when players are online |Each file supports:

| `max-deviation-percent` | Cap on total price change from base price |- Custom titles with color codes

- Inventory size (rows × 9)

<Tip>- Slot positions for buttons and items

With a 120s update cycle, 0.01% per cycle ≈ 7.2% per day.- Category definitions with custom materials

</Tip>

## Command Aliases

---

Custom command aliases are configured in `commands.yml`:

## Buy/Sell Spread (Anti-Arbitrage) [PERF: MINIMAL]

```yaml

Creates a price gap between buy and sell prices to prevent arbitrage exploits.aliases:

  shop: "market"           # /shop → /market

```yaml  stock: "market holdings" # /stock → /market holdings

spread:  prices: "market top"     # /prices → /market top

  enabled: true```

  buy-markup-percent: 1.5

  sell-markdown-percent: 1.5For the full deep-dive config guide, see `docs/CONFIG.md` in the repo.

```

Example with 1.5% spread each way:
- Market price: $100
- Buy price: $101.50 (1.5% above)
- Sell price: $98.50 (1.5% below)
- Total spread: 3% gap

---

## Transaction Tax [PERF: MINIMAL]

```yaml
transaction-tax-percent: 0.0
```

Percentage tax applied to ALL buy AND sell transactions. Acts as a money sink.

---

## Data Storage [PERF: MEDIUM to HIGH]

```yaml
storage:
  sqlite: true
```

| Mode | Description |
|------|-------------|
| `sqlite: false` | YAML files (human-readable, slower with many items) |
| `sqlite: true` | SQLite database (faster, scalable, **recommended**) |

### Autosave & Backup [PERF: LOW to MEDIUM]

```yaml
autosave-minutes: 5
save-on-each-update: false
```

<Warning>
`save-on-each-update: true` causes disk I/O every update cycle. Only enable if your server crashes frequently.
</Warning>

---

## Inventory-Driven Pricing [PERF: LOW]

Adjusts prices based on what online players are holding.

```yaml
price-inventory:
  enabled: false
  sensitivity: 0.02
  per-player-baseline: 64
  max-impact-percent: 10.0
```

| Setting | Description |
|---------|-------------|
| `sensitivity` | How strongly inventory levels affect prices (0.0-1.0) |
| `per-player-baseline` | Expected items per player. Above = price drops |
| `max-impact-percent` | Maximum price impact per cycle |

---

## World Storage Scanner [PERF: HIGH to CRITICAL]

<Warning>
⚠️ **Most performance-intensive feature!** Scans ALL containers in loaded chunks.
</Warning>

```yaml
price-world-storage:
  enabled: false
  scan-interval-seconds: 300
  sensitivity: 0.01
  global-baseline: 1000
  max-impact-percent: 5.0
  chunks-per-tick: 50
  
  containers:
    chests: true
    barrels: true
    shulker-boxes: true
    hoppers: false
    droppers: false
    dispensers: false
    furnaces: false
    brewing-stands: false
  
  scan-shulker-contents: true
  excluded-worlds: []
```

### Anti-Manipulation Protection

```yaml
price-world-storage:
  anti-manipulation:
    per-chunk-item-cap: 10000
    per-material-chunk-cap: 5000
    min-tps: 18.0
    log-suspicious: true
```

### Smart Caching [PERF: CRITICAL - Enable for best performance!]

```yaml
price-world-storage:
  cache:
    enabled: true
    chunk-expiry-seconds: 600
    full-refresh-cycles: 5
    persist-to-disk: true
```

Caching reduces scan work by **70-90%** on established servers.

---

## Virtual Holdings System [PERF: LOW]

Virtual storage for purchased items.

```yaml
holdings:
  enabled: true
  max-total-per-player: 100000
  auto-withdraw-on-login: false
```

| Setting | Description |
|---------|-------------|
| `max-total-per-player` | Maximum total items across all materials. -1 for unlimited |
| `auto-withdraw-on-login` | Automatically give items when player logs in |

---

## Delivery System [PERF: LOW]

Overflow protection for full inventories.

```yaml
delivery:
  enabled: true
  auto-claim-on-login: false
  max-pending-per-player: 10000
```

---

## Investment System [PERF: LOW]

Passive income through compound interest.

```yaml
investments:
  enabled: true
  apr-percent: 5.0
```

Interest compounds **continuously** based on time elapsed since purchase.

---

## Market Events

Temporary price multipliers (e.g., "Diamond Rush").

```yaml
events:
  multiplier-cap: 10.0
```

Configure events in `events.yml`. See [Events](/features/events) for details.

---

## Shop System Mode [PERF: MINIMAL]

Choose between default scrollable market or custom category-based shop.

```yaml
shop:
  mode: DEFAULT
  main-shop: main
  command: shop
```

| Mode | Description |
|------|-------------|
| `DEFAULT` | Standard scrollable market list |
| `CUSTOM` | Category-based shop with main menu |

### Custom Shop Settings

```yaml
shop:
  custom:
    items-per-page: 45
    show-back-button: true
    back-button-slot: 49
    show-pagination: true
    prev-page-slot: 48
    next-page-slot: 50
    show-amount-button: true
    amount-button-slot: 45
    admin-edit: true
    price-lore:
      - "&7"
      - "&aLeft Click: &fBuy: &6$%buy_price%"
      - "&cRight Click: &fSell: &6$%sell_price%"
      - "&7"
      - "&8Stock: %stock% | Demand: %demand%"
    admin-lore: "&e⚡ Shift Click: Edit item (Admin)"
```

---

## GUI Customization [PERF: MINIMAL]

```yaml
gui:
  details-chart: true
```

GUI layouts are configured in separate files under `guis/`:
- `guis/market.yml` — Main market interface
- `guis/details.yml` — Item details panel
- `guis/holdings.yml` — Virtual holdings panel
- `guis/deliveries.yml` — Delivery queue panel

---

## Resource Tracking

```yaml
tracking:
  resources:
    enabled: true
    apply-to-market: false
    sources:
      block-break: true
      mob-drops: true
      fishing: true
```

<Warning>
`apply-to-market: true` adds gathered resources to supply, which can cause deflation on active servers.
</Warning>

---

## History Export [PERF: LOW]

Export price history to CSV files for external analysis.

```yaml
history-export:
  enabled: true
  folder: history
```

Files are saved to `plugins/TheEndex/history/*.csv`.

---

## Item Blacklist

Materials that should never appear in the market.

```yaml
blacklist-items: []
```

Example: `blacklist-items: [BEDROCK, COMMAND_BLOCK, BARRIER, SPAWNER]`

---

## Initial Market Setup [PERF: MINIMAL]

Only applies on first run (no existing market data).

```yaml
seed-all-materials: false
include-default-important-items: true
```

<Warning>
`seed-all-materials: true` creates 1000+ market items! Only for testing.
</Warning>

---

## Web Dashboard [PERF: LOW to MEDIUM]

Built-in web interface for trading and price charts.

### Basic Settings

```yaml
web:
  enabled: true
  host: 127.0.0.1
  port: 3434
  session-duration-hours: 2
```

<Tip>
Use `host: 127.0.0.1` with a reverse proxy (nginx/Caddy) for production HTTPS.
</Tip>

### Compression

```yaml
web:
  compression:
    enabled: true
    level: 4
```

### Real-Time Updates

```yaml
web:
  poll-ms: 1000
  history-limit: 120
  
  sse:
    enabled: false
  
  websocket:
    enabled: true
```

### Rate Limiting

```yaml
web:
  rate-limit:
    enabled: true
    requests: 30
    per-seconds: 10
    exempt-ui: true
```

### Permissions & Roles

```yaml
web:
  roles:
    default: TRADER
    trader-permission: endex.web.trade
    admin-view-permission: endex.web.admin
```

| Role | Capabilities |
|------|-------------|
| `TRADER` | View prices, charts, execute trades |
| `VIEWER` | Read-only access |

### Item Icons

```yaml
web:
  icons:
    enabled: false
    source: ""
```

Set `source` to a resource pack path or URL to enable Minecraft textures in the web UI.

### Custom Web UI Override

```yaml
web:
  custom:
    enabled: false
    root: webui
    reload: false
    export-default: true
```

### Holdings API

```yaml
web:
  holdings:
    inventory:
      enabled: true
      include-enderchest: false
      cache-seconds: 15
```

### Addon Display

```yaml
web:
  addons: []
```

Example:
```yaml
web:
  addons:
    - name: "Crypto Addon"
      description: "Futures trading and cryptocurrency"
```

---

## Logging & Debugging [PERF: LOW]

```yaml
logging:
  verbose: false
```

<Warning>
Verbose mode significantly increases log file size. Enable only when debugging.
</Warning>

---

## Update Checker [PERF: MINIMAL]

```yaml
update-checker:
  enabled: true
  notify-ops: true
```

---

## Command Aliases

Custom command shortcuts in `commands.yml`:

```yaml
aliases:
  shop: "market"
  stock: "market holdings"
  prices: "market top"
```

---

## Performance Optimization Guide

For large servers (50+ players), consider these settings:

```yaml
# Increase update interval
update-interval-seconds: 300

# Disable world storage scanning (biggest CPU saver)
price-world-storage:
  enabled: false

# Or if enabled, use conservative settings
price-world-storage:
  enabled: true
  scan-interval-seconds: 600
  chunks-per-tick: 30
  cache:
    enabled: true

# Disable inventory pricing
price-inventory:
  enabled: false

# Reduce history
history-length: 30

# Use SQLite
storage:
  sqlite: true

# Disable save-on-each-update
save-on-each-update: false
```

---

<Note>
For the full deep-dive config guide with more examples, see `docs/CONFIG.md` in the [GitHub repository](https://github.com/khristianaarongarcia/endex).
</Note>
