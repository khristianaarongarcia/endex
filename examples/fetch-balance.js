// Minimal Node script: node fetch-balance.js
// Usage: set ENDEx_TOKEN env var to API token or provide ?session= from in-game.
import fetch from 'node-fetch';

const base = 'http://localhost:3434';
const token = process.env.ENDEX_TOKEN;
(async () => {
  if (!token) {
    console.error('Set ENDEX_TOKEN environment variable');
    process.exit(1);
  }
  const r = await fetch(`${base}/api/balance`, { headers: { Authorization: `Bearer ${token}` } });
  console.log(await r.json());
})();
