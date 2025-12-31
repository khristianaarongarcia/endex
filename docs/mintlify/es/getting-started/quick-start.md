---
title: "Inicio Rápido"
description: "Comienza con The Endex en 5 minutos."
---

Esta guía te ayudará a configurar y ejecutar el mercado rápidamente.

## Paso 1: Verificar Instalación

```text
/endex version
```

Si ves la información de la versión, el plugin está cargado.

## Paso 2: Abrir el Mercado

```text
/market
```

Esto abre la GUI principal del mercado donde los jugadores pueden explorar, comprar y vender objetos.

## Paso 3: Comandos Básicos

### Para Jugadores

```text
/market                    # Abrir GUI del mercado
/market buy <material> <cantidad>   # Comprar objetos
/market sell <material> <cantidad>  # Vender objetos
/market price <material>   # Ver precio actual
/market holdings           # Ver tenencias virtuales
```

### Para Administradores

```text
/endex reload              # Recargar configuración
/endex version             # Ver versión del plugin
/market event list         # Listar eventos activos
/market event <nombre>     # Iniciar un evento
```

## Paso 4: Personalizar el Mercado

Edita `plugins/TheEndex/items.yml` para configurar qué objetos están disponibles:

```yaml
items:
  DIAMOND:
    enabled: true
    base-price: 800.0
    min-price: 100.0
    max-price: 2000.0
    
  IRON_INGOT:
    enabled: true
    base-price: 50.0
    min-price: 10.0
    max-price: 200.0
```

Después de editar, ejecuta:
```text
/endex reload
```

## Paso 5: Crear un Evento (Opcional)

Edita `plugins/TheEndex/events.yml`:

```yaml
events:
  fiebre_minera:
    display-name: "&6⛏ ¡Fiebre Minera!"
    description: "¡Los materiales de minería tienen alta demanda!"
    duration: 3600
    broadcast: true
    multipliers:
      DIAMOND: 1.5
      EMERALD: 1.5
      GOLD_INGOT: 1.3
```

Inicia el evento:
```text
/market event fiebre_minera
```

## Panel Web (Opcional)

Habilita el panel web en `config.yml`:

```yaml
web:
  enabled: true
  port: 8080
```

Accede en: `http://tu-ip:8080`

## Próximos Pasos

<CardGroup cols={2}>
  <Card title="Precios Dinámicos" icon="chart-line" href="../features/pricing">
    Aprende cómo funcionan los precios
  </Card>
  <Card title="Tenencias" icon="warehouse" href="../features/holdings">
    Sistema de almacenamiento virtual
  </Card>
  <Card title="Inversiones" icon="piggy-bank" href="../features/investments">
    Sistema de APR
  </Card>
  <Card title="Comandos" icon="terminal" href="../reference/commands">
    Lista completa de comandos
  </Card>
</CardGroup>
