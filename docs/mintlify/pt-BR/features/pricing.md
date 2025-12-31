---
title: "Preços Dinâmicos"
description: "Como funciona o sistema de preços dinâmicos"
---

# Sistema de Preços Dinâmicos

O núcleo do The Endex é seu algoritmo de preços dinâmicos, que reage à oferta e demanda em tempo real.

## O Conceito

Ao contrário das lojas tradicionais onde os preços são fixos (estáticos), o The Endex ajusta os preços automaticamente:

- **Se os jogadores compram**, o estoque (teórico) diminui e a demanda aumenta -> **O preço sobe**.
- **Se os jogadores vendem**, o estoque aumenta e a oferta aumenta -> **O preço cai**.

Isso cria uma economia viva onde os jogadores podem lucrar comprando na baixa e vendendo na alta.

## O Algoritmo de Preços

O preço é calculado usando uma fórmula que leva em consideração:

1. **Preço Base**: O ponto de partida definido pelos administradores.
2. **Volatilidade**: Quão rápido o preço muda.
3. **Volume**: Quantos itens estão sendo negociados.
4. **Limites**: Preços mínimos e máximos para evitar inflação/deflação extrema.

### Fórmula Simplificada

$$
P_{novo} = P_{atual} \times (1 + \frac{Volume \times Volatilidade}{Constante})
$$

## Configurando a Volatilidade

Cada item pode ter sua própria volatilidade no `items.yml`.

- **Baixa Volatilidade (0.1 - 0.3)**: Preços estáveis, mudam lentamente. Bom para recursos básicos (pedra, madeira).
- **Média Volatilidade (0.4 - 0.7)**: Flutuação padrão. Bom para minérios ou drops de mobs.
- **Alta Volatilidade (0.8 - 1.5)**: Preços muito instáveis, alto risco e alta recompensa. Bom para itens raros.

```yaml
GOLD_INGOT:
  base-price: 50.0
  volatility: 0.5  # Volatilidade média
```

## Limites de Preço

Para proteger sua economia, você pode definir limites rígidos:

- `min-price`: O preço nunca cairá abaixo disso.
- `max-price`: O preço nunca excederá isso.

```yaml
DIAMOND:
  base-price: 100.0
  min-price: 20.0   # Nunca abaixo de 20
  max-price: 500.0  # Nunca acima de 500
```

## Regeneração de Preço

Você pode configurar os preços para retornarem lentamente ao preço base ao longo do tempo se não houver atividade. Isso evita que os preços fiquem presos em extremos indefinidamente.

Ative isso no `config.yml` em `price-regeneration`.
