---
title: "Custom Shop System"
description: "Create EconomyShopGUI-style category-based shops with custom layouts"
---

# Custom Shop System

<Info>
Available since v1.5.0. Enhanced in v1.5.4 with filter-based categories and Holdings/Sort buttons. Layout Editor fixed in v1.5.6.
</Info>

## Overview

The Custom Shop System provides an alternative to the default scrollable market interface. Instead of a single list of all items, players see a **main menu with category icons** that lead to dedicated **category pages** with items.

### Features

- **Category-based navigation** - Organize items into logical categories (Blocks, Ores, Food, etc.)
- **Filter-based auto-population** - Categories automatically include matching items (v1.5.4+)
- **Holdings button** - Access virtual holdings from any category page (v1.5.4+)
- **Sort button** - Sort items by Name, Price, or Change within categories (v1.5.4+)
- **Custom layouts** - Full control over slot positions, decorations, and borders
- **In-game Layout Editor** - Visual GUI editor for shop layouts (v1.5.6 fixes)
- **Market price integration** - Items automatically use dynamic market prices
- **Fixed prices** - Override specific items with static buy/sell prices
- **Multiple shops** - Create different shops for different purposes
- **Pagination** - Automatic page navigation for large categories
- **Admin editing** - Shift+click items to edit (with permission)
- **Sound effects** - Customizable sounds for interactions
- **Permission-based items** - Restrict items to specific groups

## Enabling Custom Shop Mode

Edit your `config.yml`:

```yaml
shop:
  # Change from DEFAULT to CUSTOM
  mode: CUSTOM
  
  # Which shop config to use as main
  main-shop: main
  
  # Custom command (optional)
  command: shop
```

After changing the mode, run `/endex reload` or restart your server.

## Shop Commands

| Command | Description |
|---------|-------------|
| `/market` | Opens custom shop (when mode=CUSTOM) |
| `/market shop [id]` | Opens a specific shop |
| `/market stock` | Opens default market (bypasses mode) |

## Creating a Shop

Shop configurations are stored in `plugins/TheEndex/shops/` as YAML files. The default shop is `main.yml`.

### Basic Structure

```yaml
# Unique identifier
id: main
enabled: true
title: "&5&lServer Shop"

# Main menu settings
menu:
  title: "&8Server Shop"
  size: 54  # Must be multiple of 9
  layout:
    # Define what goes in each slot
    19:
      type: CATEGORY
      category: blocks
    21:
      type: CATEGORY
      category: ores

# Decoration
decoration:
  fill-empty: false
  empty-material: GRAY_STAINED_GLASS_PANE

# Categories with items
categories:
  blocks:
    name: "Blocks"
    icon: BRICKS
    items:
      COBBLESTONE: true
      STONE: true
```

## Menu Layout Types

The `menu.layout` section defines what appears in each slot:

| Type | Description |
|------|-------------|
| `CATEGORY` | Links to a category page |
| `DECORATION` | Static decorative item |
| `EMPTY` | Empty slot (air) |
| `CLOSE` | Close button |
| `INFO` | Information display (shows balance, etc.) |

### Category Slot

```yaml
layout:
  19:
    type: CATEGORY
    category: blocks  # References categories.blocks
```

### Decoration Slot

```yaml
layout:
  0:
    type: DECORATION
    material: BLACK_STAINED_GLASS_PANE
    name: " "
```

### Info Slot

```yaml
layout:
  4:
    type: INFO
    material: GOLD_INGOT
    name: "&6Your Balance: &f%balance%"
    lore:
      - "&7Browse the shop below!"
```

## Category Configuration

Each category has its own page with items:

```yaml
categories:
  ores:
    # Display settings
    name: "Ores & Minerals"
    icon: DIAMOND
    icon-name: "&bOres & Minerals"
    icon-lore:
      - "&7Valuable ores and minerals"
      - "&eClick to browse!"
    
    # Page settings
    page-title: "&8Ores & Minerals"
    page-size: 54
    
    # Item slot range (for auto-arrangement)
    item-slots:
      start: 0
      end: 44
    
    # Fill empty slots
    fill-empty: false
    empty-material: GRAY_STAINED_GLASS_PANE
    
    # Items in this category
    items:
      DIAMOND: true
      EMERALD: true
      GOLD_INGOT: true
```

## Filter-Based Categories (v1.5.4+)

<Info>
New in v1.5.4: Categories can automatically populate items based on material name filters!
</Info>

Instead of manually listing every item, use the `filter` property to auto-populate categories:

