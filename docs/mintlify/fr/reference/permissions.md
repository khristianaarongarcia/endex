---
title: "Permissions"
description: "Liste des nœuds de permission"
---

# Permissions

Gérez l'accès aux fonctionnalités de The Endex avec ces permissions.

## Permissions Utilisateur

Ces permissions sont recommandées pour les joueurs par défaut.

| Permission | Description | Recommandé |
|------------|-------------|------------|
| `endex.use` | Permet d'utiliser le plugin et d'ouvrir le GUI. | **Oui** |
| `endex.help` | Permet de voir l'aide. | **Oui** |
| `endex.price` | Permet de vérifier les prix. | **Oui** |
| `endex.buy` | Permet d'acheter des objets. | **Oui** |
| `endex.sell` | Permet de vendre des objets. | **Oui** |
| `endex.sellall` | Permet d'utiliser `/endex sellall`. | **Oui** |
| `endex.holdings` | Permet d'accéder au portefeuille. | **Oui** |
| `endex.invest` | Permet d'investir (acheter holdings). | **Oui** |
| `endex.divest` | Permet de désinvestir (vendre holdings). | **Oui** |
| `endex.history` | Permet de voir l'historique des prix. | **Oui** |

## Permissions Administrateur

⚠️ **Attention** : Ne donnez ces permissions qu'aux administrateurs de confiance.

| Permission | Description |
|------------|-------------|
| `endex.admin.*` | Donne toutes les permissions admin. |
| `endex.admin.reload` | Permet de recharger la config. |
| `endex.admin.additem` | Permet d'ajouter des objets au marché. |
| `endex.admin.removeitem` | Permet de retirer des objets. |
| `endex.admin.setprice` | Permet de manipuler les prix. |
| `endex.admin.event` | Permet de lancer/arrêter des événements. |
| `endex.admin.debug` | Permet de voir les infos de debug. |
| `endex.admin.update` | Reçoit les notifications de mise à jour. |

## Permissions Spéciales

| Permission | Description |
|------------|-------------|
| `endex.bypass.tax` | Le joueur ne paie pas de taxes sur les holdings. |
| `endex.bypass.limit` | Le joueur n'a pas de limite de holdings. |
