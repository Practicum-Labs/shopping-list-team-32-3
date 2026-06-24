@file:Suppress("MagicNumber") // Hex color literals are the source of truth for design tokens.

package ru.practicum.shoppinglist.core.ui.theme

import androidx.compose.ui.graphics.Color

// 1. Именование цветов соответствует именованию в фигма
// например
// material-theme/sys/dark/on-surface-variant -> MaterialDarkOnSurfaceVariant
// material-theme/sys/dark-medium-contrast/primary -> MaterialDarkMediumContrastPrimary
// 2. Tint для иконок не заводился - слишком много оттенков, не хватает токенов,
// лучше загрузить иконки отдельно для светлой и для темной темы
// 3. некоторые цвета встречаются в фигма под разными названиями,
// тогда в этом файле цвет заведен и приравнен аналогичному цвету,
// чтобы удобнее было искать

val SchemesLightOutlineVariant = Color(0xFFCAC4D0)
val SchemesDarkOutlineVariant = Color(0xFFCAC4D0)
val SchemesLightBackground = Color(0xFFFFF8F4)
val SchemesDarkBackground = Color(0xFF19120C)
val MaterialLightPrimary = Color(0xFF845416)
val MaterialDarkMediumContrastPrimary = Color(0xFFFFBE77)

val MaterialLightOnPrimary = Color(0xFFFFFFFF)
val MaterialDarkMediumContrastOnPrimary = Color(0xFF241200)

val MaterialLightOnSurface = Color(0xFF211A14)
val MaterialDarkOnSurface = Color(0xFFEEE0D5)

val MaterialDarkMediumContrastOnSurface = Color(0xFFFFFAF8)

val MaterialLightOnSurface12 = Color(0x1F211A14)
val MaterialDarkMediumContrastOnSurface12 = Color(0x1FFFFAF8)
val MaterialLightOnSurfaceVariant = Color(0xFF50453A)
val MaterialDarkOnSurfaceVariant = Color(0xFFD4C4B5)
val MaterialDarkMediumContrastOnSurfaceVariant = Color(0xFFD9C8B9)

val MaterialLightPrimaryContainer = Color(0xFFFFDCBB)
val MaterialDarkMediumContrastPrimaryContainer = Color(0xFFBE8543)

val MaterialLightOnPrimaryContainer = Color(0xFF2B1700)
val MaterialDarkMediumContrastOnPrimaryContainer = Color(0xFF000000)

val MaterialDarkSecondary = Color(0xFFE0C1A3)
val MaterialDarkMediumContrastSecondary = Color(0xFFE5C5A7)
val MaterialLightOnSecondaryContainer = Color(0xFF281805)
val MaterialDarkMediumContrastOnSecondaryContainer = Color(0xFF000000)

val MaterialLightSecondaryContainer = Color(0xFFFEDDBD)
val MaterialDarkMediumContrastSecondaryContainer = Color(0xFFA78C70)

val MaterialLightSurface = SchemesLightBackground
val MaterialDarkMediumContrastSurface = SchemesDarkBackground
val MaterialDarkSurface = MaterialDarkMediumContrastSurface

val MaterialLightSurfaceContainer = Color(0xFFFAEBE0)
val MaterialDarkMediumContrastSurfaceContainer = Color(0xFF251E17)
val MaterialLightSurfaceContainerLowest = Color(0xFFFFFFFF)
val MaterialDarkSurfaceContainerLowest = Color(0xFF130D07)
val MaterialLightSurfaceContainerLow = Color(0xFFFFF1E7)
val MaterialDarkMediumContrastSurfaceContainerLow = Color(0xFF211A14)
val MaterialDarkSurfaceContainerHighest = Color(0xFF3B332C)
val MaterialLightSurfaceContainerHigh = Color(0xFFF4E6DA)
val MaterialDarkSurfaceContainerHigh = Color(0xFF302921)
val MaterialDarkMediumContrastSurfaceContainerHigh = MaterialDarkSurfaceContainerHigh

val MaterialLightOutline = Color(0xFF827568)
val MaterialDarkMediumContrastOutline = Color(0xFFB0A092)

val MaterialLightScrim = Color(0x52000000)
val MaterialDarkMediumContrastScrim = Color(0x52000000)

val M3ElevationLight15 = Color(0x26000000)
val M3ElevationLight30 = Color(0x4D000000)

val ColorsGreen = Color(0xFF34C759)
