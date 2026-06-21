# Правила R8/ProGuard для релизной сборки (NFR-07).

# Room: сущности и БД (в core и фичах).
-keep @androidx.room.Entity class * { *; }
-keep class ru.practicum.shoppinglist.core.data.database.** { *; }

# Доменные модели фич (используются в State и навигации).
-keep class ru.practicum.shoppinglist.**.domain.models.** { *; }

# Koin использует рефлексию минимально; перестраховываемся.
-keep class org.koin.** { *; }

# kotlinx.serialization (type-safe навигация).
-keepclassmembers @kotlinx.serialization.Serializable class * { *; }

# Корутины.
-keepclassmembers class kotlinx.coroutines.** { volatile <fields>; }
