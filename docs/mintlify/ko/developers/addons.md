---
title: "애드온"
description: "The Endex 기능 확장"
---

# 애드온 시스템

The Endex는 모듈식 애드온 시스템을 지원하여 메인 플러그인을 비대하게 만들지 않고도 특정 기능을 추가할 수 있습니다.

## 공식 애드온

### 1. CryptoAddon
시장에 가상 암호화폐(비트코인, 이더리움 등)를 추가합니다.
- **기능**: 실제 API(CoinGecko) 기반 가격, 개별 암호화폐 지갑.
- **설치**: `Endex-Crypto.jar`를 `plugins/TheEndex/addons/`에 넣습니다.

### 2. DiscordLink
시장을 Discord 서버에 연결합니다.
- **기능**: Discord 슬래시 명령어(`/price`), 채널 내 붕괴 알림, 자산 기반 역할 동기화.

## 애드온 만들기

개발자는 `EndexAddon` 클래스를 확장하여 자체 애드온을 만들 수 있습니다.

### 기본 구조

```java
public class MyCustomAddon extends EndexAddon {
    
    @Override
    public void onEnable() {
        getLogger().info("내 애드온이 활성화되었습니다!");
    }
    
    @Override
    public void onDisable() {
        // 정리
    }
}
```

### 로드

애드온은 `plugins/TheEndex/addons/` 폴더에 있어야 합니다. 메인 플러그인 시작 시 자동으로 로드됩니다.

## 마켓플레이스

(곧 출시 예정) 다른 사용자가 만든 애드온을 다운로드할 수 있는 커뮤니티 마켓플레이스입니다.
