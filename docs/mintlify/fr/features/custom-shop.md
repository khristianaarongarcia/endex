---
title: "Boutique Personnalisée"
description: "Intégration avec les plugins de boutique existants"
---

# Boutique Personnalisée

The Endex est conçu pour fonctionner comme un moteur de prix pour vos boutiques existantes, plutôt que de les remplacer entièrement.

## Intégration GUI

The Endex fournit sa propre interface graphique (GUI) pour voir les prix et les tendances, mais pour les transactions physiques, il s'intègre souvent avec d'autres systèmes ou permet l'achat/vente direct via commandes.

### Menu Principal

La commande `/market` ouvre le menu principal où les joueurs peuvent :

- Voir tous les objets listés.
- Voir le prix actuel, le changement en pourcentage (24h), et la tendance (Haut/Bas).
- Accéder à leurs portefeuilles (Holdings).

## Création de Boutiques Physiques

Vous pouvez créer des boutiques physiques utilisant les prix de The Endex.

### Panneaux (Signs)

The Endex supporte les panneaux de commerce dynamiques.

**Format de Création :**
1. `[Endex]`
2. `BUY` ou `SELL`
3. `<Nom_Item>`
4. `<Quantité>`

**Exemple :**
```text
[Endex]
BUY
DIAMOND
1
```

Cela créera un panneau qui permet aux joueurs d'acheter 1 Diamant au prix actuel du marché. Le prix affiché sur le panneau se mettra à jour automatiquement.

### Intégration Citizens (NPC)

Si vous avez le plugin **Citizens**, vous pouvez créer des traits de commerçant.

```bash
/npc create Marchand --type villager
/trait endex-trader
```

*(Note: Cette fonctionnalité dépend de la version de The Endex et des addons installés)*

## Commandes d'Achat/Vente Rapides

Les joueurs peuvent aussi utiliser des commandes pour commercer rapidement sans GUI :

- `/endex buy <item> <quantité>`
- `/endex sell <item> <quantité>`
- `/endex sellall` : Vend tout l'inventaire compatible au marché.

## Personnalisation des Messages

Tous les messages liés aux transactions (Succès, Fonds insuffisants, Inventaire plein) sont configurables dans `messages.yml`.
