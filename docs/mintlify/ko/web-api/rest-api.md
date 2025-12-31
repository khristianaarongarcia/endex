---
title: "REST API"
description: "개발자용 HTTP API 문서"
---

# REST API

The Endex는 외부 개발자가 시장 데이터와 상호 작용할 수 있도록 RESTful API를 제공합니다. 통계 웹사이트, Discord 봇 또는 모바일 앱을 만드는 데 유용합니다.

## 엔드포인트 (기본 URL)

`http://<서버 IP>:<포트>/api/v1`

## 엔드포인트

### 모든 아이템 가져오기

`GET /items`

추적 중인 모든 아이템 목록을 반환합니다.

**응답:**
```json
[
  {
    "id": "DIAMOND",
    "name": "다이아몬드",
    "price": 105.50,
    "basePrice": 100.0,
    "trend": "UP",
    "change": 5.5
  },
  {
    "id": "GOLD_INGOT",
    "name": "금괴",
    "price": 48.20,
    "basePrice": 50.0,
    "trend": "DOWN",
    "change": -3.6
  }
]
```

### 특정 아이템 가져오기

`GET /items/{id}`

**예시:** `GET /items/DIAMOND`

**응답:**
```json
{
  "id": "DIAMOND",
  "name": "다이아몬드",
  "price": 105.50,
  "history": [
    {"time": 1620000000, "price": 100.0},
    {"time": 1620003600, "price": 102.0},
    {"time": 1620007200, "price": 105.5}
  ]
}
```

### 시장 상태 가져오기

`GET /status`

시장의 전역 상태(진행 중인 이벤트 등)를 반환합니다.

**응답:**
```json
{
  "status": "OPEN",
  "activeEvents": [],
  "lastUpdate": 1620007200
}
```

## 인증

현재 API는 기본적으로 공개되어 있으며 읽기 전용입니다. 향후 버전에는 쓰기 작업(POST/PUT)을 위한 API 키 인증이 포함될 예정입니다.

## 속도 제한 (Rate Limiting)

하드코딩된 속도 제한은 없지만 많은 요청을 하는 경우 상식적으로 사용하고 결과를 캐시하세요.
