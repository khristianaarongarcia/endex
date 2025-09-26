# Security Audit Summary (2025-09-26)

This document summarizes a manual security-oriented review of the main Endex plugin codebase (v1.2.0) and the bundled Crypto addon structure. It addresses a Spigot review comment suggesting "AI‑generated" structure and potential vulnerabilities.

## 1. Reviewer Comment About `kotlin/coroutines/jvm/internal/ModuleNameRetriever` / `kotlin/io/FilesKt__UtilsKt`
Those class names are **internal Kotlin standard library implementation artifacts** automatically included (because the plugin shades the Kotlin stdlib). They are not evidence of AI generation and are not vulnerabilities. Any Kotlin project that shades/ships the stdlib will contain similar internal classes in the JAR. Removing them would break runtime behavior.

## 2. Overall Risk Posture
| Area | Rating | Notes |
|------|--------|-------|
| Remote Code Execution | Low | No dynamic script eval; addons are locally supplied JARs (trusted operator). |
| Deserialization / Injection | Low | Jackson only deserializes small internal DTOs; SQL uses prepared statements. |
| Path Traversal / File IO | Low/Medium | Custom UI export & resource pack unzip constrained to plugin data folder; add canonical path check (recommended) & network download size/time limits. |
| Reflection Abuse | Low | (Mitigated in 1.3.0) Reflection removed; explicit getters now used. |
| Denial of Service (DoS) | Medium | Each trade opens a fresh SQLite connection; potential high-frequency overhead; rate limiting keyed by session only (in-memory unbounded map). |
| Information Disclosure | Low | Market & price data are intended public for authenticated sessions; SSE feed (if enabled) unauthenticated—acceptable but document it. |
| Permissions / Auth | Low/Medium | (Partially mitigated in 1.3.0) Session tokens moved to Authorization header post-load; URL stripped. API tokens now support hashed digests (`web.api.token-hashes`); plain list deprecated. |
| Concurrency / Thread Safety | Medium | Economy ops properly on main thread, but synchronous SQLite operations during buy/sell also run on main thread (latency risk). |
| Supply Chain (Dependencies) | Medium | Jackson 2.15.2 & Javalin 5.6.1 have newer versions available with fixes; keep updated. |

## 3. Positive Findings
* SQL interactions use prepared statements (no obvious injection vectors).
* History exporting and backups use atomic temp file writes.
* Session tokens are 128-bit (UUID hex) – acceptable entropy.
* Rate limiting present for API endpoints (per-session window) to mitigate abuse.
* No dynamic code eval / scripting / runtime compilation.

## 4. Identified Issues & Suggested Fixes

### 4.1 Reflection Access to Internals (Mitigated in 1.3.0)
Locations: `WebServer.kt` (`marketManager.db`), `EndexCommand.kt`, `EndexTabCompleter.kt`, `AddonCommandRouter.kt`, Crypto addon.
Risks: Future maintenance fragility; potential accidental exposure if other plugins gain reflective access pattern; slightly raises review skepticism.
Remediation:
1. Add explicit public (or internal) getters in `MarketManager` (e.g., `fun sqliteStore(): SqliteStore?`).
2. Expose needed fields via narrow interfaces instead of reflection.
3. Remove `setAccessible(true)` usage from commands and use those getters.

### 4.2 Web Session Token in URL (Mitigated in 1.3.0)
Risk: URL query parameter may leak in browser history or via outbound link `Referer` headers.
Remediation:
1. Support alternate header `Authorization: Bearer <session>` for the SPA after initial load.
2. On first load, JS moves token from query to `sessionStorage` and rewrites history (`history.replaceState`) to strip it.
3. Optionally short session lifetimes + silent refresh.

### 4.3 API Tokens (Static List → Hashed Support Added in 1.3.0)
Risk: Configured plaintext tokens; compromise of config file = reuse.
Remediation:
1. Hash tokens in config (store SHA-256 digests) and compare hashed input.
2. Support token rotation (multiple active digests, with expiration timestamp).

