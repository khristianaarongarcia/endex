---
title: "PlaceholderAPI"
description: "The Endex 플레이스홀더 사용"
---

# PlaceholderAPI

The Endex는 **PlaceholderAPI** (PAPI)를 완벽하게 지원하여 시장 정보를 어디에나 표시할 수 있습니다(스코어보드, 채팅, 탭 목록, 홀로그램, DeluxeMenus 메뉴 등).

## 설치

다음이 있는지 확인하세요:
1. PlaceholderAPI가 설치되어 있음.
2. The Endex가 설치되어 있음.
3. `/papi ecloud download TheEndex`를 실행하거나(ecloud에서 사용 가능한 경우), The Endex가 자체 플레이스홀더를 등록하므로 단순히 `/papi reload`로 PAPI를 리로드합니다.

## 플레이스홀더 목록

### 아이템 플레이스홀더

`<item>`을 아이템 ID(예: `DIAMOND`, `OAK_LOG`)로 바꾸세요.

| 플레이스홀더 | 설명 |
|--------------|------|
| `%endex_price_<item>%` | 아이템의 현재 가격. |
| `%endex_price_formatted_<item>%` | 통화가 포함된 형식화된 가격 (예: $100.50). |
| `%endex_change_<item>%` | 가격 변동률 (예: +5.2%). |
| `%endex_trend_<item>%` | 추세 아이콘 (예: 📈 또는 📉). |
| `%endex_base_price_<item>%` | 구성된 기본 가격. |
| `%endex_volume_<item>%` | 현재 거래량. |

### 플레이어 플레이스홀더

| 플레이스홀더 | 설명 |
|--------------|------|
| `%endex_holdings_value%` | 플레이어 포트폴리오의 총 가치. |
| `%endex_holdings_count_<item>%` | 특정 아이템의 보유 수량. |
| `%endex_profit_loss%` | 플레이어의 총 손익 (P&L). |

### 글로벌 플레이스홀더

| 플레이스홀더 | 설명 |
|--------------|------|
| `%endex_top_gainer%` | 가장 많이 상승한 아이템 이름. |
| `%endex_top_loser%` | 가장 많이 하락한 아이템 이름. |
| `%endex_market_status%` | 시장 상태 (개장/폐장/붕괴). |

## 사용 예시

**스코어보드 내:**
```yaml
lines:
  - "&6시장:"
  - " 다이아몬드: %endex_price_formatted_DIAMOND% %endex_trend_DIAMOND%"
  - " 금: %endex_price_formatted_GOLD_INGOT% %endex_trend_GOLD_INGOT%"
  - "&e포트폴리오: $%endex_holdings_value%"
```

**홀로그램 내:**
```text
비트코인 가격 (가상)
%endex_price_formatted_BTC%
```
