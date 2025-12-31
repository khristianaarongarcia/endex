---
title: "Integración PlaceholderAPI"
description: "Lista completa de placeholders de PlaceholderAPI para The Endex — úsalos en scoreboards, hologramas, tablists y más."
---

The Endex proporciona integración completa con PlaceholderAPI con más de 30 placeholders para mostrar datos del mercado, tenencias de jugadores, tablas de clasificación y estadísticas.

<Info>
PlaceholderAPI es una **dependencia blanda** — el plugin funciona sin él, pero los placeholders no estarán disponibles.
</Info>

## Instalación

1. Instala [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) en tu servidor
2. Instala The Endex (1.5.3+)
3. Reinicia el servidor
4. La extensión se registra automáticamente — ¡no se necesita `/papi ecloud download`!

## Referencia de Placeholders

### Placeholders de Objetos del Mercado

Usa nombres de materiales para obtener datos de objetos específicos.

| Placeholder | Descripción | Salida de Ejemplo |
|-------------|-------------|-------------------|
| `%endex_price_<MATERIAL>%` | Precio actual | `800.00` |
| `%endex_price_formatted_<MATERIAL>%` | Precio con símbolo de moneda | `$800.00` |
| `%endex_change_<MATERIAL>%` | Cambio de precio desde última actualización | `+5.25%` |
| `%endex_trend_<MATERIAL>%` | Flecha de tendencia | `↑`, `↓`, o `→` |
| `%endex_supply_<MATERIAL>%` | Oferta actual | `1,234` |
| `%endex_demand_<MATERIAL>%` | Demanda actual | `567` |

**Ejemplos:**
```
%endex_price_DIAMOND%           → 800.00
%endex_price_formatted_DIAMOND% → $800.00
%endex_trend_DIAMOND%           → ↑
%endex_change_DIAMOND%          → +5.25%
%endex_supply_IRON_INGOT%       → 1,234
%endex_demand_GOLD_INGOT%       → 567
```

### Objetos por Precio Clasificados (1-10)

Muestra los objetos más caros y más baratos del mercado.

| Placeholder | Descripción |
|-------------|-------------|
| `%endex_top_price_<N>%` | Nombre del N-ésimo objeto más caro |
| `%endex_top_price_<N>_value%` | Precio del N-ésimo objeto más caro |
| `%endex_bottom_price_<N>%` | Nombre del N-ésimo objeto más barato |
| `%endex_bottom_price_<N>_value%` | Precio del N-ésimo objeto más barato |

**Ejemplos:**
```
%endex_top_price_1%        → Lingote de Netherite
%endex_top_price_1_value%  → 2,000.00
%endex_top_price_2%        → Diamante
%endex_top_price_2_value%  → 800.00
%endex_bottom_price_1%     → Piedra
%endex_bottom_price_1_value% → 4.00
```

### Mayores Ganadores y Perdedores (1-10)

Muestra objetos con los mayores cambios de precio.

| Placeholder | Descripción |
|-------------|-------------|
| `%endex_top_gainer_<N>%` | Nombre del N-ésimo mayor ganador |
| `%endex_top_gainer_<N>_change%` | Cambio porcentual del N-ésimo ganador |
| `%endex_top_loser_<N>%` | Nombre del N-ésimo mayor perdedor |
| `%endex_top_loser_<N>_change%` | Cambio porcentual del N-ésimo perdedor |

### Placeholders de Tenencias del Jugador

Muestra información sobre las tenencias virtuales de un jugador.

<Note>
Estos placeholders requieren que un jugador esté en línea y son relativos al jugador que ve.
</Note>

| Placeholder | Descripción |
|-------------|-------------|
| `%endex_holdings_total%` | Valor total de tenencias del jugador |
| `%endex_holdings_count%` | Objetos totales en tenencias |
| `%endex_holdings_top_<N>%` | Nombre del N-ésimo objeto más valioso en tenencias |
| `%endex_holdings_top_<N>_value%` | Valor del N-ésimo objeto más valioso |
| `%endex_holdings_top_<N>_amount%` | Cantidad del N-ésimo objeto más valioso |

### Tabla de Clasificación de Tenencias (1-10)

Muestra los jugadores más ricos por valor total de tenencias.

| Placeholder | Descripción |
|-------------|-------------|
| `%endex_top_holdings_<N>%` | N-ésimo jugador más rico por tenencias (nombre) |
| `%endex_top_holdings_<N>_value%` | Valor de tenencias del N-ésimo jugador más rico |

### Estadísticas del Mercado

Estadísticas de todo el mercado.

| Placeholder | Descripción | Salida de Ejemplo |
|-------------|-------------|-------------------|
| `%endex_total_items%` | Total de objetos en el mercado | `57` |
| `%endex_total_volume%` | Suma de todos los precios de objetos | `25,430.00` |
| `%endex_average_price%` | Precio promedio de objetos | `446.14` |
| `%endex_active_events%` | Número de eventos de mercado activos | `2` |

## Casos de Uso

### Ejemplo de Scoreboard

Usa plugins como [TAB](https://www.spigotmc.org/resources/tab-1-5-x-1-21-1.57806/) o [AnimatedScoreboard](https://www.spigotmc.org/resources/animatedscoreboard.20848/) para crear un scoreboard del mercado:

```yaml
scoreboard:
  title: "&6⚡ Mercado"
  lines:
    - "&7Tus Tenencias:"
    - "&f%endex_holdings_total%"
    - ""
    - "&7Diamante: &f%endex_price_DIAMOND% %endex_trend_DIAMOND%"
    - "&7Esmeralda: &f%endex_price_EMERALD% %endex_trend_EMERALD%"
    - "&7Hierro: &f%endex_price_IRON_INGOT% %endex_trend_IRON_INGOT%"
    - ""
    - "&7Mayor Ganador: &a%endex_top_gainer_1%"
    - "&7  %endex_top_gainer_1_change%"
```

### Ejemplo de Holograma

Usa [DecentHolograms](https://www.spigotmc.org/resources/decentholograms.96927/) o [HolographicDisplays](https://dev.bukkit.org/projects/holographic-displays) para mostrar información del mercado.

### Ejemplo de Tablist

Usa [TAB](https://www.spigotmc.org/resources/tab.57806/) para mostrar información del mercado en la tab.

## Solución de Problemas

### Placeholders No Funcionan

1. **Verifica que PlaceholderAPI esté instalado:**
   ```
   /papi info
   ```

2. **Verifica que la extensión de The Endex esté cargada:**
   ```
   /papi list
   ```
   Busca `endex` en la lista.

3. **Prueba un placeholder:**
   ```
   /papi parse me %endex_total_items%
   ```

## Páginas Relacionadas

- [Sistema de Tenencias](/es/features/holdings) — Aprende sobre tenencias virtuales
- [Configuración](/es/reference/configuration) — Ajustes del plugin
- [Comandos](/es/reference/commands) — Comandos disponibles
