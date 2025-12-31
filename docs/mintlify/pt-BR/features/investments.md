---
title: "Investimentos"
description: "Sistemas avançados de investimento"
---

# Investimentos

Além da simples compra e venda de itens, o The Endex oferece mecânicas de investimento mais complexas para servidores avançados.

## ETFs (Exchange-Traded Funds)

*Em Breve / Recurso em Desenvolvimento*

Permite que os jogadores invistam em uma cesta de itens (ex: "Fundo de Mineração" contendo Ferro, Ouro, Diamante, Carvão). O valor do fundo segue a média ponderada dos itens subjacentes.

## Dividendos

Se você configurar um item para agir como uma "Ação" (virtual), pode configurar pagamentos regulares de dividendos.

```yaml
# Exemplo no items.yml
SERVER_SHARE:
  base-price: 1000.0
  dividend:
    enabled: true
    amount: 10.0 # Paga $10 por ação
    interval: 86400 # A cada 24 horas
```

Jogadores que possuem este item em seu [Portfólio](/pt-BR/features/holdings) receberão dinheiro automaticamente a cada intervalo.

## Venda a Descoberto (Short Selling)

*Recurso Avançado*

Permite que os jogadores apostem na queda do preço.
1. O jogador "pega emprestado" um item e o vende pelo preço atual (ex: $100).
2. Ele deve comprá-lo de volta mais tarde para pagar a dívida.
3. Se o preço cair para $80, ele compra de volta e fica com a diferença ($20 de lucro).
4. Se o preço subir para $120, ele perde $20.

Isso deve ser ativado explicitamente, pois pode ser arriscado para jogadores inexperientes.
