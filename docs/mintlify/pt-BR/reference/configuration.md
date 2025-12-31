---
title: "Configuração"
description: "Guia detalhado dos arquivos de configuração"
---

# Configuração

O The Endex é altamente configurável através de arquivos YAML localizados na pasta `plugins/TheEndex/`.

## config.yml

O arquivo principal para configurações globais.

```yaml
# Configurações Gerais
plugin-prefix: "&8[&5The Endex&8] "
language: pt-BR # Idioma (en, fr, es, de, jp, ko, pt-BR, etc.)
debug-mode: false

# Economia
economy:
  currency-symbol: "$"
  format: "#,##0.00"
  
# Configurações de Mercado
market:
  update-interval: 60 # Segundos entre atualizações de preço
  save-interval: 300 # Segundos entre salvamentos automáticos
  
# Sistema de Portfólio
holdings:
  enabled: true
  tax-rate: 0.05

# Servidor Web (Dashboard)
web:
  enabled: true
  port: 8080
  host: "0.0.0.0"
```

## items.yml

Define todos os itens negociáveis e suas propriedades.

```yaml
DIAMOND:
  material: DIAMOND # Material do Minecraft
  display-name: "&bDiamante" # Nome de exibição (opcional)
  base-price: 100.0
  min-price: 10.0
  max-price: 500.0
  volatility: 0.5
  stock: 1000 # Estoque inicial virtual
  elasticity: 1.0 # Sensibilidade a mudanças de estoque
  
GOLD_INGOT:
  material: GOLD_INGOT
  base-price: 50.0
  volatility: 0.3
```

## events.yml

Configura eventos de mercado aleatórios.

```yaml
events:
  market_crash:
    display-name: "&cColapso do Mercado"
    chance: 0.005
    duration: 1200
    global-multiplier: 0.7 # Todos os preços x0.7
    
  gold_rush:
    display-name: "&6Corrida do Ouro"
    items: [GOLD_INGOT, GOLD_BLOCK]
    price-multiplier: 0.5
```

## messages.yml

Contém todo o texto exibido aos jogadores. Você pode traduzir ou personalizar mensagens aqui. Suporta códigos de cores (`&` ou MiniMessage dependendo da versão).

```yaml
prefix: "&8[&5The Endex&8] "
buy-success: "&aVocê comprou &e%amount%x %item%&a por &e%price%&a."
sell-success: "&aVocê vendeu &e%amount%x %item%&a por &e%price%&a."
insufficient-funds: "&cFundos insuficientes."
```