```yaml
categories:
  ores:
    name: "Ores & Minerals"
    icon: DIAMOND
    filter: "_ORE|RAW_|DIAMOND|EMERALD|LAPIS|COAL|COPPER_INGOT|IRON_INGOT|GOLD_INGOT|NETHERITE"
    
  wood:
    name: "Wood & Logs"
    icon: OAK_LOG
    filter: "_LOG|_WOOD|_PLANKS|_SLAB|_STAIRS|_FENCE|_DOOR|_BUTTON|_SIGN"
    
  food:
    name: "Food"
    icon: GOLDEN_APPLE
    filter: "APPLE|BREAD|BEEF|PORK|CHICKEN|MUTTON|RABBIT|COD|SALMON|CARROT|POTATO|BEETROOT|MELON|COOKIE|CAKE|PIE|STEW|SOUP"
```

### How Filters Work

- The `filter` property is a **regex pattern** matched against material names
- Use `|` to separate multiple patterns (OR logic)
- Only items that are **enabled in the market** will be included
- Items matching the filter are automatically added to the category

### Filter Examples

| Category | Filter Pattern |
|----------|---------------|
| Ores | `_ORE\|RAW_\|DIAMOND\|EMERALD` |
| Wood | `_LOG\|_WOOD\|_PLANKS` |
| Stone | `STONE\|COBBLE\|GRANITE\|DIORITE\|ANDESITE\|DEEPSLATE` |
| Wool | `_WOOL` |
| Glass | `GLASS` |
| Food | `APPLE\|BREAD\|BEEF\|PORK\|CHICKEN` |

### Combining Filter + Manual Items

You can use both filter and manual items together:

```yaml
categories:
  special:
    name: "Special Items"
    icon: NETHER_STAR
    filter: "NETHERITE|ELYTRA"
    items:
      # Additional manual items not matching the filter
      BEACON: true
      TOTEM_OF_UNDYING:
        buy-price: 10000
        use-market-prices: false
```

## Category Page Buttons (v1.5.4+)

Category pages now include navigation buttons in the bottom row:

| Slot | Button | Function |
|------|--------|----------|
| 45 | 📦 Holdings | Opens your virtual holdings panel |
| 49 | ⬅️ Back | Returns to main shop menu |
| 53 | 🔀 Sort | Cycles sort: Name → Price → Change |

### Sort Options

The Sort button cycles through:
1. **Name** (A-Z) - Alphabetical order
2. **Price** (High-Low) - Most expensive first
3. **Change** (High-Low) - Biggest gainers first

<Tip>
Sort preference is saved per-player and persists across sessions!
</Tip>

## Item Configuration

### Simple Format (Market Prices)

```yaml
items:
  DIAMOND: true
  EMERALD: true
  IRON_INGOT: true
```

Items use dynamic market prices with buy/sell spread.

### Advanced Format (Fixed Prices)

```yaml
items:
  DIAMOND:
    material: DIAMOND
    name: "&b✦ Premium Diamond"
    lore:
      - "&7A sparkling diamond!"
    slot: 0                    # Force specific slot
    buy-price: 1000            # Fixed buy price
    sell-price: 800            # Fixed sell price
    use-market-prices: false   # Disable market integration
    quantity: 1                # Items per transaction
    permission: vip.diamond    # Required permission
    commands:
      on-buy:
        - "say %player% bought a diamond!"
      on-sell:
        - "say %player% sold a diamond!"
```

### Item Properties

| Property | Description | Default |
|----------|-------------|---------|
| `material` | Bukkit material name | Key name |
| `name` | Custom display name | Material name |
| `lore` | Additional lore lines | Empty |
| `slot` | Force specific slot | Auto |
| `buy-price` | Fixed buy price | Market |
| `sell-price` | Fixed sell price | Market |
| `use-market-prices` | Use dynamic prices | true |
| `quantity` | Stack size per buy | 1 |
| `permission` | Required permission | None |
| `commands.on-buy` | Commands on purchase | None |
| `commands.on-sell` | Commands on sale | None |

## Sounds Configuration

```yaml
sounds:
  open-menu: UI_BUTTON_CLICK
  open-category: UI_BUTTON_CLICK
  buy: ENTITY_EXPERIENCE_ORB_PICKUP
  sell: ENTITY_EXPERIENCE_ORB_PICKUP
  error: ENTITY_VILLAGER_NO
  page-change: UI_BUTTON_CLICK
```

Use Bukkit sound names from [Sound API](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html).

## Price Lore Customization

In `config.yml`, customize how prices appear in item lore:

