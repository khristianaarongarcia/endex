---
title: "Fehlerbehebung"
description: "Lösung häufiger Probleme"
---

# Fehlerbehebung

Wenn Sie Probleme mit The Endex haben, befolgen Sie diese Diagnoseschritte.

## Häufige Probleme

### Das Plugin startet nicht (Rot in /plugins)
- **Ursache**: Fehlende Abhängigkeit.
- **Lösung**: Stellen Sie sicher, dass Sie **Vault** und ein Wirtschaftsplugin (EssentialsX) installiert haben. Überprüfen Sie die Startprotokolle auf "Dependency not found".

### "An internal error occurred..." bei einem Befehl
- **Ursache**: Plugin-Bug oder beschädigte Konfiguration.
- **Lösung**:
  1. Überprüfen Sie die Konsole auf den "Stack Trace" (den Fehlertextblock).
  2. Überprüfen Sie Ihre YAML-Dateien (config, items) mit einem Online-YAML-Validator. Ein einfacher Einrückungsfehler kann alles kaputt machen.
  3. Wenn der Fehler weiterhin besteht, melden Sie ihn auf unserem Discord oder GitHub.

### Preise ändern sich nicht
- **Ursache**: Volatilität zu niedrig oder keine Transaktionen.
- **Lösung**:
  - Überprüfen Sie, ob `volatility` in `items.yml` > 0 ist.
  - Überprüfen Sie, ob der Markt nicht "geschlossen" oder "eingefroren" ist (falls diese Funktion aktiv ist).
  - Führen Sie Testtransaktionen durch, um zu sehen, ob sich etwas bewegt.

### Das Web-Dashboard zeigt "Disconnected"
- **Ursache**: WebSocket- oder Port-Problem.
- **Lösung**:
  - Überprüfen Sie, ob der Port geöffnet ist (TCP).
  - Wenn Sie einen Proxy (Nginx/Apache) verwenden, stellen Sie sicher, dass die WebSocket-Unterstützung konfiguriert ist.
  - Überprüfen Sie die in `config.yml` konfigurierte URL.

## Hilfe erhalten

Wenn Sie hier keine Lösung finden:

1. **Logs**: Kopieren Sie die relevanten Logs aus Ihrer Konsole.
2. **Config**: Bereiten Sie eine Kopie Ihrer Konfigurationsdateien vor (ohne Passwörter).
3. **Kontakt**: Treten Sie unserem Support-Discord-Server bei oder öffnen Sie ein Issue auf GitHub.

Bitte geben Sie so viele Details wie möglich an, damit wir Ihnen helfen können!
