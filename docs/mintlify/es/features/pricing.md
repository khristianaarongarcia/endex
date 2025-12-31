---
title: "Precios Dinámicos"
description: "Cómo funciona el sistema de oferta/demanda para calcular precios en tiempo real."
---

El sistema de precios de The Endex ajusta los valores de los objetos basándose en el volumen de trading — cuando los jugadores compran, los precios suben; cuando venden, los precios bajan.

## Conceptos Básicos

| Concepto | Descripción |
|---------|-------------|
| **Precio Base** | Valor inicial del objeto |
| **Oferta** | Cuántas unidades se han vendido recientemente |
| **Demanda** | Cuántas unidades se han comprado recientemente |
| **Sensibilidad** | Qué tan rápido responden los precios |

## Fórmula de Precio

```
precio = precioBase × (1 + (demanda - oferta) × sensibilidad)
```

### Ejemplo

- Precio base: $100
- Demanda (compras): 50
- Oferta (ventas): 30
- Sensibilidad: 0.05

```
precio = 100 × (1 + (50 - 30) × 0.05)
precio = 100 × (1 + 20 × 0.05)
precio = 100 × 1.10
precio = $110
```

## Configuración

En `config.yml`:

```yaml
# Segundos entre actualizaciones de precio
update-interval-seconds: 60

# Qué tan reactivos son los precios (0.01 - 0.20)
price-sensitivity: 0.05

# Cuántas actualizaciones se almacenan en el historial
history-length: 5
```

## Límites de Precio

Define mínimos y máximos en `items.yml`:

```yaml
items:
  DIAMOND:
    enabled: true
    base-price: 800.0
    min-price: 100.0    # Nunca bajará de esto
    max-price: 2000.0   # Nunca subirá de esto
```

## Multiplicadores de Eventos

Los eventos del mercado aplican multiplicadores temporales:

```yaml
events:
  hora_oro:
    multipliers:
      GOLD_INGOT: 2.0   # Precio doble
      GOLD_BLOCK: 2.0
```

Ver: [Eventos del Mercado](events)

## Indicadores de Tendencia

La GUI muestra tendencias de precios:

| Símbolo | Significado |
|--------|---------|
| ↑ | Precio subiendo |
| ↓ | Precio bajando |
| → | Precio estable |

## Historial de Precios

The Endex mantiene un historial de precios para cada objeto:

```text
/market price DIAMOND
```

Muestra:
```
Diamante — Historial de Precios
Actual: $800.00 ↑
Hace 1h: $780.00
Hace 2h: $750.00
Hace 3h: $720.00
```

## Estrategias de Trading

<Tip>
**Comprar Bajo, Vender Alto**  
Monitorea las tendencias y compra cuando los precios estén bajos.
</Tip>

<Info>
**Aprovecha los Eventos**  
Los eventos del mercado crean oportunidades de ganancia.
</Info>

<Warning>
**Cuidado con la Volatilidad**  
Precios muy volátiles pueden resultar en pérdidas.
</Warning>

## Páginas Relacionadas

- [Eventos del Mercado](events) — Multiplicadores temporales
- [Tenencias](holdings) — Almacenamiento virtual
- [Configuración](../reference/configuration) — Referencia completa
