---
title: "Installation"
description: "So installieren Sie The Endex auf Ihrem Server"
---

# Installationsanleitung

Befolgen Sie diese Schritte, um The Endex auf Ihrem Minecraft-Server zu installieren.

## Voraussetzungen

Bevor Sie beginnen, stellen Sie sicher, dass Sie Folgendes haben:

- Einen Minecraft-Server mit **Spigot**, **Paper** oder **Purpur** (Versionen 1.16 - 1.21+).
- **Java 17** oder höher installiert.
- Ein mit **Vault** kompatibles Wirtschaftsplugin (z. B. EssentialsX).
- **Vault** installiert.
- **PlaceholderAPI** (Optional, aber empfohlen für Platzhalter).

## Installationsschritte

<Steps>
  <Step title="Plugin herunterladen">
    Laden Sie die neueste Version von `TheEndex.jar` von [SpigotMC](https://www.spigotmc.org/) oder [Modrinth](https://modrinth.com/) herunter.
  </Step>
  <Step title="Datei installieren">
    Legen Sie die heruntergeladene `.jar`-Datei in den `plugins`-Ordner Ihres Servers.
  </Step>
  <Step title="Server neu starten">
    Starten Sie Ihren Server neu, um das Plugin zu laden. Verwenden Sie nicht `/reload`.
  </Step>
  <Step title="Installation überprüfen">
    Überprüfen Sie die Konsole auf die Startnachricht von The Endex oder geben Sie im Spiel `/plugins` ein, um zu sehen, ob The Endex grün aufgeführt ist.
  </Step>
</Steps>

## Erstkonfiguration

Nach dem ersten Start wird ein Ordner `TheEndex` in Ihrem `plugins`-Verzeichnis erstellt. Dieser Ordner enthält:

- `config.yml`: Die Hauptkonfigurationsdatei.
- `messages.yml`: Übersetzungsdatei für Nachrichten im Spiel.
- `items.yml`: Konfiguration der Gegenstände und ihrer Basispreise.

Weitere Informationen zur Anpassung Ihrer Installation finden Sie im Abschnitt [Konfiguration](/de/reference/configuration).
