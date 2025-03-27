# ShieldFlow SDK 

Для локальной отладки и проверки используется [TMDB-Movie App](https://github.com/sqsong66/TMDB-Movie/tree/main). SDK подключается как внешняя зависимость через `mavenLocal()`.

---

## Требования

- **Android Studio** Arctic Fox или новее
- **JDK 17**
- **Gradle 8.3+**
- Установленный `git`, `Java`, `Android SDK`, и `adb`

---

## Структура

- `TMDB-Movie-main/` — Android-приложение на примере которого показывается интеграция библиотеки
- `shieldflow-sdk/` — SDK, публикуемый локально в `mavenLocal`
- `analyse_shield_serv.py` — сервер приема аналитики

---

## Шаги по сборке

### 1. Склонируйте оба проекта (если они ещё не рядом)

```bash
git clone https://github.com/your-org/shieldflow-sdk.git
git clone https://github.com/your-org/TMDB-Movie-main.git
```

Обе папки должны быть **на одном уровне**, например:

```
/projects/
 ┣━ shieldflow-sdk/
 ┗━ TMDB-Movie-main/
```

---

### 2. Сборка и публикация ShieldFlow SDK

Перейдите в директорию SDK и выполните:

```bash
cd shieldflow-sdk
./gradlew publishToMavenLocal
```

Это опубликует артефакты:

- `shieldflow-android-sdk`
- `shieldflow-core-android-sdk`
- `shieldflow-network-android-sdk`
- `shieldflow-assert-android-sdk`

в ваш локальный Maven-репозиторий.

### 3. Запуск

Соберите и запустите проект:

```bash
cd TMDB-Movie-main
./gradlew assembleDebug
```

Или через Android Studio → Run ▶️

---

## Проверка

- Если сборка прошла успешно, `shieldflow-*` библиотеки будут использоваться внутри проекта без ошибок.