```yaml
shop:
  custom:
    price-lore:
      - "&7"
      - "&aLeft Click: &fBuy: &6$%buy_price%"
      - "&cRight Click: &fSell: &6$%sell_price%"
      - "&7"
      - "&8Stock: %stock% | Demand: %demand%"
    
    admin-lore: "&e⚡ Shift Click: Edit item (Admin)"
```

### Available Placeholders

| Placeholder | Description |
|-------------|-------------|
| `%buy_price%` | Current buy price |
| `%sell_price%` | Current sell price |
| `%market_price%` | Base market price |
| `%stock%` | Supply level |
| `%demand%` | Demand level |

## Navigation Settings

```yaml
shop:
  custom:
    items-per-page: 45      # Items shown per category page
    show-back-button: true
    back-button-slot: 49
    show-pagination: true
    prev-page-slot: 48
    next-page-slot: 50
    admin-edit: true        # Allow admin shift-click editing
```

## In-Game Layout Editor (v1.5.6)

<Info>
The Layout Editor received a complete overhaul in v1.5.6 fixing all major issues with editing, saving, and mode switching.
</Info>

The Layout Editor provides a visual GUI for creating and editing shop layouts without touching config files.

### Opening the Editor

```
/endex shop editor <shop-id>
```

Example: `/endex shop editor main`

### Editor Modes

The editor has 5 modes, selected via the bottom toolbar:

| Mode | Description |
|------|-------------|
| **Place Category** | Click an empty slot to place a category icon |
| **Place Decoration** | Click an empty slot to place decorative items |
| **Place Button** | Click an empty slot to place navigation buttons |
| **Edit Slot** | Click any existing item to modify its name/lore |
| **Remove Slot** | Click any existing item to delete it |

### Editing Names & Lores

1. Switch to **Edit Slot** mode (pencil icon)
2. **Right-click** any existing item
3. Type the new **name** in chat (or "skip" to keep current)
4. Type the new **lore** in chat (lines separated by `|`)
5. Changes appear immediately in the preview

<Warning>
Remember to click **Save** before closing the editor! Unsaved changes are preserved while navigating within the editor but lost on close.
</Warning>

### Toolbar Buttons

| Slot | Button | Function |
|------|--------|----------|
| 45 | 💾 Save | Save layout to config file |
| 46 | ❌ Cancel | Discard changes and close |
| 47-51 | Mode buttons | Switch editor mode |
| 53 | ℹ️ Help | Show editor instructions |

### Tips

- **Left-click** existing items to use their default action (depends on mode)
- **Right-click** existing items in Edit mode opens name/lore prompt
- Changes are preserved when selecting categories or buttons from sub-menus
- The editor uses a distinct red title prefix (`§4§l⚙`) to avoid conflicts with the player shop

## Multiple Shops

Create additional shop files in `plugins/TheEndex/shops/`:

```
shops/
├── main.yml        # Default shop
├── vip.yml         # VIP-only shop
└── seasonal.yml    # Event shop
```

Open specific shops with:
```
/market shop vip
/market shop seasonal
```

## Permissions

| Permission | Description |
|------------|-------------|
| `endex.shop.admin` | Edit items via Shift+Click |
| (item permission) | Access specific items |

## Example: VIP Shop

```yaml
# shops/vip.yml
id: vip
enabled: true
title: "&6&lVIP Shop"

menu:
  title: "&8VIP Exclusive Shop"
  size: 27
  layout:
    13:
      type: CATEGORY
      category: exclusive

categories:
  exclusive:
    name: "VIP Items"
    icon: NETHER_STAR
    icon-name: "&6VIP Exclusives"
    icon-lore:
      - "&7Special items for VIP members!"
    page-title: "&8VIP Items"
    page-size: 54
    items:
      ENCHANTED_GOLDEN_APPLE:
        buy-price: 5000
        sell-price: 2500
        use-market-prices: false
        permission: group.vip
      NETHERITE_BLOCK:
        buy-price: 50000
        sell-price: 25000
        use-market-prices: false
        permission: group.vip
```

## Comparison: DEFAULT vs CUSTOM Mode

| Feature | DEFAULT | CUSTOM |
|---------|---------|--------|
| Interface | Scrollable list | Category-based |
| Navigation | Sorting & filters | Main menu → Categories |
| Layout | Fixed | Fully customizable |
| Item arrangement | Automatic | Manual or automatic |
| Multiple shops | No | Yes |
| Price display | Lore | Customizable lore |
| Best for | Stock market feel | Traditional shop feel |

<Tip>
You can always access the default market with `/market stock`, even when custom mode is enabled!
</Tip>
