---
title: "FAQ"
description: "Foire Aux Questions"
---

# Foire Aux Questions (FAQ)

### Q: Est-ce que The Endex remplace EssentialsX Economy ?
**R:** Non. The Endex n'est pas un plugin de gestion de monnaie (il ne gère pas les balances des joueurs). Il utilise **Vault** pour interagir avec votre plugin d'économie existant (comme EssentialsX, CMI, ou Economy). Il agit comme une "Bourse" au-dessus de votre économie.

### Q: Les prix peuvent-ils devenir négatifs ?
**R:** Non. Vous pouvez configurer un `min-price` (prix minimum) dans la configuration. Par défaut, les prix ne peuvent pas descendre en dessous de 0.01 (ou la valeur définie).

### Q: Est-ce compatible avec la version 1.8 ?
**R:** Non. The Endex est conçu pour les versions modernes de Minecraft (1.16+). Les anciennes versions manquent de fonctionnalités API nécessaires et ne sont pas supportées.

### Q: Comment puis-je réinitialiser tous les prix ?
**R:** Vous pouvez supprimer le fichier `data.yml` (ou la base de données) pendant que le serveur est éteint, ou utiliser une commande admin (si disponible dans votre version) pour réinitialiser aux prix de base.

### Q: Le tableau de bord web ne fonctionne pas.
**R:** Vérifiez que :
1. `enabled: true` est défini dans `config.yml` sous la section `web`.
2. Le port (par défaut 8080) est ouvert sur votre pare-feu/routeur.
3. Aucun autre service n'utilise ce port.
4. Vérifiez la console pour les erreurs de démarrage "BindException".

### Q: Puis-je utiliser des objets personnalisés (Custom Items) ?
**R:** Oui, The Endex supporte partiellement les objets personnalisés via NBT ou noms spécifiques, selon la configuration. L'intégration avec des plugins comme Oraxen ou ItemsAdder est prévue ou disponible via des addons.

### Q: Comment empêcher les joueurs d'abuser du système ?
**R:**
- Utilisez les limites de prix (`min-price`, `max-price`).
- Activez les taxes sur les transactions.
- Surveillez les logs de transactions.
- Ajustez la `volatility` pour éviter des changements trop brusques.
