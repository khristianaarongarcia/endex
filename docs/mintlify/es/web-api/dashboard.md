---
title: "Panel Web"
description: "Acceder y usar el panel web de The Endex."
---

El Panel Web es una interfaz React en tiempo real que se conecta a tu servidor de Minecraft.

## URL

Cuando está habilitado, está disponible en:

```text
http://<tu-ip>:8080
```

El puerto predeterminado es **8080**.

## Características

- Tabla de precios en tiempo real
- Gráficos de precios (usando Recharts)
- Trading sin GUI del juego
- Panel de administración (recargar, activar eventos)

## Vinculación de Jugadores

Los jugadores pueden vincular su cuenta de Minecraft a la web usando:

```text
/endex link
```

Esto genera un código de un solo uso para ingresar en el navegador y autenticar su identidad.

## Roles de Administrador

En `config.yml` puedes configurar Discord OAuth, autenticación basada en tokens, y más — consulta la siguiente sección API REST y `docs/DEVELOPERS.md` en el repositorio para detalles.

## Personalización

La apariencia general del dashboard se puede personalizar — colores, logo, etc. Los usuarios avanzados pueden colocar un paquete predeterminado exportado en `webui/` y sobrescribir lo que deseen (ver `docs/CUSTOM_WEBUI.md` para detalles).

## Configuración

Los ajustes web se controlan en `config.yml`:

```yaml
web:
  enabled: true
  port: 8080
  host: "0.0.0.0"
  cors-origins:
    - "*"
  api:
    tokens: []
    token-hashes: []
```

## Seguridad

<Warning>
**Uso en Producción**  
Para despliegues de producción, configura un proxy inverso (Nginx, Caddy) delante del servidor web.
</Warning>

Consulta la documentación de [API REST](rest-api) y `docs/SECURITY.md` en el repositorio para mejores prácticas de seguridad.
