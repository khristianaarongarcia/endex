---
title: "Événements de Marché"
description: "Événements aléatoires et programmés affectant l'économie"
---

# Événements de Marché

Les événements de marché introduisent de l'imprévisibilité et de l'excitation dans l'économie. Ils peuvent causer des hausses soudaines de prix (Bull Market) ou des effondrements (Bear Market/Crash).

## Types d'Événements

### 1. Krach Boursier (Market Crash)
Tous les prix ou un groupe spécifique d'objets chutent drastiquement.
- **Cause** : Panique, surproduction, ou aléatoire.
- **Effet** : Les prix baissent de X%.

### 2. Hausse du Marché (Market Boom)
Les prix augmentent rapidement.
- **Cause** : Pénurie, forte demande, ou aléatoire.
- **Effet** : Les prix montent de X%.

### 3. Événements Spécifiques
Affectent un seul objet ou une catégorie.
- *Exemple : "Découverte d'une nouvelle mine d'or !" -> Le prix de l'or chute.*
- *Exemple : "Mauvaise récolte de blé !" -> Le prix du pain augmente.*

## Configuration des Événements

Les événements sont configurés dans `events.yml`.

```yaml
events:
  gold_rush:
    display-name: "Ruée vers l'Or"
    message: "&6Une nouvelle veine d'or a été découverte ! Les prix de l'or chutent !"
    items: [GOLD_INGOT, GOLD_BLOCK]
    price-multiplier: 0.5 # Les prix sont divisés par 2
    duration: 3600 # Dure 1 heure
    chance: 0.01 # 1% de chance par cycle
```

## Déclenchement

### Automatique
Le plugin vérifie périodiquement (configurable) si un événement doit se déclencher en fonction de sa probabilité (`chance`).

### Manuel (Admin)
Les administrateurs peuvent forcer un événement :

```bash
/endex admin event start <nom_event>
```

## Notifications

Quand un événement commence, une annonce peut être faite :
- Dans le chat.
- Via la BossBar.
- Via un Title à l'écran.

Cela alerte les joueurs qu'il y a une opportunité de trading.
