---
title: "Configuración"
description: "Ajustes clave en config.yml y consejos comunes de ajuste."
---

El archivo de configuración principal es:

```text
plugins/TheEndex/config.yml
```

Después de cambios, ejecuta:

```text
/endex reload
```

## Precios Básicos

```yaml
update-interval-seconds: 60
price-sensitivity: 0.05
history-length: 5
autosave-minutes: 5
save-on-each-update: true
```

## Modo de Almacenamiento

```yaml
storage:
  sqlite: false
```

Cuando se habilita SQLite, reinicia el servidor para que el plugin migre datos si es necesario.

## Verificador de Actualizaciones

```yaml
update-checker:
  enabled: true      # Verificar actualizaciones al inicio
  notify-ops: true   # Notificar a OPs al unirse
```

## Inversiones

```yaml
investments:
  enabled: true
  apr-percent: 5.0
```

## Eventos

```yaml
events:
  multiplier-cap: 10.0
  stacking:
    mode: multiplicative
    default-weight: 1.0
    max-active: 0
```

Ver: [Eventos](../features/events)

## Precios Conscientes del Inventario (Opcional)

```yaml
price-inventory:
  enabled: false
  sensitivity: 0.02
  per-player-baseline: 64
  max-impact-percent: 10.0
```

## Ajustes Web (Avanzado)

```yaml
web:
  roles:
    default: TRADER
    trader-permission: endex.web.trade
    admin-view-permission: endex.web.admin
  api:
    tokens: []
    token-hashes: []
```

## UI Web Personalizada

Para servir tu propio paquete de dashboard:

```yaml
web:
  custom:
    enabled: false
    root: webui
    reload: false
    export-default: true
```

## Personalización de GUI

Los layouts de GUI se configuran en archivos separados bajo `guis/`:

- `guis/market.yml` — Interfaz principal del mercado
- `guis/details.yml` — Panel de detalles del objeto
- `guis/holdings.yml` — Panel de tenencias virtuales
- `guis/deliveries.yml` — Panel de cola de entregas

Cada archivo soporta:
- Títulos personalizados con códigos de color
- Tamaño de inventario (filas × 9)
- Posiciones de slots para botones y objetos
- Definiciones de categorías con materiales personalizados

## Alias de Comandos

Los alias de comandos personalizados se configuran en `commands.yml`:

```yaml
aliases:
  tienda: "market"           # /tienda → /market
  stock: "market holdings"   # /stock → /market holdings
  precios: "market top"      # /precios → /market top
```

Para una guía de configuración completa y detallada, consulta `docs/CONFIG.md` en el repositorio.
