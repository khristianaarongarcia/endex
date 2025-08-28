
# 🗺️ Roadmap for *The Endex* (Minecraft Stock Market Plugin)

---

## 📌 Phase 1 – Foundation (Core Setup)
✅ Checklist:  
- [x] Create plugin structure with `plugin.yml` (name, version, commands, permissions).  
- [x] Set up main class `Endex.kt` with `onEnable()` and `onDisable()`.  
- [x] Create `MarketManager` class:  
  - [x] Load/save item data from local storage (`market.yml`).  
  - [x] Store each item’s base_price, min_price, max_price, current_price, demand, supply, history.  
- [x] Add scheduled task to update prices at a configurable interval.  
- [x] Implement price calculation formula:  
  ```
  new_price = old_price × (1 + (demand - supply) × sensitivity)
  ```
- [x] Ensure price is clamped between `min_price` and `max_price`.  
- [x] Reset demand/supply after each cycle.  
- [x] Add config with `update-interval-seconds`, `price-sensitivity`, `history-length`, `autosave-minutes`.  
- [x] Implement periodic backup to `market_backup.yml`.  

---

## 📌 Phase 2 – Player Interaction
✅ Checklist:  
- [x] Create `/market` command → opens GUI (chest menu).  
- [x] Design GUI items:  
  - Item name  
  - Current price  
  - Trend arrow (↑ ↓ →)  
  - Short history (last 5 price ticks)  
- [x] Add buy/sell commands:  
  - [x] `/market buy <item> <amount>` → deduct money via Vault, add items.  
  - [x] `/market sell <item> <amount>` → remove items from inventory, pay via Vault.  
- [x] Add `/market price <item>` → shows detailed info.  
- [x] Add `/market top` → lists top 5 gainers/losers.  

Note: Market GUI now supports pagination (54-slot inventory), quick amount selection (1/8/16/32/64), sorting (by name, price, change), category filters, and text search. Left-click buys, right-click sells the selected amount. Preferences (amount, sort, category, search, last page) persist per-player across sessions. The GUI auto-refreshes after trades to reflect price, inventory, and balance changes. A details view (Shift/Middle-click) provides Buy 1, Buy Selected, Buy 64, Sell 1, Sell Selected, and Sell All buttons with a Back control.

---

## 📌 Phase 3 – Economy & Persistence
✅ Checklist:  
- [x] Integrate Vault for economy transactions.  
- [x] Add configurable transaction tax (X% fee per trade).  
- [x] Persist data locally inside `plugins/TheEndex/market.yml`.  
  Example:
  ```yaml
  DIAMOND:
    base_price: 100
    min_price: 20
    max_price: 500
    current_price: 110
    demand: 5
    supply: 2
    history:
      - {time: "2025-08-26T12:00:00", price: 105}
      - {time: "2025-08-26T13:00:00", price: 107}
  ```  
- [x] Add backup system → auto-save to `market_backup.yml` every X minutes.  

---

## 📌 Phase 4 – Market Events & Shocks
✅ Checklist:  
- [x] Add `EventManager` for temporary price multipliers.  
- [x] Load events from `events.yml`:  
  ```yaml
  - name: "Zombie Plague"
    affected_item: "ROTTEN_FLESH"
    multiplier: 3.0
    duration_minutes: 60
  - name: "Iron Mine Collapse"
    affected_item: "IRON_INGOT"
    multiplier: 2.0
    duration_minutes: 30
  ```  
- [x] Apply multipliers during active events.  
- [x] Announce events via chat/broadcast.  
- [x] Allow admins to trigger events with `/market event <name>`.  

Notes:
- Events are defined in `events.yml`. Use `/market event` to list and `/market event <name>` to start one (admin permission required). Use `/market event end <name>` to end one, `/market event clear` to clear all. Multipliers stack up to a configurable cap and are reflected in buy/sell prices, `/market price`, and GUI lore with effective price. Active events persist across restarts.

---

## 📌 Phase 5 – Polish & Extensibility
✅ Checklist:  
- [x] Add permissions system (`theendex.buy`, `theendex.sell`, `theendex.admin`).  
- [x] Add configurable blacklist (items excluded from market).  
- [x] Add history chart support (ASCII sparkline in /market price and GUI details).  
- [x] Optimize performance (optional async save for YAML mode; SQLite path supported).  
- [x] Add investment system (optional “stock certificates” for passive gains).  

---

## 📌 Phase 6 – Release & Maintenance
✅ Checklist:  
- [x] Write clear README with install & config instructions (see README.md, docs/CONFIG.md, docs/COMMANDS.md).  
- [ ] Publish JAR build to Spigot/Paper.  
- [x] Add config-version check and runtime warnings for mismatches.  
- [x] Add automated config migration for future breaking changes.  
- [x] Add /endex version subcommand to display plugin version and storage mode.  
- [ ] Gather feedback → expand features (black market, guild manipulation, futures trading).  

---

⚡ **Summary of Storage:**  
- Use **`market.yml`** in plugin’s folder (`plugins/TheEndex/`) → fast, human-editable, local to the server.  
- Store: base/min/max/current price, supply/demand, and history per item.  
- Optional: Later upgrade to **SQLite** if you need better querying, but YAML/JSON is perfect to start.  
