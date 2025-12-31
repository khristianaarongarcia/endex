---
title: "Commandes"
description: "Liste complète des commandes et leur utilisation"
---

# Commandes

Voici la liste complète des commandes disponibles dans The Endex.

## Commandes Joueur

Ces commandes sont généralement accessibles à tous les joueurs (selon les permissions).

| Commande | Alias | Description | Permission |
|----------|-------|-------------|------------|
| `/endex` | `/market` | Ouvre le menu principal du marché. | `endex.use` |
| `/endex help` | | Affiche l'aide. | `endex.help` |
| `/endex price <item>` | | Affiche le prix actuel d'un objet. | `endex.price` |
| `/endex buy <item> <qte>` | | Achète un objet au prix du marché. | `endex.buy` |
| `/endex sell <item> <qte>` | | Vend un objet au prix du marché. | `endex.sell` |
| `/endex sellall` | | Vend tout l'inventaire compatible. | `endex.sellall` |
| `/holdings` | `/portfolio` | Ouvre le menu du portefeuille. | `endex.holdings` |
| `/endex invest <item> <qte>` | | Achète des participations (virtuel). | `endex.invest` |
| `/endex divest <item> <qte>` | | Vend des participations (virtuel). | `endex.divest` |

## Commandes Admin

Ces commandes sont réservées aux administrateurs et opérateurs.

| Commande | Description | Permission |
|----------|-------------|------------|
| `/endex admin reload` | Recharge la configuration. | `endex.admin.reload` |
| `/endex admin additem <prix>` | Ajoute l'objet en main au marché. | `endex.admin.additem` |
| `/endex admin removeitem <item>` | Retire un objet du marché. | `endex.admin.removeitem` |
| `/endex admin setprice <item> <prix>` | Force le prix d'un objet. | `endex.admin.setprice` |
| `/endex admin setvolatility <item> <val>` | Change la volatilité d'un objet. | `endex.admin.setvolatility` |
| `/endex admin event start <event>` | Force le démarrage d'un événement. | `endex.admin.event` |
| `/endex admin event stop` | Arrête l'événement en cours. | `endex.admin.event` |
| `/endex admin debug` | Active le mode debug dans la console. | `endex.admin.debug` |

## Arguments

- `<item>` : Le nom interne de l'objet (ex: `DIAMOND`, `IRON_INGOT`). Doit correspondre à `items.yml`.
- `<qte>` : La quantité (entier).
- `<prix>` : Le prix (nombre décimal).
