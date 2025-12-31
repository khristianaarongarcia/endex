---
title: "명령어"
description: "명령어 전체 목록 및 사용법"
---

# 명령어

The Endex에서 사용할 수 있는 전체 명령어 목록입니다.

## 플레이어 명령어

이 명령어들은 일반적으로 모든 플레이어가 액세스할 수 있습니다(권한에 따라 다름).

| 명령어 | 별칭 | 설명 | 권한 |
|--------|------|------|------|
| `/endex` | `/market` | 메인 시장 메뉴를 엽니다. | `endex.use` |
| `/endex help` | | 도움말을 표시합니다. | `endex.help` |
| `/endex price <아이템>` | | 아이템의 현재 가격을 표시합니다. | `endex.price` |
| `/endex buy <아이템> <수량>` | | 시장 가격으로 아이템을 구매합니다. | `endex.buy` |
| `/endex sell <아이템> <수량>` | | 시장 가격으로 아이템을 판매합니다. | `endex.sell` |
| `/endex sellall` | | 호환되는 인벤토리 전체를 판매합니다. | `endex.sellall` |
| `/holdings` | `/portfolio` | 포트폴리오 메뉴를 엽니다. | `endex.holdings` |
| `/endex invest <아이템> <수량>` | | 보유 자산을 구매합니다(가상). | `endex.invest` |
| `/endex divest <아이템> <수량>` | | 보유 자산을 판매합니다(가상). | `endex.divest` |

## 관리자 명령어

이 명령어들은 관리자 및 운영자 전용입니다.

| 명령어 | 설명 | 권한 |
|--------|------|------|
| `/endex admin reload` | 구성을 리로드합니다. | `endex.admin.reload` |
| `/endex admin additem <가격>` | 손에 든 아이템을 시장에 추가합니다. | `endex.admin.additem` |
| `/endex admin removeitem <아이템>` | 시장에서 아이템을 제거합니다. | `endex.admin.removeitem` |
| `/endex admin setprice <아이템> <가격>` | 아이템 가격을 강제로 설정합니다. | `endex.admin.setprice` |
| `/endex admin setvolatility <아이템> <값>` | 아이템의 변동성을 변경합니다. | `endex.admin.setvolatility` |
| `/endex admin event start <이벤트>` | 이벤트를 강제로 시작합니다. | `endex.admin.event` |
| `/endex admin event stop` | 진행 중인 이벤트를 중지합니다. | `endex.admin.event` |
| `/endex admin debug` | 콘솔 디버그 모드를 활성화합니다. | `endex.admin.debug` |

## 인수

- `<아이템>`: 아이템의 내부 이름(예: `DIAMOND`, `IRON_INGOT`). `items.yml`과 일치해야 합니다.
- `<수량>`: 수량(정수).
- `<가격>`: 가격(소수).
