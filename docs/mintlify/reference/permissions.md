---
title: "Permissions"
description: "Permissions used by The Endex and common server role setups."
---

# Permissions

Permission nodes can vary slightly by server version and addons, but these are the commonly used ones.

## Core

| Permission | What it does |
| --- | --- |
| `theendex.admin` | Admin commands: reload, events control, diagnostics |

## Web

These come from the config guide:

| Permission | What it does |
| --- | --- |
| `endex.web.trade` | Allows trading through the web dashboard |
| `endex.web.admin` | Allows viewing other players’ holdings via admin API endpoints |

## Addons

Addon permissions usually follow a pattern like:

- `endex.addon.<name>`
- `endex.addon.<name>.admin`

If you’re writing an addon, define your permissions in your addon plugin.yml and document them for server owners.
