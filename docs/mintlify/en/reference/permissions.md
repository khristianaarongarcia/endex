------

title: "Permissions"title: "Permissions"

description: "Permissions used by The Endex and common server role setups."description: "Permissions used by The Endex and common server role setups."

------



Permission nodes can vary slightly by server version and addons, but these are the commonly used ones.Permission nodes can vary slightly by server version and addons, but these are the commonly used ones.



## Core## Core



| Permission | Default | What it does || Permission | What it does |

| --- | --- | --- || --- | --- |

| `theendex.market` | true | Access to the market GUI and basic commands || `theendex.admin` | Admin commands: reload, events control, diagnostics |

| `theendex.buy` | true | Allow buying items from the market |

| `theendex.sell` | true | Allow selling items to the market |## Web

| `theendex.invest` | true | Access to investment commands |

| `theendex.admin` | op | Admin commands: reload, events control, add/remove items, price management |These come from the config guide:



## Shop Editor| Permission | What it does |

| --- | --- |

| Permission | Default | What it does || `endex.web.trade` | Allows trading through the web dashboard |

| --- | --- | --- || `endex.web.admin` | Allows viewing other players’ holdings via admin API endpoints |

| `endex.shop.editor` | op | Access to the shop editor GUI (`/market editor`) |

| `endex.shop.admin` | op | Admin actions in custom shops (shift-click edit items) |## Addons



## Web DashboardAddon permissions usually follow a pattern like:



These control access to the web dashboard features:- `endex.addon.<name>`

- `endex.addon.<name>.admin`

| Permission | Default | What it does |

| --- | --- | --- |If you’re writing an addon, define your permissions in your addon plugin.yml and document them for server owners.

| `theendex.web` | true | Access to generate web session links |
| `endex.web.trade` | true | Allows trading (buy/sell) through the web dashboard |
| `endex.web.admin` | op | Allows viewing other players' holdings via admin API endpoints |

## Addons

Addon permissions usually follow a pattern like:

- `theendex.crypto.*` - Crypto addon permissions
- `endex.addon.<name>` - Custom addon permissions
- `endex.addon.<name>.admin` - Custom addon admin permissions

If you're writing an addon, define your permissions in your addon plugin.yml and document them for server owners.
