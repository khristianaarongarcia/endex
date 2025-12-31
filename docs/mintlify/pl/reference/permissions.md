---
title: "Uprawnienia"
description: "Uprawnienia używane przez The Endex i typowe konfiguracje ról serwera."
---

Węzły uprawnień mogą się nieznacznie różnić w zależności od wersji serwera i dodatków, ale te są najczęściej używane.

## Podstawowe

| Uprawnienie | Co robi |
| --- | --- |
| `theendex.admin` | Komendy administratora: przeładowanie, kontrola wydarzeń, diagnostyka |

## Web

Te pochodzą z przewodnika konfiguracji:

| Uprawnienie | Co robi |
| --- | --- |
| `endex.web.trade` | Pozwala na handel przez dashboard webowy |
| `endex.web.admin` | Pozwala na przeglądanie zasobów innych graczy przez endpointy API administratora |

## Dodatki

Uprawnienia dodatków zazwyczaj podążają za wzorcem:

- `endex.addon.<name>`
- `endex.addon.<name>.admin`

Jeśli piszesz dodatek, zdefiniuj swoje uprawnienia w plugin.yml swojego dodatku i udokumentuj je dla właścicieli serwerów.
