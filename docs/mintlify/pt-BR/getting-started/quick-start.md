---
title: "Início Rápido"
description: "Configure sua primeira economia de mercado em minutos"
---

# Início Rápido

Este guia ajudará você a configurar sua primeira economia dinâmica usando o The Endex.

## 1. Adicionar Itens ao Mercado

Para que um item tenha um preço dinâmico, ele deve ser adicionado ao The Endex.

### No Jogo

Segure o item que deseja adicionar na sua mão principal e digite:

```bash
/endex admin additem <preço_inicial>
```

Por exemplo, para adicionar Diamante com um preço base de 100:

```bash
/endex admin additem 100
```

### Via Configuração

Você também pode adicionar itens manualmente no `items.yml`:

```yaml
DIAMOND:
  base-price: 100.0
  min-price: 10.0
  max-price: 1000.0
  volatility: 0.5
```

## 2. Criar Placas de Mercado (Opcional)

Se você quiser usar placas para exibir preços:

1. Coloque uma placa.
2. Escreva `[Endex]` na primeira linha.
3. Escreva o nome do item (ex: `DIAMOND`) na segunda linha.

A placa será atualizada automaticamente com o preço atual.

## 3. Abrir o Menu GUI

Os jogadores podem ver todos os itens do mercado e suas tendências no menu principal:

```bash
/market
```

## 4. Simular Negociações

Para testar a flutuação de preços, você pode simular compras e vendas:

- **Comprar** aumentará o preço (aumento da demanda).
- **Vender** diminuirá o preço (aumento da oferta).

Tente comprar alguns packs de um item e observe como o preço muda!

## Próximos Passos

Agora que você tem o básico, explore recursos mais avançados:

- [Configurar Eventos de Mercado](/pt-BR/features/events)
- [Configurar o Painel Web](/pt-BR/web-api/dashboard)
- [Entender o Sistema de Holdings](/pt-BR/features/holdings)
