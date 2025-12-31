---
title: "Commands"
description: "Player and admin commands for The Endex."
---

This page lists the common commands exposed by The Endex.

## Player commands

```text
/endex
/endex market
/market buy <material> <amount>
/market sell <material> <amount>
/market price <material>
/market top
/market invest buy <amount>
/market invest list
/market invest redeem-all
```

## Admin commands

Permissions typically require `theendex.admin`.

```text
/endex reload
/endex version
/endex track dump
/market event list
/market event <name>
/market event end <name>
/market event clear
```

## Notes

- Most commands support tab completion for materials.
- After editing `config.yml`, `events.yml`, or market data files, use `/endex reload`.

## Addons

Addon commands usually register under:

```text
/endex <addon> <subcommand> [args]
```

Example (Crypto addon):

```text
/endex crypto help
/endex crypto buy <amount>
/endex crypto sell <amount>
```
