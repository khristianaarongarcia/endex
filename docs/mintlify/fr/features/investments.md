---
title: "Investissements"
description: "Systèmes d'investissement avancés"
---

# Investissements

Au-delà du simple achat/vente d'objets, The Endex propose des mécanismes d'investissement plus complexes pour les serveurs avancés.

## Fonds d'Investissement (ETF)

*Fonctionnalité à venir / En développement*

Permet aux joueurs d'investir dans des paniers d'objets (ex: "Fonds Minier" comprenant Fer, Or, Diamant, Charbon). La valeur du fonds suit la moyenne pondérée des objets sous-jacents.

## Dividendes

Si vous configurez des objets comme des "Actions" (virtuelles), vous pouvez configurer des dividendes périodiques.

```yaml
# Exemple dans items.yml
SERVER_SHARE:
  base-price: 1000.0
  dividend:
    enabled: true
    amount: 10.0 # Paye 10$ par action
    interval: 86400 # Toutes les 24h
```

Les joueurs qui possèdent cet objet dans leur [Portefeuille](/fr/features/holdings) recevront automatiquement de l'argent à chaque intervalle.

## Vente à Découvert (Short Selling)

*Fonctionnalité Avancée*

Permet aux joueurs de parier sur la **baisse** d'un prix.
1. Le joueur "emprunte" et vend un objet au prix actuel (ex: 100$).
2. Il doit le racheter plus tard pour rembourser sa dette.
3. Si le prix a baissé à 80$, il le rachète et garde la différence (20$ de profit).
4. Si le prix a monté à 120$, il perd 20$.

Cette fonctionnalité doit être activée explicitement car elle comporte des risques pour les joueurs inexpérimentés.
