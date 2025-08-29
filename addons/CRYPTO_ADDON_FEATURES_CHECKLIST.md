# Crypto Addon — Features & Roadmap Checklist

Use this as a buffet of optional upgrades specific to the Crypto addon.

How to use:
- [ ] Not started / not requested
- [x] To be Implemented (request it by checking the box and add any notes inline)

Legend:
- [ ] Not started
- [x] To be Implemented
- (est): rough time to implement during normal work hours
- Impact: perf, UX, stability, maintainability, ops, gameplay

---

## Gameplay & Economy

- [X] Token supply models
  - What: Per-token supply policy: fixed cap, inflationary, or halving schedules; optional mint/burn hooks.
  - Why: Make tokens feel distinct and controllable over time.
  - Impact: gameplay, economy
  - (est): 1–2 days (config schema + tickers + persistence)

- [X] Burn mechanics on fees/redemptions
  - What: Portion of trading fees or redemption flows are burned (removed from supply).
  - Why: Create deflation and long-term value pressure.
  - Impact: gameplay, economy
  - (est): 4–6h

- [X] Advanced order types
  - What: Limit orders, stop‑loss/take‑profit, iceberg orders (hidden size component).
  - Why: Deeper trading strategy and risk management.
  - Impact: gameplay, UX
  - (est): 2–4 days (engine + persistence + GUI)

- [X] Liquidity and market making (AMM + Treasury MM)
  - What: AMM pools (x*y=k) for item↔token or token↔token with LP shares/fees; optional treasury market maker quoting within a spread.
  - Why: Continuous liquidity and more robust pricing.
  - Impact: gameplay, economy, UX
  - (est): 3–6 days (math, pool accounting, UI)

- [X] Staking & yields
  - What: Lock tokens for a duration to earn yield (emissions or buyback funded); optional slashing for rule breaks.
  - Why: Introduce long-term commitment and rewards.
  - Impact: gameplay
  - (est): 2–4 days

- [X] Index/basket tokens
  - What: A composite token representing a basket of items/tokens with periodic rebalancing.
  - Why: Simpler exposure for players; new strategy space.
  - Impact: gameplay, UX
  - (est): 2–4 days

- [X] Events & airdrops
  - What: Halving/merge events, seasonal boosts, targeted airdrops to active traders or LPs.
  - Why: Drive engagement and recurring interest.
  - Impact: gameplay, UX
  - (est): 1–2 days

- [X] Crafting integration & token sinks
  - What: Gate special crafts/enchants behind token spend; introduce sinks (e.g., rename costs, cosmetics).
  - Why: Real utility for tokens beyond speculation.
  - Impact: gameplay
  - (est): 1–3 days depending on scope

---

## UX & Web

- [X] Web Crypto tab (tickers, depth, trades)
  - What: Web page showing token tickers (price, 24h change, volume), last trades, basic order book/depth, and a small chart.
  - Why: Discoverability and insight outside the game.
  - Impact: UX
  - (est): 1–3 days (API + UI)

- [X] In‑game Crypto GUI
  - What: Token list with price/24h change/holdings; quick buy/sell; details panel with sparkline/last trades.
  - Why: Smooth trading flow in-game.
  - Impact: UX
  - (est): 2–4 days

- [X] Notifications & alerts
  - What: Price alerts and fill notifications via chat/title and web push (SSE/WS).
  - Why: Keeps players informed and engaged.
  - Impact: UX
  - (est): 1–2 days

- [X] First‑time onboarding & help
  - What: /endex crypto info with rich tips, in‑GUI tooltips, quick walkthrough.
  - Why: Reduce learning curve.
  - Impact: UX
  - (est): 4–6h

---

## Balance & Risk Controls

- [ ] Circuit breakers & daily limits
  - What: Per‑token volatility halts (e.g., >10% per cycle), daily max up/down, trade cooldowns.
  - Why: Contain flash crashes/spikes and abuse.
  - Impact: stability, gameplay
  - (est): 1–2 days

