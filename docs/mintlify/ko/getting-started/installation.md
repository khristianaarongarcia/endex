---
title: "설치"
description: "서버에 The Endex를 설치하는 방법"
---

# 설치 가이드

다음 단계에 따라 Minecraft 서버에 The Endex를 설치하세요.

## 전제 조건

시작하기 전에 다음이 준비되어 있는지 확인하세요:

- **Spigot**, **Paper**, 또는 **Purpur**를 실행하는 Minecraft 서버 (버전 1.16 - 1.21+).
- **Java 17** 이상 설치.
- **Vault** 호환 경제 플러그인 (예: EssentialsX).
- **Vault** 설치됨.
- **PlaceholderAPI** (선택 사항이지만 플레이스홀더 사용에 권장됨).

## 설치 단계

<Steps>
  <Step title="플러그인 다운로드">
    [SpigotMC](https://www.spigotmc.org/) 또는 [Modrinth](https://modrinth.com/)에서 최신 버전의 `TheEndex.jar`를 다운로드합니다.
  </Step>
  <Step title="파일 설치">
    다운로드한 `.jar` 파일을 서버의 `plugins` 폴더에 넣습니다.
  </Step>
  <Step title="서버 재시작">
    서버를 재시작하여 플러그인을 로드합니다. `/reload`는 사용하지 마세요.
  </Step>
  <Step title="설치 확인">
    콘솔에서 The Endex 시작 메시지를 확인하거나 게임 내에서 `/plugins`를 입력하여 The Endex가 녹색으로 표시되는지 확인합니다.
  </Step>
</Steps>

## 초기 구성

첫 실행 후 `plugins` 디렉토리에 `TheEndex` 폴더가 생성됩니다. 이 폴더에는 다음이 포함됩니다:

- `config.yml`: 메인 설정 파일.
- `messages.yml`: 게임 내 메시지 번역 파일.
- `items.yml`: 아이템 및 기본 가격 설정.

설치 사용자 정의에 대한 자세한 내용은 [구성](/ko/reference/configuration) 섹션을 참조하세요.
