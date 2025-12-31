---
title: "구성"
description: "구성 파일 상세 가이드"
---

# 구성

The Endex는 `plugins/TheEndex/` 폴더 내의 YAML 파일을 통해 고도로 구성 가능합니다.

## config.yml

전역 설정을 위한 메인 파일입니다.

```yaml
# 일반 설정
plugin-prefix: "&8[&5The Endex&8] "
language: ko # 언어 (en, fr, es, de, jp, ko 등)
debug-mode: false

# 경제
economy:
  currency-symbol: "$"
  format: "#,##0.00"
  
# 시장 설정
market:
  update-interval: 60 # 가격 업데이트 간격(초)
  save-interval: 300 # 자동 저장 간격(초)
  
# 포트폴리오 시스템
holdings:
  enabled: true
  tax-rate: 0.05

# 웹 서버 (대시보드)
web:
  enabled: true
  port: 8080
  host: "0.0.0.0"
```

## items.yml

모든 거래 가능한 아이템과 그 속성을 정의합니다.

```yaml
DIAMOND:
  material: DIAMOND # Minecraft 재질
  display-name: "&b다이아몬드" # 표시 이름(선택 사항)
  base-price: 100.0
  min-price: 10.0
  max-price: 500.0
  volatility: 0.5
  stock: 1000 # 가상 초기 재고
  elasticity: 1.0 # 재고 변화에 대한 민감도
  
GOLD_INGOT:
  material: GOLD_INGOT
  base-price: 50.0
  volatility: 0.3
```

## events.yml

무작위 시장 이벤트를 구성합니다.

```yaml
events:
  market_crash:
    display-name: "&c시장 붕괴"
    chance: 0.005
    duration: 1200
    global-multiplier: 0.7 # 모든 가격 x0.7
    
  gold_rush:
    display-name: "&6골드 러시"
    items: [GOLD_INGOT, GOLD_BLOCK]
    price-multiplier: 0.5
```

## messages.yml

플레이어에게 표시되는 모든 텍스트가 포함되어 있습니다. 여기서 메시지를 번역하거나 사용자 정의할 수 있습니다. 색상 코드(`&` 또는 버전에 따라 MiniMessage)를 지원합니다.

```yaml
prefix: "&8[&5The Endex&8] "
buy-success: "&a당신은 &e%amount%x %item%&a를 &e%price%&a에 구매했습니다."
sell-success: "&a당신은 &e%amount%x %item%&a를 &e%price%&a에 판매했습니다."
insufficient-funds: "&c자금이 부족합니다."
```
