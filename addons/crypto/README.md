# Crypto Addon for TheEndex

The Crypto addon adds a token economy on top of TheEndex: AMM pool with LP shares, staking yields, basic advanced orders (limit/stop), airdrops, and crafting sinks. It integrates with Vault and the core Endex market/pricing.

## Installation / Distribution

- Build: the addon jar is produced at `addons/crypto/build/libs/crypto-<version>.jar`.
- Deployment:
  - Server path: `plugins/TheEndex/addons/`
  - After assemble, this project copies the addon jar to `MCTestServer/plugins/TheEndex/addons/` automatically.
  - For a different server folder, copy the jar manually or adjust the Gradle copy task in `addons/crypto/build.gradle.kts`.

## Configuration Reference (Bukkit YAML)

Put these under the Crypto addon’s config (typically `plugins/TheEndex/addons/crypto/config.yml`). Keys shown with defaults/examples:

- fees:
  - sink: `burn` | `treasury` (where trading/staking fees go)

- supply:
  - model: `fixed` | `capped` | `emission` (choose supply regime)
  - cap: 1000000            (max supply when model=capped)
  - emissionPerMinute: 250  (tokens emitted per minute when model=emission)
  - halvingMinutes: 10080   (minutes between emission halves, e.g., weekly)

- amm:
  - enabled: true           (enable constant-product AMM pool)
  - feePercent: 0.3         (swap fee percent; typically 0.2–0.5)

- staking:
  - enabled: true
  - ratePctPerDay: 1.25     (daily percentage yield applied over time)

- airdrops:
  - enabled: true

- limits:
  - buyPerMinute: 10000     (optional throttle values)
  - sellPerMinute: 10000
  - maxOpenOrders: 20

- aliases:
  - token: "Coin"          (display alias for the token)

- refundOnFailure: true      (return spent currency if an operation fails mid-flight)

Web/pricing integration is automatic; when AMM is enabled and the pool has reserves, trade quotes use pool price. Otherwise, it falls back to unit price logic.

## Persistence Files

- `balances.yml` — Per-player token balances.
- `supply.yml` — Total supply, burned total, treasury balance, and supply model state.
- `pool.yml` — AMM pool reserves and LP share totals; per-player LP mapping.
- `stakes.yml` — Active stakes and claimable rewards.
- `crafting-sinks.yml` — Per-material token costs for crafting sinks.

These are stored under the addon’s data folder (e.g., `plugins/TheEndex/addons/crypto/`).

## Commands

User commands:
- `/crypto buy <amount>` — Buy tokens at current price (AMM if enabled).
- `/crypto sell <amount>` — Sell tokens at current price.
- `/crypto transfer <player> <amount>` — Send tokens to another player.
- `/crypto balance` — Show your token balance.
- `/crypto info` — Show supply, burned/treasury, pool stats, etc.
- `/crypto shop` — Open the in-game UI.
- `/crypto stake <amount>` — Stake tokens to earn yield.
- `/crypto claim` — Claim accrued staking rewards.
- `/crypto unstake <amount|all>` — Unstake principal (and any unclaimed rewards are settled).
- `/crypto limit <buy|sell> <price> <amount>` — Place a limit order.
- `/crypto stop <buy|sell> <stopPrice> <amount>` — Place a stop order (converts to a limit when triggered).
- `/crypto cancel <orderId>` — Cancel an open order.

Admin commands:
- `/crypto setprice <unitPrice>` — Set fallback unit price (when AMM inactive).
- `/crypto mint|give <player> <amount>` — Mint tokens (adds to supply; respects model rules).
- `/crypto burn|take <player> <amount>` — Burn tokens (reduces supply).
- `/crypto limits get|set <key> [value]` — Get/set limit values.
- `/crypto reload` — Reload config and state.
- `/crypto pool add <tokenAmt> <moneyAmt>` — Add liquidity to the AMM pool; mints LP shares.
- `/crypto pool remove <lpShares>` — Remove liquidity; burns LP shares and withdraws reserves.
- `/crypto airdrop <amount> <all|online>` — Distribute tokens to all or online players.

Note: Exact permission nodes follow your server’s conventions.

## Quick Start Scenarios

1) Fixed Supply
- Set `supply.model: fixed`.
- Mint an initial allocation to the treasury or designated players.
- (Optional) Disable emission and AMM if you prefer static pricing.

2) Capped Supply
- Set `supply.model: capped` and choose a `cap`.
- Mint up to the cap; burns reduce circulating supply.

3) Emission with Halving
- Set `supply.model: emission` with `emissionPerMinute` and `halvingMinutes`.
- The emission rate halves at each interval; configure staking to complement distribution.

4) AMM LP Bootstrap
- Enable AMM and decide on a starting price (ratio of token to currency reserves).
- Use `/crypto pool add <tokenAmt> <moneyAmt>` to seed initial liquidity.
- Trading now uses the pool price; fees accrue to the configured sink.

## Crafting Sinks Example

`crafting-sinks.yml` example:

```yaml
# Material names are Bukkit `Material` enums
DIAMOND_SWORD: 50
NETHERITE_PICKAXE: 150
ENCHANTING_TABLE: 200
```

When players craft these items, the token cost is charged and added to the sink accounting.

## Notes

- Price quoting prefers AMM when enabled and reserves exist; otherwise falls back to unit price logic.
- A periodic tick processes staking accrual and order execution; default is ~30 seconds.
- Use the `info` and `balance` commands to quickly inspect system state.

## Troubleshooting

- If trades fail due to limits, check the `limits.*` config keys.
- If AMM price seems off, verify the pool reserves and LP seeding were done with the intended ratio.
- If jars aren’t copied to your server, adjust or disable the Gradle copy task in `addons/crypto/build.gradle.kts`.
