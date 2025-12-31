---
title: "Livraison"
description: "Récupération physique des investissements virtuels"
---

# Système de Livraison

Le système de **Livraison** fait le pont entre le [Portefeuille Virtuel](/fr/features/holdings) et l'inventaire physique. Il permet aux joueurs de convertir leurs participations virtuelles en objets réels.

## Concept

Si un joueur possède 100 "Participations en Fer" et qu'il a besoin de fer pour construire, il peut demander une "Livraison".

1. Le joueur sélectionne les participations à retirer.
2. Les participations sont supprimées de son portefeuille virtuel.
3. Les objets physiques correspondants sont ajoutés à son inventaire.

## Commande

```bash
/endex deliver <item> <quantité>
```

Ou via le menu `/holdings` en cliquant sur l'icône de livraison (généralement un Minecart ou un Coffre).

## Frais de Livraison

Pour équilibrer l'économie et encourager le trading virtuel vs physique, vous pouvez configurer des **Frais de Livraison**.

```yaml
delivery:
  enabled: true
  fee-per-item: 0.0 # Coût fixe par item
  fee-percentage: 0.02 # 2% de la valeur actuelle de l'objet
```

Si des frais sont configurés, le joueur doit payer ce montant pour convertir ses actions en objets.

## Restrictions

- **Inventaire Plein** : Si l'inventaire du joueur est plein, la livraison sera annulée ou partielle (selon la configuration).
- **Objets Non-Livrables** : Vous pouvez configurer certains objets comme étant "purement virtuels" (non livrables) dans `items.yml` en mettant `deliverable: false`. Cela est utile pour des "Actions d'Entreprise" ou des indices boursiers qui n'existent pas physiquement.
