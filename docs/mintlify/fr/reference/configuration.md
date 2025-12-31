---
title: "Configuration"
description: "Guide détaillé des fichiers de configuration"
---

# Configuration

The Endex est hautement configurable via ses fichiers YAML situés dans `plugins/TheEndex/`.

## config.yml

Le fichier principal pour les paramètres globaux.

```yaml
# Paramètres Généraux
plugin-prefix: "&8[&5The Endex&8] "
language: fr # Langue (en, fr, es, de, etc.)
debug-mode: false

# Économie
economy:
  currency-symbol: "$"
  format: "#,##0.00"
  
# Paramètres du Marché
market:
  update-interval: 60 # Secondes entre les mises à jour de prix
  save-interval: 300 # Secondes entre les sauvegardes auto
  
# Système de Portefeuille
holdings:
  enabled: true
  tax-rate: 0.05

# Web Server (Tableau de Bord)
web:
  enabled: true
  port: 8080
  host: "0.0.0.0"
```

## items.yml

Définit tous les objets échangeables et leurs propriétés.

```yaml
DIAMOND:
  material: DIAMOND # Matériau Minecraft
  display-name: "&bDiamant" # Nom d'affichage (Optionnel)
  base-price: 100.0
  min-price: 10.0
  max-price: 500.0
  volatility: 0.5
  stock: 1000 # Stock virtuel initial
  elasticity: 1.0 # Sensibilité au changement de stock
  
GOLD_INGOT:
  material: GOLD_INGOT
  base-price: 50.0
  volatility: 0.3
```

## events.yml

Configure les événements de marché aléatoires.

```yaml
events:
  market_crash:
    display-name: "&cKrach Boursier"
    chance: 0.005
    duration: 1200
    global-multiplier: 0.7 # Tous les prix x0.7
    
  gold_rush:
    display-name: "&6Ruée vers l'Or"
    items: [GOLD_INGOT, GOLD_BLOCK]
    price-multiplier: 0.5
```

## messages.yml

Contient tous les textes affichés aux joueurs. Vous pouvez traduire ou personnaliser les messages ici. Supporte les codes couleurs (`&` ou MiniMessage selon la version).

```yaml
prefix: "&8[&5The Endex&8] "
buy-success: "&aVous avez acheté &e%amount%x %item% &apour &e%price%&a."
sell-success: "&aVous avez vendu &e%amount%x %item% &apour &e%price%&a."
insufficient-funds: "&cVous n'avez pas assez d'argent."
```
