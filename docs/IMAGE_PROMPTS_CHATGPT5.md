# The Endex — Image Prompt Pack for ChatGPT‑5 (Consistent Style)

This file provides ready‑to‑use image and short‑loop GIF prompts that match the current Spigot description. All assets should share the same style and theme so your page looks cohesive.

Use these verbatim or paste them as a starting point, then iterate with tiny tweaks only (keep the style guide locked).

---

## Global Style Guide (apply to ALL assets)

- Art direction: modern Minecraft‑inspired “End” (void) aesthetic, clean and game‑adjacent. Absolutely no copyrighted textures or logos; use original shapes/patterns.
- Palette:
  - Primary accent: Ender purple glow (#A36BFF ±) for active/positive elements
  - Secondary accent: prismatic amethyst/magenta (#C77DFF ±) for “Events & Shocks” emphasis
  - Background: deep void gradient (#06050A → #0C0814) with a faint starfield; panels #141126 with borders #2B2140
- Motifs: subtle square/pixel motifs, crystalline shards, floating “island” silhouettes and soft End‑like particles—non‑infringing and abstract.
- UI elements to reuse: 6×9 inventory grid feel, category row, search bar, sort toggles, item cards with 32px‑ish icons, details panel with tiny sparkline, and a “Last cycle: Demand X / Supply Y (Z%)” line.
- Lighting: ethereal purple rim light, gentle bloom on accents, shallow depth‑of‑field on background void.
- Text usage: keep text minimal and generic. Use short labels like “Market”, “Events”, “Buy”, “Sell”, “Search…”. No real chat logs.
- Composition: center‑weighted focal panel, generous breathing room; consistent corner radius, borders, and panel shadows.
- Output rules: no watermarks, no brand logos, and no direct Minecraft textures or Mojang IP.

Recommended outputs:
- Stills: PNG, 1920×1080 (or 1600×900)
- GIF loops: 6–8 seconds, < 15 MB; or MP4 short clips if you’ll convert to GIF later

---

## 1) Hero Banner (Top Image)

Use for the top banner where you currently show the project title image.

Prompt
```
Create a sleek hero banner for a Minecraft‑inspired economy plugin called “The Endex | Dynamic Market & Addons”, using an End‑themed void aesthetic.
Style: modern, dark UI with Ender purple glow accents (#A36BFF) and prismatic amethyst highlights (#C77DFF), subtle square/pixel motifs, and abstract crystalline shards. Background is a deep void gradient (#06050A → #0C0814) with a faint starfield and soft floating‑island silhouettes. Center a glossy panel with a 6×9 inventory‑like motif, soft shadows, and a tiny sparkline graphic.
Include small generic labels only (e.g., “Market”, “Events”, “Addons”), no real Minecraft textures or logos. Avoid clutter; keep the title readable and the panel as the focal point. No watermark.
Output: 1600×400 (or 1920×480) PNG.
```

---

## 2) Market GUI — Feature GIF (primary media)

Goal: show categories, search/sort, details panel with sparkline, and last‑cycle metrics. Keep it short and clear.

Loop storyboard (6–8s)
1. Idle view of the market grid with a highlighted item card.
2. Type into the search bar (letters animate in) and sort toggles change once.
3. Open details panel for the highlighted item: show tiny sparkline, “Last cycle: Demand 12 / Supply 7 ( +3.1% )”.
4. Quick “Buy” click with a subtle confirmation glow, then loop seamlessly back to idle.

Single‑prompt version
```
Design a short looping GIF (6–8s) of a modern, Minecraft‑inspired market GUI with an End‑themed style.
Consistent style: void background (#06050A → #0C0814), panels #141126 with #2B2140 borders, Ender purple glow accents (#A36BFF). 6×9 inventory‑like grid, top row with Categories, a Search field (“Search…”), and Sort toggles. Highlight one item card.
Animate: search text appears briefly, a sort toggle flips once, then a right panel slides in showing a tiny sparkline and the line: “Last cycle: Demand 12 / Supply 7 ( +3.1% )”. A quick “Buy” click produces a soft purple confirmation glow. No real Minecraft textures or brand logos. Minimal text; clean, readable UI.
Output: 1920×1080 GIF or MP4 (to be converted to GIF), loop seamlessly, < 15 MB.
```

Static screenshot variant
```
Create a PNG still frame of the market GUI in the same style and palette. Show grid, categories, search, sort toggles, a selected item card, and the details panel with a tiny sparkline and “Last cycle: Demand 12 / Supply 7 ( +3.1% )”. 1920×1080 PNG.
```

---

## 3) Events & Shocks — Feature GIF

Goal: convey multipliers (e.g., “Ore Rush”), broadcast start, and price boost in the UI.

Loop storyboard (6–8s)
1. Market idle on an ore‑like item (generic, non‑infringing icon).
2. A tasteful broadcast ribbon slides in: “Event started: Ore Rush”.
3. The item card shows a small gold “×2.0” badge; price nudges up with a soft glow.
4. Badge pulses once; ribbon fades; loop back to idle.

Single‑prompt version
```
Produce a short looping GIF (6–8s) showing “Events & Shocks” for a Minecraft‑inspired economy UI in an End‑themed style.
Style: same void theme with Ender purple primary accents and prismatic amethyst highlights for event elements. No real Minecraft textures.
Scene: market grid focused on an ore‑like item card. A subtle broadcast ribbon appears: “Event started: Ore Rush”. The item card displays a prismatic amethyst “×2.0” badge, and its price increases slightly with a gentle purple glow. The badge gives a single soft pulse, ribbon fades, and the scene returns to idle for a seamless loop.
Output: 1920×1080 GIF/MP4, < 15 MB, no watermark.
```

Static screenshots (2 options)
```
A) Before event: normal item card with neutral price, no badge.
B) During event: same framing, show gold “×2.0” badge and slightly higher price, with a subtle highlight.
1920×1080 PNG.
```

---

## 4) Addon Framework (Crypto Example) — Feature GIF

Goal: show a drop‑in addon UI (e.g., Crypto) with a quick buy and balance update. Keep it generic (no real coin logos).

Loop storyboard (6–8s)
1. Addon panel opens: tabs like “Shop”, “Balance”, “Transfer”.
2. In “Shop”, select a token card labeled “GEM”.
3. Click “Buy 1”, show a brief confirmation glow.
4. Switch to “Balance”: number increments slightly; loop back to addon panel idle.

Single‑prompt version
```
Create a short looping GIF (6–8s) demonstrating an addon module UI for a Minecraft‑inspired economy plugin in an End‑themed style.
Style: same dark void UI, Ender purple accents (#A36BFF), clean rounded panels. No real crypto logos; use a generic token named “GEM”.
Scene: addon panel with tabs “Shop”, “Balance”, “Transfer”. In Shop, a token card “GEM” is selected. A click on “Buy 1” triggers a soft End‑purple confirmation glow. Switch to Balance where the amount increases slightly. Minimal text, no brand marks, smooth loop.
Output: 1920×1080 GIF/MP4, < 15 MB.
```

Static screenshots (2 options)
```
A) Addon Shop open with the “GEM” card selected (highlight ring, clean pricing row).
B) Balance view after purchase with a small upward increment indicator.
1920×1080 PNG.
```

---

## 5) Optional: Web Dashboard Card (for docs/API section)

Prompt
```
Design a single PNG showcasing a web dashboard card for the plugin: a clean panel with a mini line chart, a list of recent price points, and a few square item icons (generic, non‑infringing). Style matches the End‑themed void palette, Ender purple accents, and soft borders. 1600×900 PNG.
```

---

## Consistency Checklist (use this before exporting)

- Colors match the palette and feel consistent across all assets.
- Panel shapes, borders, shadows, sparkline style look identical.
- Text is minimal, generic, and readable—no long sentences or chat logs.
- No real Minecraft textures, logos, or copyrighted assets.
- GIFs loop cleanly and stay under ~15 MB.

---

## How to Use These Prompts

1) Paste a prompt into ChatGPT‑5 (Vision/Image) and request the exact output size.
2) If the first result strays from style, start with the “Global Style Guide” paragraph as a system/style message, then paste the feature prompt.
3) For GIFs, you can request MP4 and convert to GIF with ezgif.com to shrink size.
4) Keep all assets in one theme; if you must change, only adjust tiny details (e.g., sparkline value or one label).

---

## Suggested Filenames

- hero_endex_banner.png
- market_gui_feature.gif  (and market_gui_still.png)
- events_shocks_feature.gif  (and events_shocks_before.png, events_shocks_during.png)
- addon_framework_feature.gif  (and addon_shop.png, addon_balance.png)
- web_dashboard_card.png
