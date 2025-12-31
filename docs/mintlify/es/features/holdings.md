---
title: "Tenencias Virtuales"
description: "El sistema de tenencias virtuales de The Endex para trading sin inventario."
---

Las tenencias virtuales permiten a los jugadores almacenar objetos sin usar su inventario de Minecraft, permitiendo estrategias de trading a largo plazo.

## Cómo Funciona

1. Los jugadores compran objetos del mercado
2. Los objetos van a las tenencias (no al inventario)
3. Los jugadores pueden vender desde las tenencias en cualquier momento
4. O retirar objetos físicos a su inventario

## Comandos

```text
/market holdings              # Ver tus tenencias
/market holdings withdraw <material> <cantidad>  # Retirar al inventario
/market sell <material> <cantidad>              # Vender desde tenencias
```

## GUI de Tenencias

```text
/market holdings
```

Abre una interfaz visual mostrando:
- Todos los objetos en tenencia
- Cantidad de cada objeto
- Valor actual de mercado
- Valor total del portafolio

## Configuración

En `config.yml`:

```yaml
holdings:
  # Habilitar sistema de tenencias
  enabled: true
  
  # Cantidad máxima de objetos por jugador
  max-items: 1000000
  
  # Máximo tipos únicos de objetos
  max-types: 50
```

## Sistema de Entregas

Cuando los jugadores retiran objetos, van a una cola de entregas:

```text
/market delivery              # Ver entregas pendientes
/market delivery claim        # Reclamar todas las entregas
```

### Configuración de Entregas

```yaml
delivery:
  # Segundos antes de que los objetos estén disponibles
  delay: 0
  
  # Entregas máximas pendientes
  max-pending: 100
  
  # Auto-entregar cuando hay espacio en inventario
  auto-deliver: false
```

## Valor del Portafolio

El valor total de las tenencias se calcula usando precios actuales del mercado:

```
valorTotal = Σ(cantidad × precioActual)
```

Esto significa que el valor del portafolio fluctúa con el mercado.

## PlaceholderAPI

```text
%endex_holdings_total%        # Valor total de tenencias
%endex_holdings_count%        # Objetos totales en tenencias
%endex_holdings_top_1%        # Objeto más valioso
%endex_holdings_top_1_value%  # Valor del objeto más valioso
```

## Tabla de Clasificación

Ver los jugadores más ricos por tenencias:

```text
/market top holdings
```

Muestra:
```
=== Tenencias Principales ===
#1: Steve — $1,250,000.00
#2: Alex — $890,500.00
#3: Notch — $654,321.00
```

## Estrategias

<Tip>
**Diversifica**  
No pongas todos tus fondos en un solo objeto.
</Tip>

<Info>
**Monitorea el Mercado**  
Los valores de las tenencias cambian con los precios del mercado.
</Info>

<Warning>
**Retiros Durante Eventos**  
Los precios de los eventos afectan los valores de venta.
</Warning>

## Permisos

| Permiso | Descripción |
|---------|-------------|
| `theendex.holdings` | Acceso a tenencias |
| `theendex.delivery.use` | Usar sistema de entregas |
| `theendex.delivery.bypass` | Saltar retraso de entregas |

## Páginas Relacionadas

- [Precios Dinámicos](pricing) — Cómo se calculan los valores
- [Inversiones](investments) — Bloquear tenencias para APR
- [Entregas](delivery) — Sistema de retiro de objetos
