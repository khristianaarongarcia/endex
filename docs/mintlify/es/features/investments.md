---
title: "Inversiones"
description: "Bloquea objetos, gana APR, y canjéalos más tarde."
---

Las inversiones permiten a los jugadores bloquear objetos de sus tenencias en posiciones tipo certificado, acumulando **APR** con el tiempo.

## Cómo Funciona

1. El jugador invierte una cantidad de material.
2. Los objetos se eliminan de las tenencias líquidas.
3. Con el tiempo, la inversión acumula interés.
4. El jugador canjea y recibe objetos originales (a tenencias) más interés (generalmente pagado en monedas).

## Configuración (Típica)

```yaml
investments:
  enabled: true
  apr-percent: 5.0
```

## Comandos (Típicos)

```text
/market invest buy <material> <cantidad>
/market invest list
/market invest redeem-all
```

<Warning>
Si tu servidor vincula el valor de canje con el valor de mercado actual, las inversiones no están libres de riesgo; los precios pueden moverse durante tu inversión.
</Warning>

## Relacionado

- [Tenencias](holdings)
- [Precios](pricing)

### Ver Inversiones

```
/market invest list
```

Salida:

```
=== Tus Inversiones ===
#1: 100 Diamantes
    Precio de compra: $10,000.00
    Valor actual: $10,125.00 (+1.25%)
    Mantenido: 3 días 2 horas
    Madurado: ✓ Puede canjearse

#2: 500 Lingotes de Hierro  
    Precio de compra: $2,500.00
    Valor actual: $2,531.25 (+1.25%)
    Mantenido: 2 horas
    Madurado: ✗ 22 horas restantes
```

### Canjear Inversiones

Reclama inversiones maduras:

```
/market invest redeem-all
```

Solo las inversiones que han pasado el período mínimo de tenencia se canjean.

---

## Cálculo de APR

El interés se acumula según la Tasa Porcentual Anual (APR):

```
valor = principal × (1 + APR × (díasMantenidos / 365))
```

### Ejemplo

- Inversión: 100 diamantes a $100 cada uno = $10,000
- APR: 5%
- Tiempo mantenido: 30 días

```
interés = $10,000 × 0.05 × (30 / 365) = $41.10
valorTotal = $10,041.10
```

---

## Maduración

Las inversiones tienen un **período mínimo de tenencia** antes de que puedan canjearse:

```yaml
investments:
  min-hold-time: 3600  # segundos (1 hora)
```

- **Antes de madurar** — No se puede canjear
- **Después de madurar** — Canjeable en cualquier momento, sigue ganando

---

## Configuración

```yaml
investments:
  # Habilitar sistema de inversiones
  enabled: true
  
  # Tasa porcentual anual (5.0 = 5%)
  apr: 5.0
  
  # Tiempo mínimo de tenencia antes de canjear (segundos)
  min-hold-time: 3600
  
  # Máximo de inversiones por jugador
  max-per-player: 10
  
  # Valor máximo por inversión
  max-value: 100000
```

---

## Estrategias de Inversión

### Tenencia a Largo Plazo

Mayor APR significa que tenencias más largas son más rentables:

| Tiempo de Tenencia | Retorno con 5% APR |
|--------------------|-------------------|
| 1 día | +0.014% |
| 7 días | +0.096% |
| 30 días | +0.411% |
| 90 días | +1.233% |
| 365 días | +5.000% |

### Especulación de Precios

Las inversiones bloquean tu precio de compra. Si los precios del mercado suben:

- El valor de tu inversión refleja el precio más alto
- Canjear da más monedas de las que invertiste

<Warning>
Si los precios del mercado bajan, tu valor de canje también baja. ¡Las inversiones no están libres de riesgo!
</Warning>

---

## Canje

Cuando canjeas:

1. Los objetos regresan a tus **tenencias** (no al inventario)
2. Recibes monedas por el valor acumulado
3. La inversión se cierra

### Lo Que Recibes

```
canje = (objetosOriginales) + (monedasDeInterés)
```

Ejemplo:
- Invertiste: 100 diamantes a $100 = $10,000
- APR ganado: $200
- **Recibido:** 100 diamantes + $200 en monedas

---

## Acceso por GUI

Accede a las inversiones desde la GUI del mercado:

1. Abre `/market`
2. Haz clic en el botón de **Inversiones**
3. Ve todas las inversiones activas
4. Haz clic para canjear las maduras

---

## Permisos

| Permiso | Descripción |
|---------|-------------|
| `theendex.invest` | Crear y gestionar inversiones |

---

## Consejos

<Tip>
**Diversifica**  
No pongas toda tu riqueza en una inversión. Diversifica en múltiples materiales.
</Tip>

<Info>
**Observa el Mercado**  
Invierte en objetos que crees que subirán de precio para ganancias dobles (APR + aumento de precio).
</Info>

<Warning>
**Liquidez**  
Los objetos invertidos están bloqueados. No inviertas objetos que puedas necesitar pronto.
</Warning>

---

## Páginas Relacionadas

- [Precios Dinámicos](pricing) — Cómo los precios afectan las inversiones
- [Tenencias Virtuales](holdings) — Dónde van los objetos canjeados
- [Comandos](../reference/commands) — Referencia completa de comandos
