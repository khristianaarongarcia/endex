<p>
  <img src="https://i.imgur.com/onDbDSW.png" alt="The Endex Banner" />
</p>

<h1>The Endex &vert; Dynamic Market &amp; Addons <span style="font-size:0.8em;">[1.20.1 &ndash; 1.21.8]</span></h1>

<p>Bring a living economy to your server. Prices move with player demand and supply, backed by a slick Market GUI, timed events, an optional web dashboard, delivery queue safeguards, and an addon framework.</p>

<h2>What&rsquo;s new in 1.4.0</h2>
<ul>
  <li><strong>Virtual Delivery System:</strong> Overflow purchases now head into a per-player pending queue backed by SQLite. Claim everything from the GUI&rsquo;s Ender Chest badge or the new <code>/market delivery</code> commands, with FIFO ordering, optional auto-claim on login, and configurable per-player caps.</li>
  <li><strong>Delivery Commands:</strong> <code>/market delivery list|claim|claim-all|gui</code> works in-game or from console, making remote administration painless.</li>
  <li><strong>Web &amp; API Coverage:</strong> Added <code>/api/deliveries</code> endpoints and live dashboard badges so players can track pending items from the browser.</li>
  <li><strong>Buy Loop Fix:</strong> Resolved the long-standing issue where buying 64 items occasionally yielded just one on 1.20.1 servers. The loop now allocates fresh stacks each cycle and safely routes overflow.</li>
  <li><strong>Safety &amp; Integrity:</strong> Delivery storage uses atomic SQLite transactions with graceful fallback to avoid duplication exploits or limit bypasses.</li>
</ul>

<h3>Highlights from 1.3.1</h3>
<ul>
  <li>Orders now check inventory capacity before charging players; overflow is capped (or delivered) instead of dropping on the ground.</li>
  <li>Players receive clear messaging when an order is capped, and the shared <code>calculateInventoryCapacity()</code> helper powers both command and web flows.</li>
</ul>

<h3>Highlights from 1.3.0</h3>
<ul>
  <li>Removed reflective access for tighter security and cleaner internal APIs.</li>
  <li>Session tokens migrate into the Authorization header, scrubbing <code>?session=</code> from URLs automatically.</li>
  <li>Support for SHA-256 hashed API tokens via <code>web.api.token-hashes</code>, plus refreshed documentation.</li>
</ul>

<h2>Feature Overview</h2>
<ul>
  <li><strong>Dynamic Pricing:</strong> Demand/supply curves with configurable sensitivity, EMA smoothing, per-item clamps, and optional inventory pressure that reacts to online player stock.</li>
  <li><strong>Market GUI:</strong> Categories, search, sorting, quick-amount buttons, and a detailed panel with ASCII sparkline and impact estimates. Preferences persist per player.</li>
  <li><strong>Delivery Queue:</strong> Claim pending items through GUI buttons or commands; delivery storage lives in <code>deliveries.db</code> with FIFO ordering.</li>
  <li><strong>Events &amp; Shocks:</strong> Time-boxed multipliers with broadcasts and persistence across restarts.</li>
  <li><strong>Investments:</strong> Buy, list, and redeem APR-based certificates entirely in-game.</li>
  <li><strong>Web Dashboard (Optional):</strong> REST + WS/SSE live updates, resource-pack icons, Combined Holdings (Invest + live inventory), and customizable overrides via <code>web.custom.*</code>.</li>
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
  <li>Delivery manager ensures overflow items are stored safely instead of dropping to the ground.</li>
</ul>

<h2>Commands</h2>
<pre><code>/endex help
/endex version

/market
/market buy &lt;item&gt; &lt;amount&gt;
/market sell &lt;item&gt; &lt;amount&gt;
/market price &lt;item&gt;
/market top

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
  <li><code>theendex.invest</code> &mdash; default: true</li>
  <li><code>theendex.admin</code> &mdash; default: op</li>
  <li>Crypto addon: <code>theendex.crypto.*</code> (info, balance, buy, sell, transfer, shop, admin)</li>
</ul>

<h2>Configuration Highlights</h2>
<ul>
  <li>Versioned config with automated migration on startup/reload.</li>
  <li>Adjustable price tick interval, sensitivity, history length, autosave frequency, taxes, blacklists, and event caps.</li>
  <li>Toggle delivery system with <code>delivery.enabled</code>, <code>delivery.auto-claim-on-login</code>, and <code>delivery.max-pending-per-player</code>.</li>
  <li>Choose YAML or SQLite storage; SQLite is recommended for delivery tracking.</li>
</ul>

<pre><code class="language-yaml">price-inventory:
  enabled: true
  sensitivity: 0.02
  per-player-baseline: 64
  max-impact-percent: 10.0

delivery:
  enabled: true
  auto-claim-on-login: false
  max-pending-per-player: 100000

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
  <li>Server: Paper/Spigot 1.20.1 through 1.21.8 (built against Paper API 1.20.1).</li>
  <li>Java 17 runtime.</li>
  <li>Vault (soft dependency) for economy operations.</li>
</ul>

<h2>Data &amp; Reliability</h2>
<ul>
  <li>YAML storage by default; SQLite recommended for delivery queues and web metrics.</li>
  <li>Periodic backups, safe reloads, and atomic CSV exports keep data consistent.</li>
  <li>Delivery database operations wrap in try/catch with rollback to maintain integrity.</li>
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
