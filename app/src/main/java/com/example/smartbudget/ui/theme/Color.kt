// ui/theme/Color.kt
package com.example.smartbudget.ui.theme

import androidx.compose.ui.graphics.Color

// ── Palette principale ─────────────────────────────────────────
val indigoPrimary   = Color(0xFF2E31E7)
val indigoLight     = Color(0xFF5B5EFF)
val indigoDark      = Color(0xFF1A1CB8)

val blueAccent      = Color(0xFF4167FF)
val skyBlue         = Color(0xFF3FBCFC)
val skyBlueDark     = Color(0xFF1A9ED4)

// Surfaces & fonds
val surfaceWhite    = Color(0xFFFFFFFF)
val surfaceCard     = Color(0xF2FFFFFF)   // blanc 95% opaque pour cartes sur bg
val surfaceGlass    = Color(0x26FFFFFF)   // blanc 15% — effet glassmorphism

val textPrimary     = Color(0xFF0D0F3D)   // indigo très sombre
val textSecondary   = Color(0xFF4A4C7A)
val textOnDark      = Color(0xFFFFFFFF)
val textOnDarkSub   = Color(0xCCFFFFFF)   // blanc 80%

val divider         = Color(0x1A2E31E7)   // indigo 10%

val errorRed        = Color(0xFFE53935)
val successGreen    = Color(0xFF43A047)
val warningAmber    = Color(0xFFFB8C00)

val black           = Color(0xFF000000)
val white           = Color(0xFFFFFFFF)

// ── Couleurs graphes ───────────────────────────────────────────
object ChartColors {
    val alimentationColor = Color(0xFF2E31E7)   // indigo primary
    val transportColor    = Color(0xFF3FBCFC)   // sky blue
    val logementColor     = Color(0xFF4B4C74)   // indigo muted
    val santeColor        = Color(0xFF5B5EFF)   // indigo light
    val loisirColor       = Color(0xFF1A9ED4)   // sky blue dark
    val etudeColor        = Color(0xFF7C7FFF)   // indigo pastel
    val autreColor        = Color(0xFFA4B3F8)   // lavender

    val asList get() = listOf(
        alimentationColor, transportColor, logementColor,
        santeColor, loisirColor, etudeColor, autreColor
    )
}