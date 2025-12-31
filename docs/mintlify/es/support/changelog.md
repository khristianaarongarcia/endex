---
title: "Registro de Cambios"
description: "Versiones y cambios de The Endex."
---

Esta página lista las versiones principales y cambios de The Endex.

Para las actualizaciones más recientes, consulta la [página de releases de GitHub](https://github.com/KAG-Apparatus/The-Endex/releases).

---

## v1.5.7-dec1022

<Note>
**Última Versión** — 10 de Junio, 2025
</Note>

**Internacionalización Web**
- Añadido soporte completo de i18n para el panel web
- 9 traducciones de idiomas: Inglés, Chino, Español, Francés, Alemán, Japonés, Coreano, Portugués, Ruso
- Las traducciones cubren toda la interfaz del panel, tooltips, mensajes de error y panel de administración
- Detección automática de idioma del navegador y cambio manual de idioma
- Preferencias de idioma guardadas automáticamente en almacenamiento local

**Características de Administración**
- Panel de administración ahora traducido
- Todos los mensajes del sistema ahora son conscientes de la localización

---

## v1.5.6

**Características de Tenencias**
- Sistema de tenencias virtuales mejorado
- Añadida tabla de clasificación de tenencias
- Corregidos problemas de retraso en entregas

**Correcciones de Errores**
- Corregido error raro de cálculo de precios
- Uso de memoria mejorado
- Corregidos problemas de actualización de PlaceholderAPI

---

## v1.5.5

**Sistema de Inversiones**
- Añadidas inversiones con APR
- Configuración de período mínimo de tenencia
- Interfaz GUI de inversiones

**Mejoras Web**
- Actualizaciones en tiempo real por WebSocket
- Renderizado de gráficos mejorado
- Diseño responsive para móviles

---

## v1.5.0

<Note>
**Versión Mayor**
</Note>

**Nuevas Características**
- Lanzamiento del Panel Web
- Endpoints de API REST
- Sistema de vinculación de cuentas de jugadores
- Sistema de eventos del mercado

**Cambios de Arquitectura**
- Motor de precios refactorizado
- Almacenamiento de datos mejorado
- Base del sistema de complementos

---

## v1.4.x

**Características Principales**
- Sistema de precios dinámicos
- Cálculos de oferta y demanda
- Interfaz GUI básica
- Integración con Vault

---

## v1.3.x

**Versiones Tempranas**
- Funcionalidad inicial del mercado
- Comandos básicos de compra y venta
- Sistema de configuración

---

## Notas de Actualización

### Actualizando de v1.4.x a v1.5.x

1. Haz backup de la carpeta `plugins/TheEndex/`
2. Detén el servidor
3. Reemplaza el JAR del plugin
4. Inicia el servidor
5. Revisa la consola para mensajes de migración
6. Verifica la integridad de los datos

### Actualizando de v1.5.x a v1.5.7

1. Detén el servidor (o usa recarga en caliente)
2. Reemplaza el JAR del plugin
3. Ejecuta `/endex reload`
4. El panel web tendrá automáticamente las nuevas características de traducción

<Warning>
**Siempre Haz Backup**  
Siempre haz backup de tus datos antes de actualizar.
</Warning>

---

## Hoja de Ruta

Características próximas:

- Soporte de base de datos MySQL
- Más complementos
- Analíticas avanzadas
- API v2

<Card title="GitHub" icon="github" href="https://github.com/KAG-Apparatus/The-Endex">
  Ver registro de cambios completo y código fuente
</Card>
