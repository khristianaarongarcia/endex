---
title: "Prix Dynamiques"
description: "Comment fonctionne le système de prix dynamique"
---

# Système de Prix Dynamiques

Le cœur de The Endex est son algorithme de prix dynamique qui réagit à l'offre et à la demande en temps réel.

## Le Concept

Contrairement aux magasins traditionnels où les prix sont fixes (statiques), The Endex ajuste les prix automatiquement :

- **Quand les joueurs achètent**, le stock diminue (théoriquement) et la demande augmente -> **Le prix monte**.
- **Quand les joueurs vendent**, le stock augmente et l'offre augmente -> **Le prix descend**.

Cela crée une économie vivante où les joueurs peuvent faire du profit en achetant bas et en vendant haut.

## Algorithme de Prix

Le prix est calculé en utilisant une formule qui prend en compte :

1. **Prix de Base** : Le point de départ défini par l'administrateur.
2. **Volatilité** : À quelle vitesse le prix change.
3. **Volume de Transaction** : Combien d'objets sont échangés.
4. **Limites** : Prix minimum et maximum pour éviter l'inflation/déflation extrême.

### Formule Simplifiée

$$
P_{nouveau} = P_{actuel} \times (1 + \frac{Volume \times Volatilité}{Constante})
$$

## Configuration de la Volatilité

Chaque objet peut avoir sa propre volatilité dans `items.yml`.

- **Volatilité Faible (0.1 - 0.3)** : Prix stables, changent lentement. Bon pour les ressources de base (Pierre, Bois).
- **Volatilité Moyenne (0.4 - 0.7)** : Fluctuations normales. Bon pour les minerais et drops de mobs.
- **Volatilité Élevée (0.8 - 1.5)** : Prix très instables, haut risque/haute récompense. Bon pour les objets rares.

```yaml
GOLD_INGOT:
  base-price: 50.0
  volatility: 0.5  # Volatilité moyenne
```

## Limites de Prix

Pour protéger votre économie, vous pouvez définir des limites strictes :

- `min-price` : Le prix ne descendra jamais en dessous de cette valeur.
- `max-price` : Le prix ne montera jamais au-dessus de cette valeur.

```yaml
DIAMOND:
  base-price: 100.0
  min-price: 20.0   # Ne va jamais en dessous de 20
  max-price: 500.0  # Ne va jamais au-dessus de 500
```

## Régénération des Prix

Vous pouvez configurer les prix pour qu'ils reviennent lentement vers leur prix de base au fil du temps s'il n'y a pas d'activité. Cela empêche les prix de rester bloqués à des extrêmes indéfiniment.

Activez ceci dans `config.yml` sous `price-regeneration`.
