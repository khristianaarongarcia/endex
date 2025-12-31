---
title: "Tableau de Bord Web"
description: "Interface web en temps réel pour votre économie"
---

# Tableau de Bord Web

The Endex inclut un serveur web intégré qui fournit un tableau de bord magnifique et réactif pour que vos joueurs puissent suivre l'économie en dehors du jeu.

## Fonctionnalités

- **Données en Temps Réel** : Les prix se mettent à jour instantanément via WebSocket.
- **Graphiques Historiques** : Visualisez les tendances de prix sur 24h, 7j ou 30j.
- **Top Gainers/Losers** : Voyez rapidement quels objets performent le mieux ou le moins bien.
- **Responsive** : Fonctionne parfaitement sur mobile et ordinateur.

## Configuration

Pour activer le tableau de bord, éditez `config.yml` :

```yaml
web:
  enabled: true
  port: 8080 # Port du serveur web
  host: "0.0.0.0" # Écoute sur toutes les interfaces
  public-url: "http://votre-serveur.com:8080" # URL pour les liens
```

### Réseau

Assurez-vous que le port `8080` (ou celui que vous avez choisi) est ouvert dans votre pare-feu et redirigé (port forwarded) si vous hébergez le serveur chez vous.

## Accès

Une fois activé et le serveur redémarré, accédez au tableau de bord via votre navigateur :

`http://<ip-de-votre-serveur>:8080`

## Personnalisation

Vous pouvez personnaliser l'apparence du tableau de bord en modifiant les fichiers HTML/CSS/JS situés dans le dossier `plugins/TheEndex/web/`.

- `index.html` : La page principale.
- `css/style.css` : Les styles.
- `js/app.js` : La logique frontend.

*Note : Une connaissance de base en développement web est recommandée pour la personnalisation avancée.*
