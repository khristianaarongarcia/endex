---
title: "PlaceholderAPI Integration"
description: "Complete list of PlaceholderAPI placeholders for The Endex - use in scoreboards, holograms, tab lists, and more."
---

The Endex provides full PlaceholderAPI integration with 30+ placeholders for displaying market data, player holdings, leaderboards, and statistics.

<Info>
PlaceholderAPI is a **soft dependency** - the plugin works without it, but placeholders won't be available.
</Info>

## Installation

1. Install [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) on your server
2. Install The Endex (1.5.3+)
3. Restart your server
4. The expansion registers automatically - no `/papi ecloud download` needed!

## Placeholder Reference

### Market Item Placeholders

Get data for specific items using the material name.

| Placeholder | Description | Example Output |
|-------------|-------------|----------------|
| `%endex_price_<MATERIAL>%` | Current price | `800.00` |
| `%endex_price_formatted_<MATERIAL>%` | Price with currency symbol | `$800.00` |
| `%endex_change_<MATERIAL>%` | Price change since last update | `+5.25%` |
| `%endex_trend_<MATERIAL>%` | Trend arrow | `↑`, `↓`, or `→` |
| `%endex_supply_<MATERIAL>%` | Current supply | `1,234` |
| `%endex_demand_<MATERIAL>%` | Current demand | `567` |

**Examples:**
```
%endex_price_DIAMOND%           → 800.00
%endex_price_formatted_DIAMOND% → $800.00
%endex_trend_DIAMOND%           → ↑
%endex_change_DIAMOND%          → +5.25%
%endex_supply_IRON_INGOT%       → 1,234
%endex_demand_GOLD_INGOT%       → 567
```

### Top Items by Price (1-10)

Display the most expensive and cheapest items on the market.

| Placeholder | Description |
|-------------|-------------|
| `%endex_top_price_<N>%` | Nth most expensive item name |
| `%endex_top_price_<N>_value%` | Nth most expensive item price |
| `%endex_bottom_price_<N>%` | Nth cheapest item name |
| `%endex_bottom_price_<N>_value%` | Nth cheapest item price |

**Examples:**
```
%endex_top_price_1%        → Netherite Ingot
%endex_top_price_1_value%  → 2,000.00
%endex_top_price_2%        → Diamond
%endex_top_price_2_value%  → 800.00
%endex_bottom_price_1%     → Cobblestone
%endex_bottom_price_1_value% → 4.00
```

### Top Gainers & Losers (1-10)

Show items with the biggest price movements.

| Placeholder | Description |
|-------------|-------------|
| `%endex_top_gainer_<N>%` | Nth biggest price gainer name |
| `%endex_top_gainer_<N>_change%` | Nth gainer's change percentage |
| `%endex_top_loser_<N>%` | Nth biggest price loser name |
| `%endex_top_loser_<N>_change%` | Nth loser's change percentage |

**Examples:**
```
%endex_top_gainer_1%        → Emerald
%endex_top_gainer_1_change% → +15.50%
%endex_top_loser_1%         → Iron Ingot
%endex_top_loser_1_change%  → -8.20%
```

### Player Holdings Placeholders

Display information about a player's virtual holdings.

<Note>
These placeholders require the player to be online and are context-sensitive to the viewing player.
</Note>

| Placeholder | Description |
|-------------|-------------|
| `%endex_holdings_total%` | Player's total holdings value |
| `%endex_holdings_count%` | Total number of items in holdings |
| `%endex_holdings_top_<N>%` | Nth most valuable holding item name |
| `%endex_holdings_top_<N>_value%` | Nth most valuable holding value |
| `%endex_holdings_top_<N>_amount%` | Nth most valuable holding quantity |

**Examples:**
```
%endex_holdings_total%       → 125,430.50
%endex_holdings_count%       → 2,456
%endex_holdings_top_1%       → Diamond
%endex_holdings_top_1_value% → 64,000.00
%endex_holdings_top_1_amount% → 80
```

### Holdings Leaderboard (1-10)

Display the richest players by total holdings value.

