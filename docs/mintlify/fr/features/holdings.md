---
title: "Portefeuille (Holdings)"
description: "Système de trading virtuel et d'investissement"
---

# Système de Portefeuille (Holdings)

Le système de **Holdings** (Participations/Portefeuille) permet aux joueurs d'investir dans des objets sans avoir à les stocker physiquement dans leur inventaire. C'est similaire à l'achat d'actions ou de contrats à terme dans le monde réel.

## Comment ça Marche

Au lieu d'acheter un "Diamant" physique, un joueur achète une "Participation en Diamant".

1. Le joueur paie le prix actuel du marché.
2. La participation est enregistrée dans son portefeuille virtuel.
3. L'objet physique n'est **pas** donné au joueur.
4. Plus tard, le joueur peut vendre sa participation au nouveau prix du marché.

### Avantages

- **Pas de Gestion d'Inventaire** : Les joueurs peuvent échanger des milliers d'unités sans avoir besoin de coffres.
- **Spéculation Pure** : Idéal pour les joueurs qui veulent jouer au trader sans grinder les ressources.
- **Protection** : Les participations ne peuvent pas être volées, brûlées ou perdues en mourant.

## Commandes de Portefeuille

- `/holdings` : Ouvre le GUI du portefeuille pour voir ses investissements.
- `/endex invest <item> <quantité>` : Achète des participations.
- `/endex divest <item> <quantité>` : Vend des participations.

## Configuration

Vous pouvez activer ou désactiver ce système dans `config.yml` :

```yaml
holdings:
  enabled: true
  max-holdings-per-item: 10000 # Limite par joueur
  tax-rate: 0.05 # Taxe de 5% sur les profits de holdings
```

## Taxes sur les Plus-values

Pour éviter que l'économie ne gonfle trop vite grâce à la spéculation, vous pouvez imposer une taxe spécifique sur les ventes de participations (`tax-rate`). Cette taxe est déduite du profit ou du total de la vente lors de la liquidation des participations.
