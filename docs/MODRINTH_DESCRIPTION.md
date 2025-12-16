<p>
  <img src="https://i.imgur.com/onDbDSW.png" alt="The Endex Banner" />
</p>

<h1>The Endex &vert; Dynamic Market &amp; Addons <span style="font-size:0.8em;">[1.20.1 &ndash; 1.21.x]</span></h1>

<p>Bring a living economy to your server. Prices move with player demand and supply, backed by a slick Market GUI, timed events, an optional web dashboard, virtual holdings system, and an addon framework.</p>

<h2>What&rsquo;s new in 1.5.1 — World Storage Scanner</h2>
<ul>
  <li><strong>Global Item Tracking:</strong> Prices now react to ALL items stored on your server — chests, barrels, shulker boxes, and more! True server-wide scarcity economics.</li>
  <li><strong>Anti-Manipulation Protection:</strong> Per-chunk item caps, per-material limits, and suspicious activity logging prevent storage farm exploits.</li>
  <li><strong>TPS-Aware Throttling:</strong> Scanner automatically skips if server is under load (configurable threshold, default 18.0 TPS).</li>
  <li><strong>Smart Scanning:</strong> Double chest deduplication, nested shulker contents, and configurable container types.</li>
  <li><strong>World Exclusions:</strong> Skip creative, minigame, or spawn worlds from affecting prices.</li>
</ul>

<h3>Highlights from 1.5.0</h3>
<ul>
  <li><strong>Virtual Holdings System:</strong> Complete redesign! Items purchased now go into virtual holdings instead of your inventory. Withdraw when you&rsquo;re ready with full P/L tracking.</li>
  <li><strong>Holdings GUI:</strong> Beautiful new panel showing all your holdings with quantity, avg cost, current price, and profit/loss. Left-click to withdraw all, right-click for one stack.</li>
  <li><strong>Holdings Commands:</strong> <code>/market holdings</code> to view, <code>/market withdraw &lt;item&gt; [amount]</code> to claim items to inventory.</li>
  <li><strong>Web UI Withdraw:</strong> Withdraw buttons (📤) on each holding row plus &ldquo;Withdraw All&rdquo; button. Real-time notifications on success.</li>
  <li><strong>Minecraft 1.21 Support:</strong> Full compatibility with Minecraft 1.21.x servers.</li>
</ul>

<h3>Highlights from 1.4.0</h3>
<ul>
  <li><strong>Virtual Delivery System:</strong> Overflow purchases now head into a per-player pending queue backed by SQLite. Claim everything from the GUI&rsquo;s Ender Chest badge or the new <code>/market delivery</code> commands, with FIFO ordering, optional auto-claim on login, and configurable per-player caps.</li>
  <li><strong>Buy Loop Fix:</strong> Resolved the long-standing issue where buying 64 items occasionally yielded just one on 1.20.1 servers.</li>
</ul>

<h2>Feature Overview</h2>
<ul>
  <li><strong>Dynamic Pricing:</strong> Demand/supply curves with configurable sensitivity, EMA smoothing, per-item clamps, and optional inventory pressure that reacts to online player stock.</li>
  <li><strong>World Storage Scanner:</strong> Prices adapt to global item quantities across ALL server storage (chests, barrels, shulkers). Above-baseline = price drops, scarce = price rises. Anti-manipulation caps prevent exploits.</li>
  <li><strong>Market GUI:</strong> Categories, search, sorting, quick-amount buttons, and a detailed panel with ASCII sparkline and impact estimates. Preferences persist per player.</li>
  <li><strong>Virtual Holdings:</strong> Buy items into virtual storage with P/L tracking. Withdraw to inventory when ready. Configurable global limits.</li>
  <li><strong>Delivery Queue:</strong> Backup system for overflow items when holdings are full.</li>
  <li><strong>Events &amp; Shocks:</strong> Time-boxed multipliers with broadcasts and persistence across restarts.</li>
  <li><strong>Investments:</strong> Buy, list, and redeem APR-based certificates entirely in-game.</li>
  <li><strong>Web Dashboard (Optional):</strong> REST + WS/SSE live updates, resource-pack icons, withdraw buttons, and customizable overrides via <code>web.custom.*</code>.</li>
  <li><strong>Addon Framework:</strong> Drop-in addons gain command routing, aliases, and tab completion. The bundled Crypto addon ships with a YAML-defined shop and analytics endpoints.</li>
  <li><strong>Resource Tracking:</strong> Optional server-wide counters for gathered materials, exportable via CSV.</li>
</ul>

<p>
  <img src="https://i.imgur.com/2NVDOxj.gif" alt="Market GUI GIF" />
</p>
<p>
  <img src="https://i.imgur.com/SY0ZO4F.png" alt="Market GUI Screenshot 1" />
  <img src="https://i.imgur.com/fdWGBho.png" alt="Market GUI Screenshot 2" />
