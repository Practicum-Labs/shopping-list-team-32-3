# Релизная подпись приложения

Для публикации приложения в Google Play требуется подписанная релизная сборка. Подпись выполняется с использованием релизного хранилища ключей (Keystore).

Важно: Все секретные данные (keystore, пароли) НЕ ХРАНЯТСЯ в репозитории и НЕ КОММИТЯТСЯ в Git.

**Создать ключ командой:**

>keytool -genkey -v -keystore ~/keystores/release.keystore -alias release -keyalg RSA -keysize 2048 -validity 10000

**или через интерфейс:**
> Build ➔ Generate Signed Bundle / APK / Create new

**Информация о ключе:**

>keytool -list -v -keystore ~/keystores/release.keystore -alias release

**В корне проекта добавить в файл local.properties:**
- release.storeFile=[Путь к файлу keystore]
- release.storePassword=[Пароль хранилища]
- release.keyPassword=[Пароль ключа]
- release.keyAlias=[Алиас ключа]

**Сборка релизной версии:**
- Очистка проекта: "./gradlew clean"
- Сборка APK "./gradlew assembleRelease"
- Сборка AAB (для Google Play) "./gradlew bundleRelease"

**Результаты сборки:**
- APK: app/build/outputs/apk/release/app-release.apk
- AAB: app/build/outputs/bundle/release/app-release.aab



**Проверить, что APK подписан:**
>apksigner verify -v app/build/outputs/apk/release/app-release.apk

**Установить релизную сборку на девайс/симулятор:**
- "./gradlew app:installRelease" (создаст APK и установит)