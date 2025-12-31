---
title: "Sistema de Entregas"
description: "Cómo funcionan las entregas de objetos en The Endex."
---

Cuando los jugadores retiran objetos de sus tenencias virtuales, esos objetos van a una cola de entregas antes de llegar a su inventario.

## Flujo de Entregas

```
Tenencias → Cola de Entregas → (Retraso opcional) → Inventario del Jugador
```

## Comandos

```text
/market delivery              # Ver entregas pendientes
/market delivery claim        # Reclamar todas las entregas disponibles
/market delivery claim <id>   # Reclamar entrega específica
```

## Configuración

En `config.yml`:

```yaml
delivery:
  # Habilitar sistema de entregas
  enabled: true
  
  # Retraso en segundos antes de que los objetos estén disponibles
  delay: 0
  
  # Entregas máximas pendientes por jugador
  max-pending: 100
  
  # Auto-entregar cuando el jugador tiene espacio
  auto-deliver: false
  
  # Notificar al jugador cuando las entregas estén listas
  notify: true
```

## GUI de Entregas

```text
/market delivery
```

Muestra una interfaz con:
- Entregas pendientes
- Tiempo restante (si hay retraso)
- Estado (disponible/esperando)
- Botón para reclamar

## Estados de Entrega

| Estado | Descripción |
|--------|-------------|
| `PENDIENTE` | Esperando que pase el retraso |
| `DISPONIBLE` | Listo para reclamar |
| `RECLAMADO` | Entregado al jugador |
| `EXPIRADO` | No reclamado a tiempo |

## Retraso de Entregas

El retraso añade realismo simulando "tiempo de envío":

```yaml
delivery:
  delay: 300  # 5 minutos
```

### Saltar Retraso

Los jugadores con permiso especial pueden saltar el retraso:

```text
theendex.delivery.bypass
```

## Auto-Entrega

Cuando está habilitado, los objetos se entregan automáticamente cuando:
- El jugador tiene espacio en inventario
- La entrega está disponible (retraso pasado)

```yaml
delivery:
  auto-deliver: true
```

## Expiración

Las entregas pueden expirar si no se reclaman:

```yaml
delivery:
  expire-after: 604800  # 7 días en segundos
  expire-action: return  # 'return' a tenencias o 'delete'
```

## Notificaciones

Configura notificaciones:

```yaml
delivery:
  notify: true
  notify-sound: ENTITY_EXPERIENCE_ORB_PICKUP
  notify-message: "&a¡Tienes entregas disponibles! Usa /market delivery"
```

## PlaceholderAPI

```text
%endex_deliveries_pending%    # Entregas pendientes
%endex_deliveries_available%  # Entregas disponibles para reclamar
```

## Permisos

| Permiso | Descripción |
|---------|-------------|
| `theendex.delivery.use` | Usar sistema de entregas |
| `theendex.delivery.bypass` | Saltar retraso de entregas |
| `theendex.delivery.unlimited` | Sin límite de entregas pendientes |

## Estrategias

<Tip>
**Revisa Regularmente**  
No dejes que las entregas expiren.
</Tip>

<Info>
**Planifica con Anticipación**  
Considera el retraso de entrega antes de retirar objetos que necesitas urgentemente.
</Info>

<Warning>
**Espacio de Inventario**  
Asegúrate de tener espacio antes de reclamar.
</Warning>

## Páginas Relacionadas

- [Tenencias Virtuales](holdings) — De dónde vienen las entregas
- [Configuración](../reference/configuration) — Referencia completa
- [Permisos](../reference/permissions) — Lista de permisos
