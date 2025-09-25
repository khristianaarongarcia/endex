# 🚀 Endex Crypto Addon Release

**Bring decentralized-style token mechanics to your server economy — inside The Endex.**

> Fully optional addon: drop in, configure, and it auto‑integrates with the unified market view (no legacy tabs required).

---
## ✨ Core Features
- **Server Token (Symbol configurable)**: A synthetic fungible token tracked in persistent storage.
- **Automated Market Maker (AMM) Pool**: Dynamic pricing via a constant‑product style formula (with configurable dampening / mean reversion to Endex item market if enabled).
- **Orders Layer**: Lightweight limit/instant buy & sell order handling (batched matching each tick/cycle depending on config).
- **Staking**: Lock tokens to earn periodic yield (configurable APR / emission schedule / minimum stake). Auto accrual; claim or compound.
- **Airdrops**: Scheduled or manual token distributions (flat, proportional, or activity‑based modes depending on config). Logged for audit.
- **History & Analytics**: Price/time snapshots, volume, staking TVL, recent orders & airdrops — exposed via addon web endpoints.
- **Unified Web UI Presence**: Appears as a special market entry (with badge) plus dedicated JSON endpoints for richer dashboards.
- **Permissions & Roles**: Granular nodes for trading, staking, transferring, and admin mint/airdrop functions.
- **Economy Bridge**: Optional conversion paths between Vault currency and the token (rate & fees configurable).
- **Extensible Hooks**: Event bus signals (order placed, matched, stake updated, airdrop executed) for future automation.

> If a feature above is disabled in the config, related commands/endpoints hide automatically to reduce clutter.

---
## 🧪 Gameplay Loop (Example Flow)
1. Player earns Vault currency (jobs, shops, etc.).
2. Converts part of it into the token or buys via AMM.
3. Stakes tokens to earn yield OR places limit orders to speculate.
4. Periodic airdrop rewards active participants (or holders) — boosting engagement.
5. Player can redeem gains back into Vault currency or reinvest.

---
## 🛠 Commands (Prefix examples; actual may vary if you set aliases)
```
/endex crypto help
/endex crypto info              # Token stats, price, supply, pool, staking APY
/endex crypto balance           # Your token balance
/endex crypto buy <amount>      # AMM buy (Vault -> token)
/endex crypto sell <amount>     # AMM sell (token -> Vault)
/endex crypto price             # Current token spot/AMM price
/endex crypto stake <amount>
/endex crypto unstake <amount>
/endex crypto claim             # Claim staking rewards
/endex crypto orders            # List open orders
/endex crypto order buy <amount> <limitPrice>
/endex crypto order sell <amount> <limitPrice>
/endex crypto cancel <orderId>
/endex crypto airdrop list
/endex crypto airdrop claim <id>   # If claim-based mode
/endex crypto history            # Recent price/volume snapshots

# Admin / elevated
/endex crypto mint <player> <amount>
/endex crypto burn <player> <amount>
/endex crypto airdrop run <mode> <amount>
/endex crypto reload
```

---
## 🔐 Permissions (Suggested Schema)
```
theendex.crypto.info
theendex.crypto.balance
theendex.crypto.trade.buy
theendex.crypto.trade.sell
theendex.crypto.trade.order
theendex.crypto.stake
theendex.crypto.airdrop.claim

# Admin tier
theendex.crypto.admin.mint
theendex.crypto.admin.burn
theendex.crypto.admin.airdrop
theendex.crypto.admin.reload
```
Grant a wildcard like `theendex.crypto.*` to trusted testers while you balance.

---
## ⚙️ Configuration Highlights
```yaml
crypto:
  enabled: true
  symbol: COIN
  decimals: 2
  storage: sqlite            # or yaml
  economy-bridge:
    enabled: true
    buy-fee-percent: 1.0
    sell-fee-percent: 1.5
  amm:
    initial-liquidity: 10000.0
    price-dampening: 0.15    # Mean reversion strength
    spread-percent: 0.25
  orders:
    enabled: true
    match-interval-ticks: 40
    max-open-per-player: 16
  staking:
    enabled: true
    base-apr-percent: 24.0
    reward-interval-seconds: 300
    min-stake: 10.0
  airdrops:
    enabled: true
    mode: PARTICIPATION      # or HOLDINGS / FLAT / CLAIM
    interval-minutes: 180
    amount: 500.0
  history:
    snapshots: 288           # e.g., 5 min * 24h
```
(Adjust names if your actual config differs; this block is illustrative.)

---
## 🌐 Web Integration
- Token shows as a pseudo-item with price, 24h change, and volume.
- Extra JSON endpoints (e.g. `/api/crypto/*`) expose richer data: orders, staking, airdrops, history.
- Compatible with the **Custom Web UI override** — you can build a dedicated crypto panel.

---
## 📦 Installation
1. Ensure main Endex plugin is updated to **v1.2.0+**.
2. Place the Crypto addon JAR into `plugins/TheEndex/addons/`.
3. Restart the server (first run will generate config & storage).
4. Review `crypto:` section in the generated config; tune liquidity, fees, staking APR.
5. Grant appropriate permissions to player groups.
6. (Optional) Expose web endpoints via reverse proxy if you want external dashboards.

### Upgrading
- Replace the addon JAR, then `/endex crypto reload` (or full restart if storage schema changed).
- Review changelog for any renamed keys or migration notes.

---
## 🧮 Balancing Tips
| Goal | Suggestion |
|------|------------|
| Reduce speculation spikes | Increase `price-dampening` and AMM spread |
| Encourage staking | Raise `base-apr-percent` modestly (watch inflation) |
| Control supply growth | Add sell fee or lower airdrop frequency |
| Improve liquidity stability | Seed higher `initial-liquidity` before launch |

---
## 🛡 Risk & Safety
- All economy changes route through main thread-safe calls (avoid race conditions).
- Limit order caps and staking minimums reduce spam.
- Logs include order fills, mints/burns, and airdrops for audit.

---
## ❓ FAQ (Quick)
**Q:** Can players dump instantly?  
**A:** AMM spread + fees + dampening mitigate extreme swings; tune them early.

**Q:** Can I disable staking later?  
**A:** Yes; rewards stop accruing. Let players claim before disabling long-term.

**Q:** Do I have to expose the web endpoints?  
**A:** No; they’re optional. In-game commands cover core interactions.

---
## 📣 Launch Copy (Short Version)
**Endex Crypto Addon is live!** Dynamic token with AMM pricing, staking yields, limit orders & automated airdrops. Drop the addon jar in `addons/`, restart, tweak config, and start building a player‑driven token economy. `/endex crypto help` to begin.

---

Happy trading! 💹
