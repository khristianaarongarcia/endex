---
title: "Comandos"
description: "Lista completa de comandos e uso"
---

# Comandos

Aqui está a lista completa de comandos disponíveis no The Endex.

## Comandos de Jogador

Estes comandos são geralmente acessíveis a todos os jogadores (dependendo das permissões).

| Comando | Alias | Descrição | Permissão |
|---------|-------|-----------|-----------|
| `/endex` | `/market` | Abre o menu principal do mercado. | `endex.use` |
| `/endex help` | | Mostra a ajuda. | `endex.help` |
| `/endex price <item>` | | Mostra o preço atual de um item. | `endex.price` |
| `/endex buy <item> <qtd>` | | Compra itens ao preço de mercado. | `endex.buy` |
| `/endex sell <item> <qtd>` | | Vende itens ao preço de mercado. | `endex.sell` |
| `/endex sellall` | | Vende tudo no inventário compatível. | `endex.sellall` |
| `/holdings` | `/portfolio` | Abre o menu de portfólio. | `endex.holdings` |
| `/endex invest <item> <qtd>` | | Compra holdings (virtual). | `endex.invest` |
| `/endex divest <item> <qtd>` | | Vende holdings (virtual). | `endex.divest` |

## Comandos de Administrador

Estes comandos são restritos a administradores e operadores.

| Comando | Descrição | Permissão |
|---------|-----------|-----------|
| `/endex admin reload` | Recarrega a configuração. | `endex.admin.reload` |
| `/endex admin additem <preço>` | Adiciona o item na mão ao mercado. | `endex.admin.additem` |
| `/endex admin removeitem <item>` | Remove um item do mercado. | `endex.admin.removeitem` |
| `/endex admin setprice <item> <preço>` | Define forçadamente o preço de um item. | `endex.admin.setprice` |
| `/endex admin setvolatility <item> <val>` | Altera a volatilidade de um item. | `endex.admin.setvolatility` |
| `/endex admin event start <evento>` | Inicia forçadamente um evento. | `endex.admin.event` |
| `/endex admin event stop` | Para qualquer evento em andamento. | `endex.admin.event` |
| `/endex admin debug` | Ativa o modo de depuração no console. | `endex.admin.debug` |

## Argumentos

- `<item>`: Nome interno do item (ex: `DIAMOND`, `IRON_INGOT`). Deve corresponder ao `items.yml`.
- `<qtd>`: Quantidade (número inteiro).
- `<preço>`: Preço (número decimal).
