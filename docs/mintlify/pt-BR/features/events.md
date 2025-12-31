---
title: "Eventos de Mercado"
description: "Eventos aleatórios e planejados que afetam a economia"
---

# Eventos de Mercado

Eventos de mercado adicionam imprevisibilidade e emoção à economia. Eles podem causar picos repentinos de preços (Bull Market) ou quedas (Bear Market/Crash).

## Tipos de Eventos

### 1. Colapso do Mercado (Market Crash)
Uma queda significativa nos preços de todos os itens ou de um grupo específico.
- **Causa**: Pânico, superprodução ou aleatório.
- **Efeito**: Preços caem X%.

### 2. Boom do Mercado (Market Boom)
Um aumento rápido nos preços.
- **Causa**: Escassez, alta demanda ou aleatório.
- **Efeito**: Preços sobem X%.

### 3. Eventos Específicos
Afetam um único item ou categoria.
- *Ex: "Nova veia de ouro descoberta!" -> Preço do Ouro cai.*
- *Ex: "Praga nas plantações de trigo!" -> Preço do Pão sobe.*

## Configurando Eventos

Eventos são configurados no `events.yml`.

```yaml
events:
  gold_rush:
    display-name: "Corrida do Ouro"
    message: "&6Uma nova veia de ouro foi encontrada! O preço do ouro está caindo!"
    items: [GOLD_INGOT, GOLD_BLOCK]
    price-multiplier: 0.5 # Preço cortado pela metade
    duration: 3600 # Dura 1 hora
    chance: 0.01 # 1% de chance por ciclo
```

## Gatilhos

### Automático
O plugin verifica periodicamente (configurável) se deve acionar um evento com base na `chance`.

### Manual (Admin)
Administradores podem forçar o início de um evento:

```bash
/endex admin event start <nome_do_evento>
```

## Notificações

Quando um evento começa, você pode anunciar:
- No chat.
- Via BossBar.
- Via Título na tela.

Isso alerta os jogadores sobre oportunidades de negociação.