| Placeholder | Description |
|-------------|-------------|
| `%endex_top_holdings_<N>%` | Nth richest player by holdings (name) |
| `%endex_top_holdings_<N>_value%` | Nth richest player's holdings value |

**Examples:**
```
%endex_top_holdings_1%       → Steve
%endex_top_holdings_1_value% → 1,250,000.00
%endex_top_holdings_2%       → Alex
%endex_top_holdings_2_value% → 890,500.00
```

### Market Statistics

General market-wide statistics.

| Placeholder | Description | Example Output |
|-------------|-------------|----------------|
| `%endex_total_items%` | Total items in the market | `57` |
| `%endex_total_volume%` | Sum of all item prices | `25,430.00` |
| `%endex_average_price%` | Average item price | `446.14` |
| `%endex_active_events%` | Number of active market events | `2` |

## Use Cases

### Scoreboard Example

Create a market scoreboard using plugins like [TAB](https://www.spigotmc.org/resources/tab-1-5-x-1-21-1.57806/) or [AnimatedScoreboard](https://www.spigotmc.org/resources/animatedscoreboard.20848/):

```yaml
scoreboard:
  title: "&6⚡ Market"
  lines:
    - "&7Your Holdings:"
    - "&f%endex_holdings_total%"
    - ""
    - "&7Diamond: &f%endex_price_DIAMOND% %endex_trend_DIAMOND%"
    - "&7Emerald: &f%endex_price_EMERALD% %endex_trend_EMERALD%"
    - "&7Iron: &f%endex_price_IRON_INGOT% %endex_trend_IRON_INGOT%"
    - ""
    - "&7Top Gainer: &a%endex_top_gainer_1%"
    - "&7  %endex_top_gainer_1_change%"
```

### Hologram Example

Display market info using [DecentHolograms](https://www.spigotmc.org/resources/decentholograms.96927/) or [HolographicDisplays](https://dev.bukkit.org/projects/holographic-displays):

```yaml
# /dh create market
# /dh line add market &6&l⚡ THE ENDEX
# /dh line add market &7Real-time Market
# /dh line add market &8─────────────
# /dh line add market &fDiamond: &a$%endex_price_DIAMOND% %endex_trend_DIAMOND%
# /dh line add market &fEmerald: &a$%endex_price_EMERALD% %endex_trend_EMERALD%
# /dh line add market &fNetherite: &a$%endex_price_NETHERITE_INGOT% %endex_trend_NETHERITE_INGOT%
# /dh line add market &8─────────────
# /dh line add market &7Top Trader: &e%endex_top_holdings_1%
```

### Tab List Example

Show market info in tab using [TAB](https://www.spigotmc.org/resources/tab.57806/):

```yaml
header:
  - "&6⚡ The Endex Market"
  - "&7Your Holdings: &f%endex_holdings_total%"
  - "&7Active Events: &f%endex_active_events%"
```

### Leaderboard Signs

Create leaderboard signs with [PlaceholderAPI Sign](https://www.spigotmc.org/resources/placeholderapi-sign.41475/):

```
[papi]
%endex_top_holdings_1%
$%endex_top_holdings_1_value%
&a#1 Trader
```

## Troubleshooting

### Placeholders Not Working

1. **Check PlaceholderAPI is installed:**
   ```
   /papi info
   ```

2. **Verify The Endex expansion is loaded:**
   ```
   /papi list
   ```
   Look for `endex` in the list.

3. **Test a placeholder:**
   ```
   /papi parse me %endex_total_items%
   ```

4. **Check console for errors** on server startup regarding PlaceholderAPI.

### Common Issues

| Issue | Solution |
|-------|----------|
| Returns `%endex_...%` literally | PlaceholderAPI not installed or expansion not registered |
| Returns `N/A` | Material not in market or invalid material name |
| Returns `0` | No data available (e.g., no holdings, no history) |
| Player placeholders blank | Player must be online for holdings data |

## Related Pages

- [Holdings System](holdings.md) — Understanding virtual holdings
- [Configuration](../reference/configuration.md) — Plugin settings
- [Commands](../reference/commands.md) — Available commands
