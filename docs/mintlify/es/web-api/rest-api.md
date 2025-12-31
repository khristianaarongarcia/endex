---
title: "API REST"
description: "Endpoints de la API REST expuestos por The Endex."
---

Los siguientes endpoints están disponibles en el servidor web integrado.

## URL Base

```text
http://<tu-ip>:8080/api
```

## Autenticación

Para endpoints protegidos, proporciona en los headers:

```text
Authorization: Bearer <token>
```

Los tokens se configuran en el array `web.api.tokens` de `config.yml`.

<Warning>
Mantén los tokens en secreto. Si se comprometen, rota inmediatamente.
</Warning>

## Endpoints

### Obtener Precios

**`GET /api/prices`**

Devuelve precios actuales para todos los materiales.

Respuesta:

```json
{
  "DIAMOND": { "price": 800.00, "change": "+5.25%" },
  "EMERALD": { "price": 500.00, "change": "-2.10%" }
}
```

### Obtener Historial de Precios

**`GET /api/prices/:material/history`**

Devuelve historial de precios para un material.

Respuesta:

```json
{
  "material": "DIAMOND",
  "history": [
    { "timestamp": 1609459200000, "price": 750.00 },
    { "timestamp": 1609545600000, "price": 800.00 }
  ]
}
```

### Comprar/Vender

**`POST /api/trade`**

Solicitud:

```json
{
  "action": "buy",
  "material": "DIAMOND",
  "amount": 10
}
```

Respuesta:

```json
{
  "success": true,
  "newBalance": 9200.00,
  "totalCost": 8000.00
}
```

### Obtener Tenencias

**`GET /api/holdings`**

Devuelve tenencias actuales del jugador autenticado.

### Admin: Activar Evento

**`POST /api/admin/event`**

Activa un evento del mercado. Requiere token de admin.

Solicitud:

```json
{
  "event": "fiebre_minera"
}
```

### Admin: Recargar

**`POST /api/admin/reload`**

Recarga la configuración del plugin. Requiere token de admin.

## Respuestas de Error

Todos los endpoints devuelven en caso de error:

```json
{
  "error": true,
  "message": "Descripción de lo que pasó"
}
```

Códigos HTTP comunes:
- `400` — Solicitud incorrecta
- `401` — No autenticado
- `403` — Prohibido
- `404` — No encontrado
- `500` — Error del servidor

## Límite de Tasa

Los endpoints de la API están protegidos con límites de tasa. Predeterminados:

- **Endpoints públicos:** 60 solicitudes por minuto
- **Endpoints autenticados:** 120 solicitudes por minuto
- **Endpoints de admin:** 30 solicitudes por minuto

## WebSocket

Además de los endpoints REST, las actualizaciones en tiempo real están disponibles vía WebSocket:

```text
ws://<tu-ip>:8080/ws
```

---

Para una guía detallada para desarrolladores, consulta `docs/DEVELOPERS.md` en el repositorio o la página de [API para Desarrolladores](../developers/api).
