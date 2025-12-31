---
title: "PlaceholderAPI"
description: "Usando placeholders do The Endex"
---

# PlaceholderAPI

O The Endex suporta totalmente a **PlaceholderAPI** (PAPI), permitindo que você exiba informações de mercado em qualquer lugar (Scoreboards, Chat, Tablist, Hologramas, menus DeluxeMenus, etc.).

## Instalação

Certifique-se de ter:
1. PlaceholderAPI instalado.
2. The Endex instalado.
3. Execute `/papi ecloud download TheEndex` (se disponível na ecloud) ou simplesmente `/papi reload` já que o The Endex registra seus próprios placeholders.

## Lista de Placeholders

Substitua `<item>` pelo ID do item (ex: `DIAMOND`, `OAK_LOG`).

### Placeholders de Item

| Placeholder | Descrição |
|-------------|-----------|
| `%endex_price_<item>%` | Preço atual do item. |
| `%endex_price_formatted_<item>%` | Preço formatado com moeda (ex: $100.50). |
| `%endex_change_<item>%` | Mudança percentual (ex: +5.2%). |
| `%endex_trend_<item>%` | Ícone de tendência (ex: 📈 ou 📉). |
| `%endex_base_price_<item>%` | Preço base configurado. |
| `%endex_volume_<item>%` | Volume de negociação atual. |

### Placeholders de Jogador

| Placeholder | Descrição |
|-------------|-----------|
| `%endex_holdings_value%` | Valor total do portfólio do jogador. |
| `%endex_holdings_count_<item>%` | Quantidade de um item específico mantido. |
| `%endex_profit_loss%` | Lucro/Prejuízo total do jogador (P&L). |

### Placeholders Globais

| Placeholder | Descrição |
|-------------|-----------|
| `%endex_top_gainer%` | Nome do item com maior ganho. |
| `%endex_top_loser%` | Nome do item com maior perda. |
| `%endex_market_status%` | Status do mercado (Aberto/Fechado/Colapso). |

## Exemplos de Uso

**Em um Scoreboard:**
```yaml
lines:
  - "&6Mercado:"
  - " Diamante: %endex_price_formatted_DIAMOND% %endex_trend_DIAMOND%"
  - " Ouro: %endex_price_formatted_GOLD_INGOT% %endex_trend_GOLD_INGOT%"
  - "&ePortfólio: $%endex_holdings_value%"
```

**Em um Holograma:**
```text
Preço do Bitcoin (Virtual)
%endex_price_formatted_BTC%
```
