# The Endex — Features & Optimizations Checklist

Use this as a buffet of optional upgrades.

How to use:
- [ ] Not started / not requested
- [x] Implemented
- To request something next, change it to [x] and add any notes

Legend:
- [ ] Not started
- [x] To be Implemented
- (est): rough time to implement during normal work hours
- Impact: perf, UX, stability, maintainability, ops

---

## Web UI and API

- [x] Configurable polling and history (Implemented by default)
  - What: `web.poll-ms`, `web.history-limit` in `config.yml`; frontend reads them from `/api/session`.
  - Why: Tune network and CPU usage without code changes.
  - Impact: perf, ops
  - (est): already implemented

- [x] History API caching and deltas (Implemented by default)
  - What: `/api/history` supports `since=epochSecond` and a tiny server cache (per material+limit).
  - Why: Fewer bytes and serialization work under 1s refresh.
  - Impact: perf
  - (est): already implemented

- [x] Server-Sent Events (SSE) push updates (Optional; disabled by default) — Implemented
  - What: `/api/sse` endpoint streams events to clients when enabled via `web.sse.enabled: true`.
  - Why: Reduce request volume; smoother real‑time.
  - Risks: Long‑lived connections; needs careful broadcasting logic.
  - Impact: perf, UX
  - (est): 2–4h to wire full push of price ticks and chart deltas

- [x] WebSocket live updates (Add a new line to enable this option)
  - What: Replace polling with WebSocket. Full duplex for future interactivity.
  - Why: Lowest latency, bi‑directional messaging.
  - Risks: More complex lifecycle (heartbeats, reconnection), careful thread use with Bukkit.
  - Impact: perf, UX
  - (est): 1–2 days including client wiring and server broadcaster

- [x] ETag/304 for history — Implemented
  - What: Send `ETag` based on last timestamp; respond 304 when unchanged.
  - Why: Save bandwidth further when no new points.
  - Impact: perf
  - (est): 1–2h

- [x] Rate limiting per session — Implemented
  - What: Prevent abusive API usage (leaky clients, tabs).
  - Why: Protect server resources.
  - Impact: stability, ops
  - (est): 1–2h

- [x] Themes and branding
  - What: Toggle between purple/other palettes, logo slot, server name.
  - Why: Customization for different servers.
  - Impact: UX
  - (est): 2–3h

---

## Market Model & Data

- [x] Persisted history (SQLite) — Implemented by default
  - What: Store and query history in SQLite; load on startup.
  - Why: Keep long‑term charts across restarts.
  - Impact: UX, ops
  - (est): 4–6h (schema, queries, migration helpers)

- [x] History windowing & aggregation — Implemented
  - What: Return last N minutes/hours with downsampling (1m/5m/1h candles).
  - Why: Scalable history API and nicer charts for long periods.
  - Impact: perf, UX
  - (est): 4–8h

- [x] Price volatility guards — Implemented
  - What: Clamp or smooth sudden spikes; simple EMA smoothing.
  - Why: Better player experience; fewer wild swings.
  - Impact: UX
  - (est): 2–4h

- [X] Event-driven multipliers 2.0
  - What: Weighted, stacked event rules with caps (e.g., per-biome, per-dimension).
  - Why: More interesting markets tied to gameplay.
  - Impact: UX, gameplay depth
  - (est): 1–3 days depending on scope

---

## Trading UX

- [X] Order presets & quick buttons
  - What: 1/5/10/Max buttons for buy/sell; total preview.
  - Why: Faster trading.
  - Impact: UX
  - (est): 2–3h

- [X] Holdings view
  - What: Show how many of the selected item the player owns, average cost.
  - Why: Informed trading decisions.
  - Impact: UX
  - (est): 4–6h (requires tracking buys/sells per player)

- [X] Transaction receipts
  - What: Record and list recent trades (download csv?).
  - Why: Transparency; auditing.
  - Impact: UX, ops
  - (est): 4–6h

- [X] Favorites & watchlist
  - What: Pin items to a watchlist; filter list to favorites.
  - Why: Convenience.
  - Impact: UX
  - (est): 2–4h

---

## Performance & Reliability

- [X] Async market updates
  - What: Move non-Bukkit logic off main thread safely; snapshot for web.
  - Why: Keep tick smooth under load.
  - Impact: perf, stability
  - (est): 4–8h

- [X] Caching layer for items
  - What: Immutable snapshots served to APIs; updated after each tick.
  - Why: Reduce lock contention and serialization cost.
  - Impact: perf
  - (est): 2–4h

- [X] Health endpoint and metrics
  - What: `/api/health`, basic counters (requests, errors), last price tick.
  - Why: Ops awareness and monitoring.
  - Impact: ops
  - (est): 2–3h

---

## Admin & Ops

- [x] Permissions and roles for web
  - What: Read-only vs trading sessions; staff mode.
  - Why: Control who can trade vs view.
  - Impact: security, ops
  - (est): 3–6h

- [x] API tokens (alt to session)
  - What: Long-lived API keys for dashboards.
  - Why: Integrations without needing a player session.
  - Impact: ops
  - (est): 3–6h

- [X] Backups & exports
  - What: Scheduled CSV/JSON market snapshots; restore tooling.
  - Why: Disaster recovery; analytics.
  - Impact: ops
  - (est): 3–6h

---

## Documentation & Developer Experience

- [x] API docs page
  - What: Inline `/docs` endpoint summarizing endpoints and payloads.
  - Why: Easier integrations.
  - Impact: maintainability
  - (est): 2–3h

- [ ] Example consumers
  - What: Minimal HTML/Node scripts demonstrating API usage.
  - Why: Faster adoption and testing.
  - Impact: maintainability
  - (est): 2–4h

- [x] CI checks
  - What: Lint/build on PR; bundle jar artifact.
  - Why: Guard rails.
  - Impact: maintainability, ops
  - (est): 2–4h

---

## Security & Privacy

- [X] Session hardening
  - What: Shorter expiry, IP binding option, revoke all sessions command.
  - Why: Reduce risk of leaked links.
  - Impact: security
  - (est): 2–4h
  - Additions: Shortened expiry but detects mouse movement and resets timer.

- [ ] HTTPS & reverse proxy guide
  - What: Nginx/Caddy examples for TLS and auth headers.
  - Why: Secure in production.
  - Impact: security, ops
  - (est): 2–4h

---

## Nice-to-Haves

- [X] Darker End dimension theme toggle
  - What: One-click darker palette variant.
  - Why: Vibe.
  - Impact: UX
  - (est): 1–2h

- [X] Keyboard shortcuts
  - What: Navigate items; adjust quantities; submit trades.
  - Why: Power-user speed.
  - Impact: UX
  - (est): 2–4h

- [X] Animations for trades
  - What: Subtle confetti/pulse on success.
  - Why: Delight.
  - Impact: UX
  - (est): 1–2h

---

If you check any item(s), I’ll prioritize them and start wiring changes immediately. Add notes inline if you have preferences (e.g., polling at 750ms, watchlist limit of 10, etc.).
