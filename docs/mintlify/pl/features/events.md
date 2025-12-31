---
title: "Wydarzenia Rynkowe"
description: "Wydarzenia czasowe, które nakładają mnożniki cen na określone kategorie lub cały rynek."
---

Wydarzenia rynkowe to wyzwalane przez administratora mnożniki cen, które tworzą tymczasowe okazje kupna/sprzedaży.

Wydarzenia są zdefiniowane w `plugins/TheEndex/events.yml`.

## Komendy

```text
/market event list
/market event <name>
/market event end <name>
/market event clear
```

<Tip>
Cykliczne wydarzenia (tygodniowa gorączka rudy, sezonowe zbiory itp.) to świetny sposób na utrzymanie aktywności handlowej.
</Tip>

## Przykładowa konfiguracja

```yaml
events:
  ore_rush:
    display-name: "&6⛏ Gorączka Rudy!"
    description: "Materiały górnicze są bardzo poszukiwane!"
    duration: 3600
    broadcast: true
    multipliers:
      DIAMOND: 1.5
      EMERALD: 1.5
      GOLD_INGOT: 1.3
```

## Jak działają wydarzenia

Efektywna cena to cena bazowa pomnożona przez połączony mnożnik wydarzenia.

<Info>
Zobacz `docs/EVENTS.md` w repozytorium dla pełnego schematu i zasad łączenia.
</Info>

## Zaplanowane Wydarzenia

Chociaż The Endex nie ma wbudowanego harmonogramu, możesz użyć:

### Zewnętrzne Harmonogramy

Użyj cron jobs lub harmonogramów serwera, aby wyzwalać wydarzenia:

```bash
# Codziennie o 18:00
0 18 * * * screen -S minecraft -X stuff "/market event ore_rush^M"
```

### Inne Pluginy

Pluginy takie jak **DeluxeScheduler** lub **ScheduledCommands** mogą wyzwalać:
```
/market event <name>
```

---

## Uprawnienia

| Uprawnienie | Opis |
|------------|-------------|
| `theendex.admin` | Zarządzanie wydarzeniami |

---

## Strategie Graczy

<Tip>
**Kupuj Przed Wydarzeniami**  
Jeśli wiesz, że nadchodzi wydarzenie, kup przedmioty, których dotyczy, wcześniej.
</Tip>

<Info>
**Obserwuj Ogłoszenia**  
Wydarzenia są ogłaszane na czacie. Reaguj szybko, aby zyskać!
</Info>

<Warning>
**Krachy Rynkowe**  
Podczas krachów rynkowych często lepiej jest trzymać niż sprzedawać po niskich cenach.
</Warning>

---

## Wskazówki dla Administratorów

<Tip>
**Regularne Wydarzenia**  
Planuj wydarzenia, aby utrzymać ekonomię ekscytującą. Tygodniowe wydarzenia działają dobrze.
</Tip>

<Info>
**Motywy Sezonowe**  
Dopasuj wydarzenia do pór roku w świecie rzeczywistym lub na serwerze dla immersji.
</Info>

<Warning>
**Balans Mnożników**  
Ekstremalne mnożniki (większe niż 2.0 lub mniejsze niż 0.5) mogą zdestabilizować ekonomię. Używaj oszczędnie.
</Warning>

---

## Powiązane Strony

- [Dynamiczne Ceny](pricing) — Jak wydarzenia wpływają na ceny
- [Konfiguracja](../reference/configuration) — Pełna referencja events.yml
- [Komendy](../reference/commands) — Komendy wydarzeń
