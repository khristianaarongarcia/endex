---
title: "FAQ"
description: "Häufig gestellte Fragen"
---

# Häufig gestellte Fragen (FAQ)

### F: Ersetzt The Endex EssentialsX Economy?
**A:** Nein. The Endex ist kein Währungsverwaltungs-Plugin (es verwaltet keine Spielerguthaben). Es verwendet **Vault**, um mit Ihrem bestehenden Wirtschaftsplugin (wie EssentialsX, CMI oder Economy) zu interagieren. Es fungiert als "Börse" über Ihrer Wirtschaft.

### F: Können Preise negativ werden?
**A:** Nein. Sie können einen `min-price` (Mindestpreis) in der Konfiguration festlegen. Standardmäßig können Preise nicht unter 0.01 (oder den definierten Wert) fallen.

### F: Ist es mit Version 1.8 kompatibel?
**A:** Nein. The Endex ist für moderne Minecraft-Versionen (1.16+) konzipiert. Älteren Versionen fehlen notwendige API-Funktionen und sie werden nicht unterstützt.

### F: Wie kann ich alle Preise zurücksetzen?
**A:** Sie können die Datei `data.yml` (oder die Datenbank) löschen, während der Server ausgeschaltet ist, oder einen Admin-Befehl verwenden (falls in Ihrer Version verfügbar), um auf Basispreise zurückzusetzen.

### F: Das Web-Dashboard funktioniert nicht.
**A:** Überprüfen Sie, ob:
1. `enabled: true` in `config.yml` unter dem Abschnitt `web` gesetzt ist.
2. Der Port (Standard 8080) in Ihrer Firewall/Router geöffnet ist.
3. Kein anderer Dienst diesen Port verwendet.
4. Überprüfen Sie die Konsole auf "BindException"-Startfehler.

### F: Kann ich benutzerdefinierte Gegenstände (Custom Items) verwenden?
**A:** Ja, The Endex unterstützt teilweise benutzerdefinierte Gegenstände über NBT oder spezifische Namen, je nach Konfiguration. Die Integration mit Plugins wie Oraxen oder ItemsAdder ist geplant oder über Addons verfügbar.

### F: Wie verhindere ich, dass Spieler das System missbrauchen?
**A:**
- Verwenden Sie Preisgrenzen (`min-price`, `max-price`).
- Aktivieren Sie Steuern auf Transaktionen.
- Überwachen Sie Transaktionsprotokolle.
- Passen Sie die `volatility` an, um zu abrupte Änderungen zu vermeiden.
