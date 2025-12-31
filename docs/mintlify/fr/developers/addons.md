---
title: "Addons"
description: "Extension des fonctionnalités de The Endex"
---

# Système d'Addons

The Endex supporte un système d'addons modulaire permettant d'ajouter des fonctionnalités spécifiques sans alourdir le plugin principal.

## Addons Officiels

### 1. CryptoAddon
Ajoute des crypto-monnaies virtuelles (Bitcoin, Ethereum, etc.) au marché.
- **Fonctionnalités** : Prix basés sur les API réelles (CoinGecko), portefeuilles crypto séparés.
- **Installation** : Déposez `Endex-Crypto.jar` dans `plugins/TheEndex/addons/`.

### 2. DiscordLink
Lie le marché à votre serveur Discord.
- **Fonctionnalités** : Commandes slash Discord (`/price`), notifications de krach dans un canal, synchronisation des rôles basés sur la richesse.

## Créer un Addon

Les développeurs peuvent créer leurs propres addons en étendant la classe `EndexAddon`.

### Structure de base

```java
public class MyCustomAddon extends EndexAddon {
    
    @Override
    public void onEnable() {
        getLogger().info("Mon addon est activé !");
    }
    
    @Override
    public void onDisable() {
        // Nettoyage
    }
}
```

### Chargement

Les addons doivent être placés dans le dossier `plugins/TheEndex/addons/`. Ils sont chargés automatiquement au démarrage du plugin principal.

## Marketplace

(À venir) Une place de marché communautaire où vous pourrez télécharger des addons créés par d'autres utilisateurs.
