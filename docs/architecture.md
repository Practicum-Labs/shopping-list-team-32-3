# Список покупок — архитектура и структура проекта

> Требования к **структуре и проектированию**: технологический стек, слои, MVI,
> раскладка пакетов, DI, навигация (техническая часть), статический анализ и
> релизная сборка. Функциональные требования — в [functional-requirements.md](functional-requirements.md),
> дизайн и экраны — в [design.md](design.md).

**Платформа:** Android

---

## 1. Технологический стек

| Слой        | Решение                                                               | Обоснование                                                                                                                                                     |
|-------------|-----------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| UI          | **Jetpack Compose** (Kotlin 2.2, плагин Compose)                      | Нативная декларативность,переиспользуемость, Поддержка Material‑3, Hot‑Reload и Compose Preview                                                                 |
| Архитектура | **MVI** в `ui` + слои `data / domain / ui`                            | Однонаправленный поток данных, слои изолированы, гибкость, тестируемость                                                                                        |
| База данных | **Room**                                                              | DAO‑интерфейсы и аннотации, поддержка Flow, проверка запросов на этапе компиляции                                                                               |
| DI          | **Koin 4** (`koin-android`, `koin-compose`, `koin-compose-viewmodel`) | Простая декларация зависимостей с Kotlin‑DSL, читается как код, можно получать зависимости прямо из Compose‑функций, активное сообщество и хорошая документация |
| Навигация   | **navigation-compose**, type-safe маршруты (`@Serializable`)          | Поддержка Compose, Безопасность типов, гибкая передача аргументов                                                                                               |
| Сеть        | **Retrofit**.                                                         | Поддержка suspend‑функций и Kotlin Flow, гибкая сериализация, легко настраиваются плагины                                                                       |
| Тип проекта | **Монолитный**.                                                       | Достаточен для небольшого проекта                                                                                                                               |

**SDK / тулчейн:** `compileSdk 36` · `minSdk 26` · `targetSdk 36` · Java 17 ·
AGP 8.12.3 · Kotlin 2.2.0 · Gradle 8.13.

> `targetSdk` совмещён с `compileSdk` (36, Android 16) — приложение компилируется и
> таргетируется на последнюю стабильную платформу, что удовлетворяет требованиям
> публикации Google Play и NFR-07. При таргете на Android 16 нужно учитывать
> behavior changes платформы при тестировании.

---

## 2. Архитектурные требования

- Экраны строятся на Compose.
- Слои: **ui** (Compose + MVI) → **domain** (`Repository` интерфейсы + модели)
  → **data** (Room: entity, DAO, реализация репозитория).
- Состояние UI переживает смену конфигурации (поворот экрана) — вся видимость
  диалогов/шторок и введённые данные хранятся в `State` (см. §3).
- Внедрение зависимостей — через Koin.
- Все пользовательские данные (списки, товары, шаблоны имён, порядок сортировки)
  персистентно хранятся в БД.

---

## 3. Строительные блоки MVI

На каждый экран — следующие элементы:

| Блок | Что это | Пример (экран «Мои списки») |
|---|---|---|
| **State** | неизменяемый `data class` — всё, что нужно отрисовать (включая видимость шторок) | `ListsState(lists, isLoading, activeSheet)` |
| **Intent** (Event) | `sealed interface` — действия пользователя | `CreateList`, `RenameList`, `DeleteList`, `OpenAddSheet` |
| **Reducer** | чистая функция `(State, …) -> State` — как меняется состояние | `state.copy(lists = …)` |
| **ViewModel** | принимает Intent, вызывает репозиторий, отдаёт State через `StateFlow` | `ListsViewModel : MviViewModel` |
| **Effect** (SideEffect) | одноразовые события: **навигация** и **снэкбар** | `OpenList(id)`, `ShowSnackbar(text)` |

**Правила MVI:**
- Видимость и содержимое модальных шторок/диалогов — **часть `State`** (например,
  `activeSheet: Sheet?`), а **не** Effect. Это гарантирует восстановление шторки после
  смены конфигурации (NFR-03).
- В `Effect` попадают только по-настоящему одноразовые события, которые нельзя или
  не нужно восстанавливать: переход по навигации и показ снэкбара.
