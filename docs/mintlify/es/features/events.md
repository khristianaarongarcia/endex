---
title: "Eventos del Mercado"
description: "Eventos temporizados que aplican multiplicadores de precio a categorías específicas o a todo el mercado."
---

Los eventos del mercado son multiplicadores de precio activados por administradores que crean oportunidades temporales de compra y venta.

Los eventos se definen en `plugins/TheEndex/events.yml`.

## Comandos

```text
/market event list
/market event <nombre>
/market event end <nombre>
/market event clear
```

<Tip>
Los eventos regulares (fiebre de minerales semanal, cosechas estacionales, etc.) son una gran manera de mantener el trading activo.
</Tip>

## Configuración de Ejemplo

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

## Cómo se Aplican los Eventos

El precio efectivo es el precio base multiplicado por el multiplicador del evento combinado.

<Info>
Para el esquema completo y reglas de apilamiento, consulta `docs/EVENTS.md` en el repositorio.
</Info>

## Programar Eventos

Aunque The Endex no tiene programación incorporada, puedes usar:

### Programadores Externos

Usa cron jobs o programadores del servidor para activar eventos:

```bash
# Todos los días a las 6 PM
0 18 * * * screen -S minecraft -X stuff "/market event fiebre_minera^M"
```

### Otros Plugins

Plugins como **DeluxeScheduler** o **ScheduledCommands** pueden activar:
```
/market event <nombre>
```

---

## Permisos

| Permiso | Descripción |
|---------|-------------|
| `theendex.admin` | Administrar eventos |

---

## Estrategias para Jugadores

<Tip>
**Compra Antes del Evento**  
Si sabes que viene un evento, compra objetos afectados con anticipación.
</Tip>

<Info>
**Mira los Anuncios**  
Los eventos se anuncian en el chat. ¡Reacciona rápido para obtener ganancias!
</Info>

<Warning>
**Eventos de Caída**  
Durante caídas del mercado, mantener suele ser mejor que vender barato.
</Warning>

---

## Consejos para Administradores

<Tip>
**Eventos Regulares**  
Programa eventos para mantener la economía viva. Eventos semanales funcionan bien.
</Tip>

<Info>
**Temas Estacionales**  
Relaciona eventos con temporadas del mundo real o del servidor para inmersión.
</Info>

<Warning>
**Equilibra Multiplicadores**  
Multiplicadores extremos (mayores a 2.0 o menores a 0.5) pueden desestabilizar la economía. Usa con precaución.
</Warning>

---

## Páginas Relacionadas

- [Precios Dinámicos](pricing) — Cómo los eventos afectan los precios
- [Configuración](../reference/configuration) — Referencia completa de events.yml
- [Comandos](../reference/commands) — Comandos de eventos
