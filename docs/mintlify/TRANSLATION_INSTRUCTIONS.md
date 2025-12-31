# The Endex Documentation Multi-Language Translation Guide

This document outlines the complete plan for translating The Endex Mintlify documentation into multiple languages.

## Overview

**Goal:** Translate all documentation pages into 9 languages to match our plugin's translated config files.

**Target Languages:**
| Language | Code | Flag |
|----------|------|------|
| English | `en` | 🇺🇸 (Default) |
| Chinese (Simplified) | `cn` | 🇨🇳 |
| Spanish | `es` | 🇪🇸 |
| French | `fr` | 🇫🇷 |
| German | `de` | 🇩🇪 |
| Japanese | `jp` | 🇯🇵 |
| Korean | `ko` | 🇰🇷 |
| Portuguese (Brazil) | `pt-BR` | 🇧🇷 |
| Russian | `ru` | 🇷🇺 |

---

## File Structure

### Current Structure
```
docs/mintlify/
├── docs.json
├── introduction.md
├── getting-started/
│   ├── installation.md
│   └── quick-start.md
├── features/
│   ├── pricing.md
│   ├── custom-shop.md
│   ├── holdings.md
│   ├── delivery.md
│   ├── events.md
│   ├── investments.md
│   └── placeholderapi.md
├── reference/
│   ├── commands.md
│   ├── configuration.md
│   └── permissions.md
├── web-api/
│   ├── dashboard.md
│   └── rest-api.md
├── developers/
│   ├── api.md
│   └── addons.md
└── support/
    ├── faq.md
    ├── changelog.md
    └── troubleshooting.md
```

### New Structure (After Translation)
```
docs/mintlify/
├── docs.json                    # Updated with languages array
├── TRANSLATION_INSTRUCTIONS.md  # This file
│
├── en/                          # English (default)
│   ├── introduction.md
│   ├── getting-started/
│   ├── features/
│   ├── reference/
│   ├── web-api/
│   ├── developers/
│   └── support/
│
├── cn/                          # Chinese (Simplified)
│   ├── introduction.md
│   ├── getting-started/
│   ├── features/
│   ├── reference/
│   ├── web-api/
│   ├── developers/
│   └── support/
│
├── es/                          # Spanish
│   └── ... (same structure)
│
├── fr/                          # French
│   └── ... (same structure)
│
├── de/                          # German
│   └── ... (same structure)
│
├── jp/                          # Japanese
│   └── ... (same structure)
│
├── ko/                          # Korean
│   └── ... (same structure)
│
├── pt-BR/                       # Portuguese (Brazil)
│   └── ... (same structure)
│
└── ru/                          # Russian
    └── ... (same structure)
```

---

## Pages to Translate (18 total per language)

### Section 1: Core Pages
- [ ] `introduction.md` - Welcome page with overview

### Section 2: Getting Started (2 pages)
- [ ] `getting-started/installation.md`
- [ ] `getting-started/quick-start.md`

### Section 3: Features (7 pages)
- [ ] `features/pricing.md`
- [ ] `features/custom-shop.md`
- [ ] `features/holdings.md`
- [ ] `features/delivery.md`
- [ ] `features/events.md`
- [ ] `features/investments.md`
- [ ] `features/placeholderapi.md`

### Section 4: Reference (3 pages)
- [ ] `reference/commands.md`
- [ ] `reference/configuration.md`
- [ ] `reference/permissions.md`

### Section 5: Web API (2 pages)
- [ ] `web-api/dashboard.md`
- [ ] `web-api/rest-api.md`

### Section 6: Developers (2 pages)
- [ ] `developers/api.md`
- [ ] `developers/addons.md`

### Section 7: Support (3 pages)
- [ ] `support/faq.md`
- [ ] `support/changelog.md` (Keep code examples in English)
- [ ] `support/troubleshooting.md`

**Total: 18 pages × 9 languages = 162 translated files**

---

## docs.json Configuration

The `docs.json` must be updated to use the `languages` navigation structure:

```json
{
  "$schema": "https://mintlify.com/docs.json",
  "name": "The Endex",
  "theme": "mint",
  "colors": {
    "primary": "#7c3aed"
  },
  "navigation": {
    "languages": [
      {
        "language": "en",
        "groups": [
          {
            "group": "Getting Started",
            "pages": [
              "en/introduction",
              "en/getting-started/installation",
              "en/getting-started/quick-start"
            ]
          },
          {
            "group": "Features",
            "pages": [
              "en/features/pricing",
              "en/features/custom-shop",
              "en/features/holdings",
              "en/features/delivery",
              "en/features/events",
              "en/features/investments",
              "en/features/placeholderapi"
            ]
          },
          {
            "group": "Reference",
            "pages": [
              "en/reference/commands",
              "en/reference/configuration",
              "en/reference/permissions"
            ]
          },
          {
            "group": "Web API",
            "pages": [
              "en/web-api/dashboard",
              "en/web-api/rest-api"
            ]
          },
          {
            "group": "Developers",
            "pages": [
              "en/developers/api",
              "en/developers/addons"
            ]
          },
          {
            "group": "Support",
            "pages": [
              "en/support/faq",
              "en/support/changelog",
              "en/support/troubleshooting"
            ]
          }
        ]
      },
      {
        "language": "cn",
        "groups": [
          {
            "group": "入门指南",
            "pages": [
              "cn/introduction",
              "cn/getting-started/installation",
              "cn/getting-started/quick-start"
            ]
          },
          {
            "group": "功能",
            "pages": [
              "cn/features/pricing",
              "cn/features/custom-shop",
              "cn/features/holdings",
              "cn/features/delivery",
              "cn/features/events",
              "cn/features/investments",
              "cn/features/placeholderapi"
            ]
          },
          {
            "group": "参考",
            "pages": [
              "cn/reference/commands",
              "cn/reference/configuration",
              "cn/reference/permissions"
            ]
          },
          {
            "group": "网页API",
            "pages": [
              "cn/web-api/dashboard",
              "cn/web-api/rest-api"
            ]
          },
          {
            "group": "开发者",
            "pages": [
              "cn/developers/api",
              "cn/developers/addons"
            ]
          },
          {
            "group": "支持",
            "pages": [
              "cn/support/faq",
              "cn/support/changelog",
              "cn/support/troubleshooting"
            ]
          }
        ]
      }
      // ... more languages follow same pattern
    ]
  }
}
```

