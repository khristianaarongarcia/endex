---
title: "권한"
description: "권한 노드 목록"
---

# 권한

이 권한을 사용하여 The Endex 기능에 대한 액세스를 관리하세요.

## 사용자 권한

이 권한은 기본적으로 플레이어에게 권장됩니다.

| 권한 | 설명 | 권장 |
|------|------|------|
| `endex.use` | 플러그인 사용 및 GUI 열기를 허용합니다. | **예** |
| `endex.help` | 도움말 보기를 허용합니다. | **예** |
| `endex.price` | 가격 확인을 허용합니다. | **예** |
| `endex.buy` | 아이템 구매를 허용합니다. | **예** |
| `endex.sell` | 아이템 판매를 허용합니다. | **예** |
| `endex.sellall` | `/endex sellall` 사용을 허용합니다. | **예** |
| `endex.holdings` | 포트폴리오 액세스를 허용합니다. | **예** |
| `endex.invest` | 투자(보유 자산 구매)를 허용합니다. | **예** |
| `endex.divest` | 투자 회수(보유 자산 판매)를 허용합니다. | **예** |
| `endex.history` | 가격 기록 보기를 허용합니다. | **예** |

## 관리자 권한

⚠️ **경고**: 이 권한은 신뢰할 수 있는 관리자에게만 부여하세요.

| 권한 | 설명 |
|------|------|
| `endex.admin.*` | 모든 관리자 권한을 부여합니다. |
| `endex.admin.reload` | 구성 리로드를 허용합니다. |
| `endex.admin.additem` | 시장에 아이템 추가를 허용합니다. |
| `endex.admin.removeitem` | 아이템 제거를 허용합니다. |
| `endex.admin.setprice` | 가격 조작을 허용합니다. |
| `endex.admin.event` | 이벤트 시작/중지를 허용합니다. |
| `endex.admin.debug` | 디버그 정보 보기를 허용합니다. |
| `endex.admin.update` | 업데이트 알림을 받습니다. |

## 특별 권한

| 권한 | 설명 |
|------|------|
| `endex.bypass.tax` | 플레이어는 보유 자산에 대한 세금을 내지 않습니다. |
| `endex.bypass.limit` | 플레이어는 보유 자산 제한이 없습니다. |
