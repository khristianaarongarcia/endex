---
title: "Permissões"
description: "Lista de nós de permissão"
---

# Permissões

Use estas permissões para gerenciar o acesso aos recursos do The Endex.

## Permissões de Usuário

Estas são recomendadas para jogadores por padrão.

| Permissão | Descrição | Recomendado |
|-----------|-----------|-------------|
| `endex.use` | Permite usar o plugin e abrir GUIs. | **Sim** |
| `endex.help` | Permite ver a ajuda. | **Sim** |
| `endex.price` | Permite verificar preços. | **Sim** |
| `endex.buy` | Permite comprar itens. | **Sim** |
| `endex.sell` | Permite vender itens. | **Sim** |
| `endex.sellall` | Permite usar `/endex sellall`. | **Sim** |
| `endex.holdings` | Permite acessar o portfólio. | **Sim** |
| `endex.invest` | Permite investir (comprar holdings). | **Sim** |
| `endex.divest` | Permite desinvestir (vender holdings). | **Sim** |
| `endex.history` | Permite ver histórico de preços. | **Sim** |

## Permissões de Administrador

⚠️ **Aviso**: Dê estas permissões apenas para administradores confiáveis.

| Permissão | Descrição |
|-----------|-----------|
| `endex.admin.*` | Concede todas as permissões de admin. |
| `endex.admin.reload` | Permite recarregar a configuração. |
| `endex.admin.additem` | Permite adicionar itens ao mercado. |
| `endex.admin.removeitem` | Permite remover itens. |
| `endex.admin.setprice` | Permite manipular preços. |
| `endex.admin.event` | Permite iniciar/parar eventos. |
| `endex.admin.debug` | Permite ver informações de depuração. |
| `endex.admin.update` | Recebe notificações de atualização. |

## Permissões Especiais

| Permissão | Descrição |
|-----------|-----------|
| `endex.bypass.tax` | O jogador não paga impostos sobre holdings. |
| `endex.bypass.limit` | O jogador não tem limite de holdings. |
