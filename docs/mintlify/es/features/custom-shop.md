---
title: "Tienda Personalizada"
description: "Crea tu propio diseño de mercado y tienda."
---

The Endex te permite personalizar completamente la apariencia y organización del mercado.

## Archivos de Configuración de GUI

Las GUIs se configuran en archivos separados bajo `guis/`:

```text
plugins/TheEndex/guis/
├── market.yml      # Interfaz principal del mercado
├── details.yml     # Panel de detalles del objeto
├── holdings.yml    # Panel de tenencias virtuales
└── deliveries.yml  # Cola de entregas
```

## Personalizar el Mercado Principal

Edita `guis/market.yml`:

```yaml
# Título de la GUI (soporta códigos de color)
title: "&6⚡ &lEl Mercado"

# Tamaño en filas (1-6)
rows: 6

# Objetos de relleno de fondo
background:
  enabled: true
  material: BLACK_STAINED_GLASS_PANE
  name: " "

# Posición de los botones
buttons:
  categories:
    slot: 4
    material: COMPASS
    name: "&eCategorías"
    
  search:
    slot: 49
    material: OAK_SIGN
    name: "&fBuscar"
    
  holdings:
    slot: 45
    material: CHEST
    name: "&6Mis Tenencias"
    
  next-page:
    slot: 53
    material: ARROW
    name: "&fPágina Siguiente"
    
  prev-page:
    slot: 45
    material: ARROW
    name: "&fPágina Anterior"
```

## Categorías

Define categorías para organizar objetos:

```yaml
categories:
  minerales:
    name: "&b⛏ Minerales"
    icon: DIAMOND_PICKAXE
    items:
      - DIAMOND
      - EMERALD
      - GOLD_INGOT
      - IRON_INGOT
      - COAL
      - LAPIS_LAZULI
      - REDSTONE
      
  agricultura:
    name: "&a🌾 Agricultura"
    icon: WHEAT
    items:
      - WHEAT
      - CARROT
      - POTATO
      - BEETROOT
      - MELON_SLICE
      - PUMPKIN
      
  combate:
    name: "&c⚔ Combate"
    icon: DIAMOND_SWORD
    items:
      - DIAMOND_SWORD
      - DIAMOND_CHESTPLATE
      - BOW
      - ARROW
      - SHIELD
```

## Formato de Objetos

Personaliza cómo se muestran los objetos:

```yaml
item-format:
  name: "&f{item_name}"
  lore:
    - "&7Precio: &a${price}"
    - "&7Tendencia: {trend}"
    - "&7Cambio: {change}"
    - ""
    - "&eClick Izquierdo: &7Comprar"
    - "&eClick Derecho: &7Vender"
    - "&eShift+Click: &7Detalles"
```

## Variables Disponibles

| Variable | Descripción |
|----------|-------------|
| `{item_name}` | Nombre del objeto |
| `{price}` | Precio actual |
| `{buy_price}` | Precio de compra |
| `{sell_price}` | Precio de venta |
| `{trend}` | Indicador de tendencia (↑↓→) |
| `{change}` | Cambio porcentual |
| `{supply}` | Oferta actual |
| `{demand}` | Demanda actual |

## Sonidos y Efectos

```yaml
sounds:
  open-menu: UI_BUTTON_CLICK
  buy-item: ENTITY_EXPERIENCE_ORB_PICKUP
  sell-item: ENTITY_VILLAGER_YES
  error: ENTITY_VILLAGER_NO
```

## Ejemplo Completo

```yaml
title: "&6⚡ &lMercado de {server_name}"
rows: 6

background:
  enabled: true
  material: GRAY_STAINED_GLASS_PANE
  name: " "

item-slots: [10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34]

buttons:
  categories:
    slot: 4
    material: COMPASS
    name: "&eCategorías"
    lore:
      - "&7Filtrar por tipo de objeto"
      
  my-holdings:
    slot: 45
    material: ENDER_CHEST
    name: "&dMis Tenencias"
    lore:
      - "&7Ver tus objetos almacenados"
      
  investments:
    slot: 46
    material: GOLD_INGOT
    name: "&6Inversiones"
    lore:
      - "&7Gestionar tus inversiones"

categories:
  todos:
    name: "&fTodos los Objetos"
    icon: CHEST
    items: []  # vacío = mostrar todos
```

## Recargar Cambios

Después de editar cualquier archivo de GUI:

```text
/endex reload
```

## Páginas Relacionadas

- [Configuración](../reference/configuration) — Referencia completa de config.yml
- [Precios Dinámicos](pricing) — Cómo funcionan los precios
- [Tenencias](holdings) — Sistema de almacenamiento virtual
