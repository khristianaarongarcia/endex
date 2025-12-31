---
title: "Dépannage"
description: "Résolution des problèmes courants"
---

# Dépannage

Si vous rencontrez des problèmes avec The Endex, suivez ces étapes de diagnostic.

## Problèmes Courants

### Le plugin ne démarre pas (Rouge dans /plugins)
- **Cause** : Dépendance manquante.
- **Solution** : Assurez-vous d'avoir **Vault** et un plugin d'économie (EssentialsX) installés. Vérifiez les logs de démarrage pour "Dependency not found".

### "An internal error occurred..." lors d'une commande
- **Cause** : Bug du plugin ou configuration corrompue.
- **Solution** :
  1. Vérifiez la console pour la "Stack Trace" (le bloc de texte d'erreur).
  2. Vérifiez vos fichiers YAML (config, items) avec un validateur YAML en ligne. Une simple erreur d'indentation peut tout casser.
  3. Si l'erreur persiste, signalez-la sur notre Discord ou GitHub.

### Les prix ne changent pas
- **Cause** : Volatilité trop basse ou pas de transactions.
- **Solution** :
  - Vérifiez que `volatility` est > 0 dans `items.yml`.
  - Vérifiez que le marché n'est pas "fermé" ou "gelé" (si cette fonctionnalité est active).
  - Faites des transactions de test pour voir si ça bouge.

### Le Web Dashboard affiche "Disconnected"
- **Cause** : Problème de WebSocket ou de port.
- **Solution** :
  - Vérifiez que le port est ouvert (TCP).
  - Si vous utilisez un proxy (Nginx/Apache), assurez-vous que le support WebSocket est configuré.
  - Vérifiez l'URL configurée dans `config.yml`.

## Obtenir de l'Aide

Si vous ne trouvez pas de solution ici :

1. **Logs** : Copiez les logs pertinents de votre console.
2. **Config** : Préparez une copie de vos fichiers de configuration (sans mots de passe).
3. **Contact** : Rejoignez notre serveur Discord de support ou ouvrez une issue sur GitHub.

Merci de fournir autant de détails que possible pour nous aider à vous aider !
