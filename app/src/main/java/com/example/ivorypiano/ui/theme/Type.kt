package com.example.ivorypiano.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ivorypiano.R

// Display text - Cormorant Garamond
val CormorantGaramondFontFamily = FontFamily(
    Font(R.font.cormorantgaramond_light, FontWeight.Light),
    Font(R.font.cormorantgaramond_lightitalic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.cormorantgaramond_regular, FontWeight.Normal),
    Font(R.font.cormorantgaramond_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.cormorantgaramond_medium, FontWeight.Medium),
    Font(R.font.cormorantgaramond_mediumitalic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.cormorantgaramond_semibold, FontWeight.SemiBold),
    Font(R.font.cormorantgaramond_semibolditalic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.cormorantgaramond_bold, FontWeight.Bold),
    Font(R.font.cormorantgaramond_bolditalic, FontWeight.Bold, FontStyle.Italic)
)

// Body/Title text - Lora
val LoraFontFamily = FontFamily(
    Font(R.font.lora_regular, FontWeight.Normal),
    Font(R.font.lora_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.lora_medium, FontWeight.Medium),
    Font(R.font.lora_mediumitalic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.lora_semibold, FontWeight.SemiBold),
    Font(R.font.lora_semibolditalic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.lora_bold, FontWeight.Bold),
    Font(R.font.lora_bolditalic, FontWeight.Bold, FontStyle.Italic)
)

// Default Material 3 typography values as a baseline
private val baseline = Typography()

val AppTypography = Typography(

    displayLarge = baseline.displayLarge.copy(
        fontFamily = CormorantGaramondFontFamily,
        fontWeight = FontWeight.Light,
        letterSpacing = (-1).sp
    ),
    displayMedium = baseline.displayMedium.copy(
        fontFamily = CormorantGaramondFontFamily,
        fontWeight = FontWeight.Normal
    ),
    displaySmall = baseline.displaySmall.copy(
        fontFamily = CormorantGaramondFontFamily,
        fontStyle = FontStyle.Italic,
        letterSpacing = 0.sp
    ),

    headlineLarge = baseline.headlineLarge.copy(
        fontFamily = CormorantGaramondFontFamily,
        fontWeight = FontWeight.SemiBold
    ),
    headlineMedium = baseline.headlineMedium.copy(
        fontFamily = CormorantGaramondFontFamily,
        fontWeight = FontWeight.Medium
    ),
    headlineSmall = baseline.headlineSmall.copy(
        fontFamily = CormorantGaramondFontFamily,
        fontStyle = FontStyle.Italic
    ),

    titleLarge = baseline.titleLarge.copy(
        fontFamily = LoraFontFamily,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.15.sp
    ),
    titleMedium = baseline.titleMedium.copy(
        fontFamily = LoraFontFamily,
        fontWeight = FontWeight.Medium,
        fontStyle = FontStyle.Italic,
        letterSpacing = 0.1.sp
    ),
    titleSmall = baseline.titleSmall.copy(
        fontFamily = LoraFontFamily,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.1.sp
    ),

    bodyLarge = baseline.bodyLarge.copy(
        fontFamily = LoraFontFamily,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = baseline.bodyMedium.copy(
        fontFamily = LoraFontFamily,
        lineHeight = 22.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = baseline.bodySmall.copy(
        fontFamily = LoraFontFamily,
        lineHeight = 18.sp,
        letterSpacing = 0.4.sp
    ),

    labelLarge = baseline.labelLarge.copy(
        fontFamily = LoraFontFamily,
        fontWeight = FontWeight.Medium
    ),
    labelMedium = baseline.labelMedium.copy(
        fontFamily = LoraFontFamily,
        fontWeight = FontWeight.Medium
    ),
    labelSmall = baseline.labelSmall.copy(
        fontFamily = LoraFontFamily,
        fontWeight = FontWeight.Medium
    ),
)
