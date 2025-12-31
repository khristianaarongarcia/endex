---
title: "Конфигурация"
description: "Подробное руководство по файлам конфигурации"
---

# Конфигурация

The Endex легко настраивается через YAML файлы, расположенные в папке `plugins/TheEndex/`.

## config.yml

Основной файл для глобальных настроек.

```yaml
# Общие настройки
plugin-prefix: "&8[&5The Endex&8] "
language: ru # Язык (en, fr, es, de, jp, ko, pt-BR, ru и т.д.)
debug-mode: false

# Экономика
economy:
  currency-symbol: "$"
  format: "#,##0.00"
  
# Настройки рынка
market:
  update-interval: 60 # Секунды между обновлениями цен
  save-interval: 300 # Секунды между автосохранениями
  
# Система портфеля
holdings:
  enabled: true
  tax-rate: 0.05

# Веб-сервер (Панель)
web:
  enabled: true
  port: 8080
  host: "0.0.0.0"
```

## items.yml

Определяет все торгуемые предметы и их свойства.

```yaml
DIAMOND:
  material: DIAMOND # Материал Minecraft
  display-name: "&bАлмаз" # Отображаемое имя (опционально)
  base-price: 100.0
  min-price: 10.0
  max-price: 500.0
  volatility: 0.5
  stock: 1000 # Начальный виртуальный запас
  elasticity: 1.0 # Чувствительность к изменениям запаса
  
GOLD_INGOT:
  material: GOLD_INGOT
  base-price: 50.0
  volatility: 0.3
```

## events.yml

Настраивает случайные рыночные события.

```yaml
events:
  market_crash:
    display-name: "&cКрах рынка"
    chance: 0.005
    duration: 1200
    global-multiplier: 0.7 # Все цены x0.7
    
  gold_rush:
    display-name: "&6Золотая лихорадка"
    items: [GOLD_INGOT, GOLD_BLOCK]
    price-multiplier: 0.5
```

## messages.yml

Содержит весь текст, отображаемый игрокам. Вы можете перевести или настроить сообщения здесь. Поддерживает цветовые коды (`&` или MiniMessage в зависимости от версии).

```yaml
prefix: "&8[&5The Endex&8] "
buy-success: "&aВы купили &e%amount%x %item%&a за &e%price%&a."
sell-success: "&aВы продали &e%amount%x %item%&a за &e%price%&a."
insufficient-funds: "&cНедостаточно средств."
```
