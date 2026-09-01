package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.preferences.FontFamilySetting
import com.example.data.preferences.FontScaleSetting

val googleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

private fun createArabicGoogleFontFamily(name: String, fallback: FontFamily = FontFamily.SansSerif): FontFamily {
    val googleFont = GoogleFont(name)
    return FontFamily(
        Font(googleFont = googleFont, fontProvider = googleFontProvider, weight = FontWeight.Normal),
        Font(googleFont = googleFont, fontProvider = googleFontProvider, weight = FontWeight.Medium),
        Font(googleFont = googleFont, fontProvider = googleFontProvider, weight = FontWeight.SemiBold),
        Font(googleFont = googleFont, fontProvider = googleFontProvider, weight = FontWeight.Bold)
    )
}

val CairoFontFamily = createArabicGoogleFontFamily("Cairo")
val TajawalFontFamily = createArabicGoogleFontFamily("Tajawal")
val AlmaraiFontFamily = createArabicGoogleFontFamily("Almarai")
val AmiriFontFamily = createArabicGoogleFontFamily("Amiri", FontFamily.Serif)
val ReadexProFontFamily = createArabicGoogleFontFamily("Readex Pro")
val ArefRuqaaFontFamily = createArabicGoogleFontFamily("Aref Ruqaa", FontFamily.Cursive)
val ReemKufiFontFamily = createArabicGoogleFontFamily("Reem Kufi")
val IbmsPlexArabicFontFamily = createArabicGoogleFontFamily("IBM Plex Sans Arabic")

fun FontFamilySetting.toComposeFontFamily(): FontFamily = when (this) {
    FontFamilySetting.DEFAULT -> FontFamily.Default
    FontFamilySetting.CAIRO, FontFamilySetting.NASKH -> CairoFontFamily
    FontFamilySetting.TAJAWAL -> TajawalFontFamily
    FontFamilySetting.ALMARAI -> AlmaraiFontFamily
    FontFamilySetting.AMIRI, FontFamilySetting.TRADITIONAL_SERIF -> AmiriFontFamily
    FontFamilySetting.READEX_PRO -> ReadexProFontFamily
    FontFamilySetting.AREF_RUQAA, FontFamilySetting.RUQAH -> ArefRuqaaFontFamily
    FontFamilySetting.REEM_KUFI, FontFamilySetting.KUFIC -> ReemKufiFontFamily
    FontFamilySetting.IBM_PLEX_ARABIC, FontFamilySetting.MONOSPACE -> IbmsPlexArabicFontFamily
}

fun getAppTypography(
    fontScale: FontScaleSetting = FontScaleSetting.MEDIUM,
    fontFamilySetting: FontFamilySetting = FontFamilySetting.DEFAULT
): Typography {
    val scale = fontScale.scaleFactor
    val family = fontFamilySetting.toComposeFontFamily()

    return Typography(
        displayLarge = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Bold,
            fontSize = (32 * scale).sp,
            lineHeight = (40 * scale).sp,
            letterSpacing = 0.sp
        ),
        displayMedium = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Bold,
            fontSize = (28 * scale).sp,
            lineHeight = (36 * scale).sp,
            letterSpacing = 0.sp
        ),
        displaySmall = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.SemiBold,
            fontSize = (24 * scale).sp,
            lineHeight = (32 * scale).sp,
            letterSpacing = 0.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Bold,
            fontSize = (22 * scale).sp,
            lineHeight = (28 * scale).sp,
            letterSpacing = 0.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.SemiBold,
            fontSize = (20 * scale).sp,
            lineHeight = (26 * scale).sp,
            letterSpacing = 0.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.SemiBold,
            fontSize = (18 * scale).sp,
            lineHeight = (24 * scale).sp,
            letterSpacing = 0.sp
        ),
        titleLarge = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Bold,
            fontSize = (18 * scale).sp,
            lineHeight = (24 * scale).sp,
            letterSpacing = 0.sp
        ),
        titleMedium = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.SemiBold,
            fontSize = (16 * scale).sp,
            lineHeight = (22 * scale).sp,
            letterSpacing = 0.sp
        ),
        titleSmall = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Medium,
            fontSize = (14 * scale).sp,
            lineHeight = (20 * scale).sp,
            letterSpacing = 0.1.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Normal,
            fontSize = (15 * scale).sp,
            lineHeight = (22 * scale).sp,
            letterSpacing = 0.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Normal,
            fontSize = (14 * scale).sp,
            lineHeight = (20 * scale).sp,
            letterSpacing = 0.sp
        ),
        bodySmall = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Normal,
            fontSize = (12 * scale).sp,
            lineHeight = (16 * scale).sp,
            letterSpacing = 0.sp
        ),
        labelLarge = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.SemiBold,
            fontSize = (14 * scale).sp,
            lineHeight = (20 * scale).sp,
            letterSpacing = 0.1.sp
        ),
        labelMedium = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Medium,
            fontSize = (12 * scale).sp,
            lineHeight = (16 * scale).sp,
            letterSpacing = 0.1.sp
        ),
        labelSmall = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Medium,
            fontSize = (10 * scale).sp,
            lineHeight = (14 * scale).sp,
            letterSpacing = 0.1.sp
        )
    )
}
