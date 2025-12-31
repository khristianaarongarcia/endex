---
title: "Démarrage Rapide"
description: "Configurez votre première économie de marché en quelques minutes"
---

# Démarrage Rapide

Ce guide vous aidera à configurer votre première économie dynamique avec The Endex.

## 1. Ajouter des Objets au Marché

Pour qu'un objet ait un prix dynamique, il doit être ajouté à The Endex.

### En Jeu

Tenez l'objet que vous souhaitez ajouter dans votre main principale et tapez :

```bash
/endex admin additem <prix_initial>
```

Par exemple, pour ajouter un Diamant avec un prix de base de 100 :

```bash
/endex admin additem 100
```

### Via Configuration

Vous pouvez également ajouter des objets manuellement dans `items.yml` :

```yaml
DIAMOND:
  base-price: 100.0
  min-price: 10.0
  max-price: 1000.0
  volatility: 0.5
```

## 2. Créer un Panneau de Marché (Optionnel)

Si vous utilisez des panneaux pour afficher les prix :

1. Placez un panneau.
2. Sur la première ligne, écrivez `[Endex]`.
3. Sur la deuxième ligne, écrivez le nom de l'objet (ex: `DIAMOND`).

Le panneau se mettra à jour automatiquement avec le prix actuel.

## 3. Ouvrir le Menu GUI

Les joueurs peuvent voir tous les objets du marché et leurs tendances via le menu principal :

```bash
/market
```

## 4. Simuler des Transactions

Pour tester la fluctuation des prix, vous pouvez simuler des achats et des ventes :

- **Acheter** fait monter le prix (Demande augmente).
- **Vendre** fait baisser le prix (Offre augmente).

Essayez d'acheter plusieurs stacks d'un objet et observez le prix changer !

## Prochaines Étapes

Maintenant que les bases sont en place, explorez les fonctionnalités avancées :

- [Configurer les événements de marché](/fr/features/events)
- [Configurer le tableau de bord web](/fr/web-api/dashboard)
- [Comprendre le système de participations](/fr/features/holdings)