- Reducer не выполняет побочных эффектов: обращения к репозиторию делает ViewModel,
  результат сводится в новое `State` через reducer.

---

## 4. Структура проекта

Раскладка — **package by feature** с внутренними слоями **`data / domain / ui`** и
своим `di` на фичу; общая инфраструктура — в `core/`, точка входа — в `root/`.
(Паттерн заимствован из эталонного проекта Практикума.)

```
app/src/main/java/ru/practicum/shoppinglist/
├── App.kt                          // Application → startKoin(coreModule, onboardingModule, listsModule, listDetailModule)
├── root/ui/                        // RootActivity (host) + ComposeRoot
├── core/                           // общая инфраструктура
│   ├── data/database/              // Room: AppDatabase, Converters
│   ├── di/                         // coreModule (предоставляет AppDatabase)
│   ├── mvi/                        // UiState, UiIntent, UiEffect, MviViewModel
│   └── ui/
│       ├── theme/                  // Material 3: Color, Theme, Type (свет/тьма)
│       ├── navigation/             // Screen (type-safe), NavGraph
│       └── components/             // переиспользуемые composable (EmptyState)
└── feature/
    ├── onboarding/                 // приветственный экран (первый запуск)
    │   ├── data/repository/            // хранение флага первого запуска (DataStore/prefs, не Room)
    │   ├── domain/{api,models}/        // Repository (api) + модель статуса онбординга
    │   ├── ui/                         // OnboardingContract, OnboardingViewModel, OnboardingScreen
    │   └── di/OnboardingModule.kt
    ├── lists/                      // экран «Мои списки»
    │   ├── data/{entity,dao,repository}/
    │   ├── domain/{api,models}/        // Repository (api) + доменные модели
    │   ├── ui/                         // ListsContract, ListsViewModel, ListsScreen, components/
    │   └── di/ListsModule.kt
    └── listdetail/                 // экран редактирования списка (товары)
        ├── data/{entity,dao,repository}/
        ├── domain/{api,models}/
        ├── ui/                         // ListDetailContract, ListDetailViewModel, ListDetailScreen, components/
        └── di/ListDetailModule.kt
```

**Правила раскладки:**
- Каждая фича самодостаточна: свои `data / domain / ui / di`.
- Общая инфраструктура (БД, тема, навигация, базовый MVI, общие composable) — в `core/`;
  `AppDatabase` ссылается на entity из фич.
- Слой `domain`: интерфейс `Repository` в `api/`, доменные модели в `models/`.
  Реализация репозитория — в `data/repository/`.

---

## 5. Внедрение зависимостей (DI)

- DI разбит по модулям: `coreModule` + `onboardingModule` + `listsModule` +
  `listDetailModule`, все подключаются в `App.kt` через `startKoin`.
- `coreModule` предоставляет `AppDatabase` и общую инфраструктуру; модули фич
  предоставляют DAO, репозитории и ViewModel своей фичи.

---

## 6. Навигация (техническая часть)

- Навигация — **type-safe**: `@Serializable sealed interface Screen`, переходы по типам
  (`navigation-compose`).
- Граф навигации (`NavGraph`) — в `core/ui/navigation/`.
- Пользовательская карта переходов между экранами описана в
  [functional-requirements.md](functional-requirements.md) (§ «Карта экранов и навигация»)
  и [design.md](design.md).

---

## 7. Статический анализ — detekt

Подключён и настроен detekt; сборка проходит без нарушений правил.
Конфигурация — `config/detekt/detekt.yml`.

**Ключевые пороги:**
- Тело функции — **менее 50 строк** (`LongMethod`).
- Количество аргументов функции — **менее 6** (`LongParameterList`).
- Тело класса — **менее 350 строк** (`LargeClass`).

> Соответствует NFR-02. Статус NFR-02 (обязательность) — в
> [functional-requirements.md](functional-requirements.md).

---

## 8. Релизная сборка и подпись

Настроена релизная сборка и подпись приложения (NFR-07).

**Критерии:**
- Включены `minifyEnabled` и правила ProGuard/R8 (`app/proguard-rules.pro`).
- Приложение подписано релизным ключом (release keystore), собирается подписанный APK/AAB.
- `targetSdk 36` соответствует требованиям публикации Google Play (см. §1).

Подпись приложения: https://developer.android.com/studio/publish/app-signing
