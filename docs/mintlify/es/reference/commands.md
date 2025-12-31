---
title: "Comandos"
description: "Comandos para jugadores y administradores de The Endex."
---

Esta página lista los comandos comunes expuestos por The Endex.

## Comandos de Jugador

```text
/endex
/endex market
/market buy <material> <cantidad>
/market sell <material> <cantidad>
/market price <material>
/market top
/market invest buy <cantidad>
/market invest list
/market invest redeem-all
```

## Comandos de Administrador

Los permisos típicamente requieren `theendex.admin`.

```text
/endex reload
/endex version
/endex track dump
/market event list
/market event <nombre>
/market event end <nombre>
/market event clear
```

## Notas

- La mayoría de los comandos soportan autocompletado Tab para materiales.
- Usa `/endex reload` después de editar `config.yml`, `events.yml`, o archivos de datos del mercado.

## Complementos del Plugin

Los complementos típicamente registran comandos bajo:

```text
/endex <addon> <subcomando> [args]
```

Ejemplo (Complemento Crypto):

```text
/endex crypto help
/endex crypto buy <cantidad>
/endex crypto sell <cantidad>
```