### 4.4 Addon JAR Loading
Risk: Any JAR placed in `plugins/TheEndex/addons` executes arbitrary code (expected but should be explicit). No isolation.
Remediation:
1. Document trust model (`SECURITY.md` / README).
2. Optionally allow an `addons/allowlist.json` (list of accepted addon IDs & SHA-256 hashes) before loading.
3. Consider signature verification in future.

### 4.5 SQLite Usage per Operation
Risk: Each trade/holding update opens a fresh connection on main thread (latency spikes under load).
Remediation:
1. Introduce a small connection pool (Hikari) or a single long-lived connection with synchronized access.
2. Move heavy DB writes (history append, trades insert) to async queue processed on a background thread, then schedule price recalculation back on main thread.

### 4.6 Resource Pack Download (URL)
Risk: Operator-configurable arbitrary URL fetch (potential SSRF / large file).
Remediation:
1. Impose max download size (e.g., 32 MB) and timeouts (connect/read ≤ 10s).
2. Restrict allowed schemes to https (unless explicitly overridden by config flag).
3. Canonical path validation after unzip (reject entries escaping base dir).

### 4.7 Static File Custom UI
Current: Serves from `<dataFolder>/<customRoot>`; minimal checks.
Remediation:
1. Validate that `customRootDir.canonicalPath.startsWith(dataFolder.canonicalPath)` to prevent traversal via symlinks.
2. Optionally serve with restrictive `Content-Security-Policy` header (disallow external scripts except Chart.js CDN or self-host).

### 4.8 SSE Endpoint (Public)
Risk: Minor: Exposes price ticks without auth.
Remediation: Acceptable, but document. Optionally require a read-only token.

### 4.9 Rate Limiter Map Growth
Risk: Unbounded `requestCounts` map (one entry per session token ever created until server restart) – potential memory increase.
Remediation: Periodic cleanup of entries whose lists become empty or inactive > window * N.

### 4.10 Jackson Configuration
Risk: Uses default mapper; though not enabling polymorphic typing (good), enabling safe guards would help.
Remediation: `objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).enable(MapperFeature.BLOCK_UNSAFE_POLYMORPHIC_BASE_TYPES)` (or upgrade to latest). Avoid enabling default typing.

## 5. Dependency Review (High Level)
| Dependency | Current | Note |
|------------|---------|------|
| Kotlin Stdlib | 2.2.20-RC | Use stable release when available (avoid RC in production). |
| Javalin | 5.6.1 | Update to latest 5.x for security/bug fixes. |
| Jackson Databind | 2.15.2 | Multiple CVEs fixed in 2.16/2.17; upgrade recommended. |
| sqlite-jdbc | 3.46.0.0 | Current (as of mid‑2025) – keep updated. |
| Vault API | 1.7.1 | Stable. |

## 6. Recommended Immediate Hardening Steps
1. Replace reflection with explicit getters / service methods (eliminate `getDeclaredField` usage).
2. Strip session token from URL after initial load; move to header.
3. Add canonical path & size/time limits for resource pack download + unzip traversal guard.
4. Add periodic cleanup for `requestCounts` map.
5. Upgrade Jackson & Javalin; pin stable Kotlin version.
6. Add README/SECURITY note clarifying addon trust model.

## 7. Optional Medium-Term Improvements
* Introduce an async persistence queue (batch writes every X seconds) to reduce connection churn.
* Support HTTPS binding or behind a reverse proxy with TLS only instructions.
* Add structured audit log entries (JSON) for admin actions & addon loading.
* Implement simple HMAC-based signed export of history if external analytics consume it.
* Provide configuration checksum at startup for tamper evidence.

## 8. No Issues Found In
* SQL injection (all dynamic values parameterized).
* Arbitrary command execution (no `Runtime.exec` / `ProcessBuilder`).
* Dangerous deserialization features (no default typing / polymorphic subtypes enabled).

## 9. Disclaimer
This audit is static (code inspection) and does not include fuzzing, runtime profiling, or dependency CVE database automation. Re-run after major refactors or dependency upgrades.

---
Last updated: 2025-09-26 (v1.3.0 security hardening applied: reflection removal, session header auth, hashed API tokens)