- [ ] Maker/taker fees
  - What: Separate maker/taker fees; dynamic fees during high volatility; fee sink (treasury, burn, LP).
  - Why: Shape market behavior and fund sinks.
  - Impact: gameplay, economy
  - (est): 4–8h

- [ ] Anti‑abuse controls
  - What: Per‑tick trade limits, rate limiting, permission tiers, and burst control.
  - Why: Protect server performance and fairness.
  - Impact: stability, ops
  - (est): 4–8h

---

## Integrations & Data

- [ ] Vault integration polish
  - What: Dual displays (Vault + token), configurable fee split to Vault or token sink.
  - Why: Clearer value context and server revenue tuning.
  - Impact: UX, ops
  - (est): 4–6h

- [ ] Resource oracles (optional)
  - What: Optionally link gathered materials tracking to inform or back tokens.
  - Why: Anchor tokens to gameplay signals.
  - Impact: gameplay, systems
  - (est): 1–2 days

- [ ] Exports & analytics
  - What: SQLite-first ledger (orders, trades, balances), CSV exports, and REST endpoints `/api/crypto/{tickers,trades,orders}`.
  - Why: Insight, dashboards, and integrations.
  - Impact: ops, maintainability
  - (est): 1–2 days

---

## Technical Quality

- [ ] Modernize APIs (Adventure + chat events)
  - What: Replace ChatColor with Adventure (MiniMessage) and AsyncPlayerChatEvent with AsyncChatEvent.
  - Why: Remove deprecations; future‑proof.
  - Impact: maintainability
  - (est): 4–8h

- [ ] Performance & safety
  - What: Async DB writes, bounded order queues, backpressure and retry with jitter; clear off‑main‑thread boundaries.
  - Why: Smooth ticks under load.
  - Impact: perf, stability
  - (est): 1–2 days

- [ ] Tests
  - What: Unit tests for pricing/fees/circuit breakers; small integration test for order flow.
  - Why: Prevent regressions.
  - Impact: maintainability
  - (est): 1–2 days

- [ ] Config & permissions hardening
  - What: Token registry YAML with schema validation; fine‑grained perms (create/issue/operate per token).
  - Why: Safer admin flows and clear errors.
  - Impact: maintainability, ops
  - (est): 4–8h

- [ ] Documentation
  - What: Admin guide (tokens, fees, risk settings) + player guide (orders, PnL) with examples.
  - Why: Lower support burden; clarity.
  - Impact: maintainability
  - (est): 1–2 days

---

## Quick Wins (1–3 days)

- [ ] Modernize deprecations in the Crypto addon
  - What: Migrate ChatColor → Adventure (MiniMessage); AsyncPlayerChatEvent → AsyncChatEvent.
  - Acceptance: No deprecation warnings from the addon at build time.
  - (est): 4–8h

- [ ] Maker/taker fees
  - What: Add maker/taker fee config (default 0.2%/0.3%); apply on trades; support fee sink (treasury/burn).
  - Acceptance: Fees applied and logged; fee totals visible via `/endex crypto info` or admin command.
  - (est): 4–8h

- [ ] Circuit breaker per token
  - What: Halt trading for a token if it moves more than X% within a cycle; configurable thresholds and messages.
  - Acceptance: Trades prevented while halted; clear broadcast and admin override.
  - (est): 4–6h

- [ ] Web tickers + trades endpoints and mini UI
  - What: `/api/crypto/tickers` and `/api/crypto/trades` + a small web section with sparkline.
  - Acceptance: At least one demo token returns data; web shows tickers + sparkline.
  - (est): 1–3 days

- [ ] Token registry YAML
  - What: `addons/crypto/tokens.yml` with schema: symbol, name, supply model, decimals, minTrade, fees, permissions.
  - Acceptance: Validates on startup; helpful error if misconfigured.
  - (est): 4–8h

---

If you check any item(s), I’ll prioritize them and start wiring changes immediately. Add notes inline if you have preferences (e.g., fee sink to burn, circuit breaker at 10%, or AMM pool for DIAMOND↔EXN).