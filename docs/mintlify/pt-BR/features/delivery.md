---
title: "Entrega"
description: "Retirada física de investimentos virtuais"
---

# Sistema de Entrega

O **Sistema de Entrega** atua como uma ponte entre o [Portfólio Virtual](/pt-BR/features/holdings) e o inventário físico. Ele permite que os jogadores convertam suas participações virtuais em itens reais.

## O Conceito

Se um jogador possui 100 "Participações em Ferro" e precisa de ferro para construir, ele pode solicitar uma "entrega".

1. O jogador seleciona a holding para retirar.
2. As participações são removidas do portfólio virtual.
3. Os itens físicos correspondentes são adicionados ao inventário.

## Comandos

```bash
/endex deliver <item> <quantidade>
```

Ou clique no ícone de entrega (geralmente um carrinho de mina ou baú) no menu `/holdings`.

## Taxas de Entrega

Para equilibrar a economia e gerenciar a negociação virtual vs. física, você pode configurar uma **Taxa de Entrega**.

```yaml
delivery:
  enabled: true
  fee-per-item: 0.0 # Custo fixo por item
  fee-percentage: 0.02 # 2% do valor atual do item
```

Se uma taxa for definida, o jogador deve pagar esse valor para converter suas ações em itens.

## Restrições

- **Inventário Cheio**: Se o inventário do jogador estiver cheio, a entrega será cancelada ou apenas parcialmente cumprida (dependendo da configuração).
- **Itens Não Entregáveis**: Você pode definir certos itens como "puramente virtuais" (não entregáveis) definindo `deliverable: false` no `items.yml`. Isso é útil para "Ações da Empresa" ou índices de mercado que não existem fisicamente.
