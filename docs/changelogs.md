# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]
*No unreleased changes at this time.*

## [1.3.0] - 2025-09-26
### Security / Hardening
- Removed all reflective private field access (replaced with explicit getters and helper methods) to improve transparency and reduce maintenance risk.
- Web session auth now migrates `?session=` token into `Authorization: Bearer <session>` header; SPA strips token from the URL (prevents accidental leakage via history/referrer).
- Added hashed API token support (`web.api.token-hashes`) using SHA-256 lowercase hex digests with constant-time comparison.
- Plain `web.api.tokens` still accepted for backward compatibility; one-time deprecation warning when hashes also configured.

### Added
- `web.api.token-hashes` config key (secure alternative to storing plaintext tokens).
- CHANGELOG.md (top-level) and updated SECURITY.md marking mitigated items.

### Changed
- Token validation logic centralized with constant-time equality; improved auth code clarity.

### Upgrade Notes
1. Existing session links continue to work. After first load, the UI removes the token from the URL automatically.
2. To migrate API tokens: compute SHA-256 of the token and place digest under `web.api.token-hashes`; then remove the clear token from `web.api.tokens`.
3. No breaking config changes; all keys retained. Migration is optional but recommended.

### Next (Planned, Not Included)
- Resource pack download hardening (size/time limits, traversal checks, HTTPS default).
- Rate limiter map cleanup task.
- Dependency upgrades (stable Kotlin, Jackson 2.17.x, latest Javalin 5.x).

## [1.2.0] - 2025-09-25
### Added
- Custom Web UI override (`web.custom.*`): external `index.html` & static assets, auto-export starter file, optional reload bypass.
- Admin commands: `/endex webui export`, `/endex webui reload` for managing exported UI.
- Unified web market view with advanced filters (category, price bounds, trend, sort fields & direction) and collapsible persistent filter panel.
- Category auto-population & improved watchlist/group toggles.

### Changed
- Removed legacy Addons tab and `/api/addons` usage; addons now integrate as items or custom routes.
- Simplified live update flow (single item stream via WS/SSE/polling).
- Refactored layout (two-row search + filters) for clarity and responsiveness.
- Copy tasks marked untracked to prevent Gradle state errors with transient DB journal files.

### Fixed
- UI tab reversion issue resolved by removing tab abstraction.
- Async misuse (`await` in sync render) eliminated; stabilized frontend rendering.
- Ensured Vault economy deductions occur on main thread; corrected SELL handling logic.

### Removed
- Addons navigation UI and related frontend logic.

### Documentation
- Added `docs/CUSTOM_WEBUI.md` and updated config with `web.custom.*` keys.

### Compatibility
- Built against Paper API 1.20.1; expected to operate on 1.21.x.

### Upgrade Notes
- If you relied on Addons tab navigation, migrate to item-based representation or custom web routes.
- Enable `web.custom.enabled` to customize UI; restart to export default scaffold.

## [1.0.0] - 2025-08-28
Initial release

### Added
- Demand-driven dynamic pricing with clamping and rolling history
- Full-featured market GUI (pagination, sorting, category filters, text search, details view)
- Vault economy integration with configurable transaction tax
- Market events system (stackable multipliers with cap, broadcasts, persistence across restarts)
- Investments (buy, list, redeem-all)
- YAML storage (default) and optional SQLite store with automatic seeding from YAML
- Player preference persistence (amount, sort, category, search, last page)
- ASCII sparkline trends and CSV history export
- Config versioning and automated migration on startup/reload
- Admin commands, safe reload, item blacklist, and CSV export command
- /endex version command to report plugin version and storage mode

### Compatibility
- Server: Paper/Spigot 1.20.1 to latest (built against API 1.20.1)
- Java 17 runtime
- Economy: Vault (soft dependency)

### Notes
- On Paper, the SQLite driver is fetched automatically via `plugin.yml` libraries (smaller plugin JAR). On plain Spigot, use YAML storage by default or install the SQLite driver manually if enabling SQLite.

## [1.1.0] - 2025-08-29
### Added
- Web UI item icons served from resource packs with `/icon/{material}` endpoint (public, ETag caching)
- Resource pack extraction/usage via `web.icons.*` with support for both `item/items` and `block/blocks` folders
- Aliases for tricky materials (e.g., PUMPKIN, CACTUS) to reduce 404s across pack variations
- Addons tab in the UI and `/api/addons` endpoint (auth required)
- Developer HTTP API documentation (`docs/API.md`) and reverse proxy guide (`docs/REVERSE_PROXY.md`)

### Changed
- Improved client error fallback for missing icons (initials badge)
- More informative server logs (actual paths tried for missing icons when `logging.verbose=true`)

### Fixed
- Kotlin string interpolation in logs and headers (no longer prints `$vars` literally)
- Javalin route syntax for path params (now uses `/icon/{material}`)

### Compatibility
- Built against Paper API 1.20.1; expected to work on newer Paper versions

### Highlights
- HTTP API reference: see `docs/API.md`
- Reverse proxy examples (HTTPS): `docs/REVERSE_PROXY.md`
- Config keys for icons and addons: `docs/CONFIG.md`
