---
title: "Loja Personalizada"
description: "Integração com plugins de loja existentes"
---

# Loja Personalizada

O The Endex foi projetado para funcionar como um motor de preços, não necessariamente para substituir completamente sua loja existente.

## Integração GUI

Embora o The Endex forneça sua própria Interface Gráfica do Usuário (GUI) para visualizar preços e tendências, você pode querer integrá-lo com outros sistemas para transações físicas ou permitir compra/venda direta via comandos.

### Menu Principal

O comando `/market` abre o menu principal onde os jogadores podem:

- Ver todos os itens listados.
- Ver o preço atual, mudança percentual (24h) e tendência (subindo/descendo).
- Acessar seu portfólio (holdings).

## Criando Lojas Físicas

Você pode criar lojas físicas que usam os preços do The Endex.

### Placas (Signs)

O The Endex suporta placas de comércio dinâmicas.

**Formato de Criação:**
1. `[Endex]`
2. `BUY` ou `SELL`
3. `<Nome do Item>`
4. `<Quantidade>`

**Exemplo:**
```text
[Endex]
BUY
DIAMOND
1
```

Isso criará uma placa onde os jogadores podem comprar 1 Diamante pelo preço atual de mercado. O preço exibido na placa será atualizado automaticamente.

### Integração com Citizens (NPCs)

Se você tiver o plugin **Citizens**, pode criar comerciantes.

```bash
/npc create Trader --type villager
/trait endex-trader
```

*(Nota: Este recurso pode variar dependendo da versão do The Endex e addons instalados)*

## Comandos de Compra/Venda Rápida

Os jogadores também podem usar comandos para negociar rapidamente sem a GUI:

- `/endex buy <item> <quantidade>`
- `/endex sell <item> <quantidade>`
- `/endex sellall`: Vende tudo no inventário que for compatível com o mercado.

## Personalizando Mensagens

Todas as mensagens relacionadas a transações (sucesso, fundos insuficientes, inventário cheio) podem ser configuradas no `messages.yml`.