</p>
<p>
  <img src="https://i.imgur.com/Qa5Hrhw.gif" alt="Events GIF" />
</p>
<p>
  <img src="https://i.imgur.com/2hXIQfx.gif" alt="Web Dashboard GIF" />
</p>

<h2>Gameplay &amp; Mechanics</h2>
<ul>
  <li>Price ticks use a sensitivity-based formula that considers recent demand/supply and optional inventory pressure.</li>
  <li>Prices respect per-item minimum and maximum clamps; demand/supply counters reset each cycle.</li>
  <li>Events apply temporary multipliers to effective prices, stacking up to configurable caps.</li>
  <li>Purchases go to virtual holdings first; withdraw to inventory when ready.</li>
  <li>Holdings track average cost basis for accurate profit/loss calculations.</li>
</ul>

<h2>Commands</h2>
<pre><code>/endex help
/endex version

/market
/market buy &lt;item&gt; &lt;amount&gt;
/market sell &lt;item&gt; &lt;amount&gt;
/market price &lt;item&gt;
/market top

/market holdings
/market withdraw &lt;item&gt; [amount]
/market withdraw all

/market invest buy &lt;item&gt; &lt;amount&gt;
/market invest list
/market invest redeem-all

/market delivery list
/market delivery claim &lt;item&gt; [amount]
/market delivery claim-all
/market delivery gui

/market event list
/market event &lt;name&gt;
/market event end &lt;name&gt;
/market event clear
</code></pre>

<h2>Permissions</h2>
<ul>
  <li><code>theendex.market</code> &mdash; default: true</li>
  <li><code>theendex.buy</code> &mdash; default: true</li>
  <li><code>theendex.sell</code> &mdash; default: true</li>
  <li><code>theendex.holdings</code> &mdash; default: true</li>
  <li><code>theendex.withdraw</code> &mdash; default: true</li>
  <li><code>theendex.invest</code> &mdash; default: true</li>
  <li><code>theendex.admin</code> &mdash; default: op</li>
  <li>Crypto addon: <code>theendex.crypto.*</code> (info, balance, buy, sell, transfer, shop, admin)</li>
</ul>

<h2>Configuration Highlights</h2>
<ul>
  <li>Versioned config with automated migration on startup/reload.</li>
  <li>Adjustable price tick interval, sensitivity, history length, autosave frequency, taxes, blacklists, and event caps.</li>
  <li>Toggle holdings system with <code>holdings.enabled</code>, <code>holdings.max-per-player</code>, and <code>holdings.mode</code>.</li>
  <li>World Storage Scanner with anti-manipulation protection.</li>
  <li>Choose YAML or SQLite storage; SQLite is recommended for holdings tracking.</li>
</ul>

<pre><code class="language-yaml">holdings:
  enabled: true
  max-per-player: 10000
  max-materials-per-player: 100
  mode: VIRTUAL

# NEW: World Storage Scanner
price-world-storage:
  enabled: true
  scan-interval-seconds: 300
  sensitivity: 0.01
  global-baseline: 1000
  containers:
    chests: true
    barrels: true
    shulker-boxes: true
  anti-manipulation:
    per-chunk-item-cap: 10000
    per-material-chunk-cap: 5000
    min-tps: 18.0

price-inventory:
  enabled: true
  sensitivity: 0.02
  per-player-baseline: 64
  max-impact-percent: 10.0

web:
  roles:
    default: TRADER
    trader-permission: endex.web.trade
    admin-view-permission: endex.web.admin
  holdings:
    inventory:
      enabled: true
      include-enderchest: false
      cache-seconds: 15
</code></pre>

<h2>Compatibility &amp; Requirements</h2>
<ul>
  <li>Server: Paper/Spigot 1.20.1 through 1.21.x (built against Paper API 1.21).</li>
  <li>Java 17 runtime.</li>
  <li>Vault (soft dependency) for economy operations.</li>
</ul>

<h2>Data &amp; Reliability</h2>
<ul>
  <li>YAML storage by default; SQLite recommended for holdings and web metrics.</li>
  <li>Periodic backups, safe reloads, and atomic CSV exports keep data consistent.</li>
  <li>Holdings operations use atomic transactions to prevent duplication exploits.</li>
</ul>

<h2>Support</h2>
<p>For questions, feedback, or bug reports, open a discussion and include the output of <code>/endex version</code> plus relevant logs.</p>

<p style="text-align:center;">
  <a href="https://discord.gg/ujFRXksUBE">
    <img src="https://i.postimg.cc/5tz22qFS/discord-icon-png-0-1.jpg" alt="Join our Discord!" width="64" height="64" />
  </a>
  <br />
  Join our Discord!
</p>
