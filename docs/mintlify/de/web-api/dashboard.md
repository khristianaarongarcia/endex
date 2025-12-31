---
title: "Web-Dashboard"
description: "Echtzeit-Weboberfläche für Ihre Wirtschaft"
---

# Web-Dashboard

The Endex enthält einen integrierten Webserver, der ein schönes und responsives Dashboard bereitstellt, damit Ihre Spieler die Wirtschaft außerhalb des Spiels verfolgen können.

## Funktionen

- **Echtzeit-Daten**: Preise werden sofort über WebSocket aktualisiert.
- **Historische Diagramme**: Visualisieren Sie Preistrends über 24h, 7 Tage oder 30 Tage.
- **Top Gewinner/Verlierer**: Sehen Sie schnell, welche Gegenstände am besten oder am schlechtesten abschneiden.
- **Responsive**: Funktioniert perfekt auf Mobilgeräten und Desktops.

## Konfiguration

Um das Dashboard zu aktivieren, bearbeiten Sie `config.yml`:

```yaml
web:
  enabled: true
  port: 8080 # Port des Webservers
  host: "0.0.0.0" # Hört auf allen Schnittstellen
  public-url: "http://ihr-server.com:8080" # URL für Links
```

### Netzwerk

Stellen Sie sicher, dass der Port `8080` (oder der von Ihnen gewählte) in Ihrer Firewall geöffnet und weitergeleitet (Port Forwarding) ist, wenn Sie den Server zu Hause hosten.

## Zugriff

Sobald aktiviert und der Server neu gestartet wurde, greifen Sie über Ihren Browser auf das Dashboard zu:

`http://<ip-ihres-servers>:8080`

## Anpassung

Sie können das Aussehen des Dashboards anpassen, indem Sie die HTML/CSS/JS-Dateien im Ordner `plugins/TheEndex/web/` bearbeiten.

- `index.html`: Die Hauptseite.
- `css/style.css`: Die Stile.
- `js/app.js`: Die Frontend-Logik.

*Hinweis: Grundkenntnisse in der Webentwicklung werden für fortgeschrittene Anpassungen empfohlen.*
