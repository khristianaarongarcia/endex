---
title: "Installation"
description: "Comment installer The Endex sur votre serveur"
---

# Guide d'Installation

Suivez ces étapes pour installer The Endex sur votre serveur Minecraft.

## Prérequis

Avant de commencer, assurez-vous d'avoir :

- Un serveur Minecraft exécutant **Spigot**, **Paper**, ou **Purpur** (versions 1.16 - 1.21+).
- **Java 17** ou supérieur installé.
- Un plugin d'économie compatible avec **Vault** (ex: EssentialsX).
- **Vault** installé.
- **PlaceholderAPI** (Optionnel, mais recommandé pour les placeholders).

## Étapes d'Installation

<Steps>
  <Step title="Télécharger le Plugin">
    Téléchargez la dernière version de `TheEndex.jar` depuis [SpigotMC](https://www.spigotmc.org/) ou [Modrinth](https://modrinth.com/).
  </Step>
  <Step title="Installer le Fichier">
    Placez le fichier `.jar` téléchargé dans le dossier `plugins` de votre serveur.
  </Step>
  <Step title="Redémarrer le Serveur">
    Redémarrez votre serveur pour charger le plugin. N'utilisez pas `/reload`.
  </Step>
  <Step title="Vérifier l'Installation">
    Vérifiez la console pour le message de démarrage de The Endex ou tapez `/plugins` en jeu pour voir si The Endex est listé en vert.
  </Step>
</Steps>

## Configuration Initiale

Après le premier lancement, un dossier `TheEndex` sera créé dans votre répertoire `plugins`. Ce dossier contient :

- `config.yml` : Le fichier de configuration principal.
- `messages.yml` : Fichier de traduction pour les messages en jeu.
- `items.yml` : Configuration des objets et de leurs prix de base.

Consultez la section [Configuration](/fr/reference/configuration) pour plus de détails sur la personnalisation de votre installation.