---

## Translation Progress Tracker

### Phase 1: Setup & English Base ✅
- [x] Create instruction document
- [ ] Move existing files to `en/` folder
- [ ] Update `docs.json` with languages structure

### Phase 2: Chinese (Simplified) - `cn/`
- [ ] `cn/introduction.md`
- [ ] `cn/getting-started/installation.md`
- [ ] `cn/getting-started/quick-start.md`
- [ ] `cn/features/pricing.md`
- [ ] `cn/features/custom-shop.md`
- [ ] `cn/features/holdings.md`
- [ ] `cn/features/delivery.md`
- [ ] `cn/features/events.md`
- [ ] `cn/features/investments.md`
- [ ] `cn/features/placeholderapi.md`
- [ ] `cn/reference/commands.md`
- [ ] `cn/reference/configuration.md`
- [ ] `cn/reference/permissions.md`
- [ ] `cn/web-api/dashboard.md`
- [ ] `cn/web-api/rest-api.md`
- [ ] `cn/developers/api.md`
- [ ] `cn/developers/addons.md`
- [ ] `cn/support/faq.md`
- [ ] `cn/support/changelog.md`
- [ ] `cn/support/troubleshooting.md`

### Phase 3: Spanish - `es/`
- [ ] All 18 pages

### Phase 4: French - `fr/`
- [ ] All 18 pages

### Phase 5: German - `de/`
- [ ] All 18 pages

### Phase 6: Japanese - `jp/`
- [ ] All 18 pages

### Phase 7: Korean - `ko/`
- [ ] All 18 pages

### Phase 8: Portuguese (Brazil) - `pt-BR/`
- [ ] All 18 pages

### Phase 9: Russian - `ru/`
- [ ] All 18 pages

### Phase 10: Final Steps
- [ ] Update `docs.json` with all languages
- [ ] Test locally with `mint dev`
- [ ] Commit and push to GitHub
- [ ] Verify Mintlify deployment

---

## Group Name Translations

| English | Chinese | Spanish | French | German | Japanese | Korean | Portuguese | Russian |
|---------|---------|---------|--------|--------|----------|--------|------------|---------|
| Getting Started | 入门指南 | Primeros Pasos | Démarrage | Erste Schritte | はじめに | 시작하기 | Começando | Начало работы |
| Features | 功能 | Características | Fonctionnalités | Funktionen | 機能 | 기능 | Recursos | Возможности |
| Reference | 参考 | Referencia | Référence | Referenz | リファレンス | 레퍼런스 | Referência | Справочник |
| Web API | 网页API | API Web | API Web | Web-API | Web API | 웹 API | API Web | Веб API |
| Developers | 开发者 | Desarrolladores | Développeurs | Entwickler | 開発者 | 개발자 | Desenvolvedores | Разработчикам |
| Support | 支持 | Soporte | Support | Support | サポート | 지원 | Suporte | Поддержка |

---

## Translation Guidelines

### DO:
- Translate all prose/descriptions
- Translate table headers and descriptions
- Translate comments in code blocks (but not the code itself)
- Translate UI text references
- Keep technical terms consistent within each language

### DON'T:
- Translate command names (`/market`, `/endex`, etc.)
- Translate config keys (`holdings.enabled`, `web.port`, etc.)
- Translate permission nodes (`theendex.admin`, etc.)
- Translate code examples (keep original syntax)
- Translate file paths or URLs
- Translate Material names (`DIAMOND`, `IRON_INGOT`, etc.)

### Example:
```yaml
# English
holdings:
  enabled: true  # Enable the virtual holdings system

# Chinese - DON'T translate keys, DO translate comments
holdings:
  enabled: true  # 启用虚拟持仓系统
```

---

## Deployment

After completing translations:

1. **Commit all changes:**
   ```bash
   git add docs/mintlify/
   git commit -m "feat(docs): Add multi-language support for 9 languages"
   ```

2. **Push to GitHub:**
   ```bash
   git push origin main
   ```

3. **Mintlify auto-deploys** from GitHub on push to main branch.

4. **Verify** at: https://lokixcz-plugins.kagsystems.tech/

---

## Notes

- Total estimated work: 162 translated files
- Recommended approach: Complete one language at a time
- The changelog can share most content (version numbers, code) but descriptions should be translated
- Consider using machine translation as a base, then review for accuracy
