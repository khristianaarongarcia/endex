---
title: "Permisos"
description: "Nodos de permiso para The Endex."
---

Esta página lista los nodos de permiso asociados con The Endex.

Asigna estos en tu plugin de permisos (por ejemplo, LuckPerms).

## Permisos Básicos

| Permiso | Descripción |
|---------|-------------|
| `theendex.use` | Acceso a comandos del mercado |
| `theendex.admin` | Comandos de administración (recargar, eventos) |
| `theendex.invest` | Permite crear inversiones |
| `theendex.bypass.cooldown` | Saltar enfriamientos de trading |

## Permisos Web

| Permiso | Descripción |
|---------|-------------|
| `theendex.web.trade` | Comerciar a través del dashboard |
| `theendex.web.admin` | Funciones de administración del dashboard |
| `theendex.web.link` | Permitir usar /endex link |

## Permisos de Entregas

| Permiso | Descripción |
|---------|-------------|
| `theendex.delivery.use` | Acceso al sistema de entregas |
| `theendex.delivery.bypass` | Saltar retraso de entregas |

## Permisos de PlaceholderAPI

| Permiso | Descripción |
|---------|-------------|
| `theendex.placeholder.use` | Acceso a placeholders PAPI |

## Permisos de Complementos

Cada complemento típicamente define permisos bajo su propio espacio de nombres:

```text
theendex.<addon>.*
theendex.<addon>.use
theendex.<addon>.admin
```

Ejemplo (Complemento Crypto):

```text
theendex.crypto.use
theendex.crypto.admin
theendex.crypto.mine
```

Para una guía de permisos completa y detallada, consulta `docs/PERMISSIONS.md` en el repositorio.
