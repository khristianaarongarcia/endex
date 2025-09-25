# Custom Web UI Override

The Endex now supports serving a fully custom web UI so server owners can tailor the front‑end (HTML/CSS/JS) without recompiling the plugin.

## Quick Start
1. In `config.yml`, set:
```yaml
web:
  custom:
    enabled: true
    root: webui            # default folder name under plugins/TheEndex/
    reload: false          # set true while actively editing (no caching)
    export-default: true   # export embedded UI on first run if missing
```
2. Restart or `/endex reload`.
3. A folder will be created at: `plugins/TheEndex/webui/`.
4. If `index.html` did not exist and `export-default: true`, the built‑in UI snapshot is written there.
5. Edit `index.html` (add your CSS/JS/images in the same folder). Refresh the browser.

## Commands
| Command | Description |
|---------|-------------|
| `/endex webui` | Shows help for webui subcommands |
| `/endex webui export` | Force re‑export (overwrite) the embedded UI to `index.html` |
| `/endex webui reload` | Clears cache & forces next request to re-read `index.html` |

(Admin permission `theendex.admin` required.)

## How It Works
- When `web.custom.enabled = true` the plugin checks `<dataFolder>/<root>/index.html`.
- If present, it is served instead of the embedded UI.
- All files inside that folder become directly accessible (static serving) — so you can split scripts into `app.js`, styles into `styles.css`, add images, etc.
- If `reload = false`, the file is cached by lastModified timestamp to avoid disk I/O every request.
- Setting `reload = true` or running `/endex webui reload` clears the cache for fast iteration.

## Best Practices
- Keep a backup before using `/endex webui export` (it overwrites `index.html`).
- Use relative paths for scripts and styles: `<script src="app.js"></script>` if `app.js` is placed beside `index.html`.
- Avoid hotlinking Chart.js if you want offline use; you can download and place it locally (e.g., `chart.min.js`).
- Don’t remove the `session` query param logic; the UI must still read `?session=...` to authenticate.

## Security Notes
- Only players with generated session tokens (or configured API tokens) can hit API endpoints.
- Static files are served as-is; DO NOT place secrets, config backups, or server logs inside the custom webui folder.
- Avoid adding forms or scripts that POST to unvalidated external endpoints.

## Updating the Plugin
- Updating The Endex may add new features to the embedded UI. Your exported `index.html` will not auto‑merge changes.
- To incorporate upstream changes: temporarily rename your `index.html`, run `/endex webui export`, diff the new export with your version, then re-apply customizations.

## Roadmap Ideas (Not Yet Implemented)
- Token placeholders (e.g., `{{SERVER_NAME}}`) auto-replaced server-side.
- Automatic hot-reload via WebSocket notification when files change.
- Split export mode: separate `styles.css` and `app.js` for easier overrides.
- Partial override: only replace a specific section while inheriting core logic.

## Troubleshooting
| Symptom | Cause | Fix |
|---------|-------|-----|
| Browser shows built-in UI | `web.custom.enabled` false or `index.html` missing & export disabled | Enable or export default UI |
| Changes not appearing | Caching on | Set `reload: true` or run `/endex webui reload` |
| 403 error | Invalid/expired session token | Get a new link via `/endex web` |
| Missing styles/scripts | Wrong relative path | Use relative paths from `index.html` root |

---
Happy customizing!
