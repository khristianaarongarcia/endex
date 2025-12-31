---
title: "Holdings (Portfólio)"
description: "Sistema de negociação virtual e investimentos"
---

# Sistema de Holdings (Portfólio)

O sistema de **Holdings** (Portfólio) permite que os jogadores invistam em itens sem precisar armazená-los fisicamente em seus inventários. Isso é semelhante a comprar ações ou contratos futuros no mundo real.

## Como Funciona

Em vez de comprar um "Diamante" físico, o jogador compra uma "Participação em Diamante".

1. O jogador paga o preço atual de mercado.
2. A participação é armazenada em seu portfólio virtual.
3. Nenhum item físico é dado ao jogador.
4. Mais tarde, o jogador pode vender a participação pelo novo preço de mercado.

### Benefícios

- **Sem Gestão de Inventário**: Jogadores podem negociar milhares de unidades sem precisar de baús.
- **Especulação Pura**: Ótimo para jogadores que querem atuar como traders sem coletar recursos.
- **Proteção**: Holdings não podem ser roubadas, queimadas ou perdidas na morte.

## Comandos de Portfólio

- `/holdings`: Abre a GUI do portfólio para ver seus investimentos.
- `/endex invest <item> <quantidade>`: Compra holdings.
- `/endex divest <item> <quantidade>`: Vende holdings.

## Configuração

Você pode ativar ou desativar este sistema no `config.yml`:

```yaml
holdings:
  enabled: true
  max-holdings-per-item: 10000 # Limite por jogador
  tax-rate: 0.05 # 5% de imposto sobre lucros de holdings
```

## Imposto sobre Ganhos de Capital

Para evitar que a economia infle muito rapidamente devido à especulação, você pode impor um imposto (`tax-rate`) sobre a venda de holdings. Este imposto é deduzido do lucro ou do total ao vender.
