package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.preferences.FontFamilySetting
import com.example.data.preferences.FontScaleSetting

val CairoFontFamily = FontFamily(
    Font(R.font.cairo, weight = FontWeight.Normal),
    Font(R.font.cairo, weight = FontWeight.Medium),
    Font(R.font.cairo, weight = FontWeight.SemiBold),
    Font(R.font.cairo, weight = FontWeight.Bold)
)

val TajawalFontFamily = FontFamily(
    Font(R.font.tajawal, weight = FontWeight.Normal),
    Font(R.font.tajawal, weight = FontWeight.Medium),
    Font(R.font.tajawal_bold, weight = FontWeight.SemiBold),
    Font(R.font.tajawal_bold, weight = FontWeight.Bold)
)

val AlmaraiFontFamily = FontFamily(
    Font(R.font.almarai, weight = FontWeight.Normal),
    Font(R.font.almarai, weight = FontWeight.Medium),
    Font(R.font.almarai_bold, weight = FontWeight.SemiBold),
    Font(R.font.almarai_bold, weight = FontWeight.Bold)
)

val AmiriFontFamily = FontFamily(
    Font(R.font.amiri, weight = FontWeight.Normal),
    Font(R.font.amiri, weight = FontWeight.Medium),
    Font(R.font.amiri_bold, weight = FontWeight.SemiBold),
    Font(R.font.amiri_bold, weight = FontWeight.Bold)
)

val ReadexProFontFamily = FontFamily(
    Font(R.font.readex_pro, weight = FontWeight.Normal),
    Font(R.font.readex_pro, weight = FontWeight.Medium),
    Font(R.font.readex_pro, weight = FontWeight.SemiBold),
    Font(R.font.readex_pro, weight = FontWeight.Bold)
)

val ArefRuqaaFontFamily = FontFamily(
    Font(R.font.aref_ruqaa, weight = FontWeight.Normal),
    Font(R.font.aref_ruqaa, weight = FontWeight.Medium),
    Font(R.font.aref_ruqaa_bold, weight = FontWeight.SemiBold),
    Font(R.font.aref_ruqaa_bold, weight = FontWeight.Bold)
)

val ReemKufiFontFamily = FontFamily(
    Font(R.font.reem_kufi, weight = FontWeight.Normal),
    Font(R.font.reem_kufi, weight = FontWeight.Medium),
    Font(R.font.reem_kufi, weight = FontWeight.SemiBold),
    Font(R.font.reem_kufi, weight = FontWeight.Bold)
)

val IbmsPlexArabicFontFamily = FontFamily(
    Font(R.font.ibm_plex_arabic, weight = FontWeight.Normal),
    Font(R.font.ibm_plex_arabic, weight = FontWeight.Medium),
    Font(R.font.ibm_plex_arabic_bold, weight = FontWeight.SemiBold),
    Font(R.font.ibm_plex_arabic_bold, weight = FontWeight.Bold)
)

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
