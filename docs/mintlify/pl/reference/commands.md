---
title: "Komendy"
description: "Komendy gracza i administratora dla The Endex."
---

Ta strona wymienia typowe komendy udostępniane przez The Endex.

## Komendy gracza

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

## Komendy administratora

Uprawnienia zazwyczaj wymagają `theendex.admin`.

```text
/endex reload
/endex version
/endex track dump
/market event list
/market event <name>
/market event end <name>
/market event clear
```

## Uwagi

- Większość komend obsługuje uzupełnianie tabulatorem dla materiałów.
- Po edycji `config.yml`, `events.yml` lub plików danych rynkowych, użyj `/endex reload`.

## Dodatki

Komendy dodatków zazwyczaj rejestrują się pod:

```text
/endex <addon> <subcommand> [args]
```

Przykład (dodatek Crypto):

```text
/endex crypto help
/endex crypto buy <amount>
/endex crypto sell <amount>
```
