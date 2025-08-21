package com.example.vardadienas.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.vardadienas.R


// Initialize the google font provider (this didn't work for some reason, so I just downloaded the fonts)
//val provider = GoogleFont.Provider(
//    providerAuthority = "com.google.android.gms.fonts",
//    providerPackage = "com.google.android.gms",
//    certificates = R.array.com_google_android_gms_fonts_certs
//)
//
//// Define needed fonts
//val libertinusSerif = GoogleFont(name = "Libertinus Serif")

// Define the font families for the project
val libertinusSerifFontFamily = FontFamily(
    Font(R.font.libertinus_serif_regular, FontWeight.Normal),
    Font(R.font.libertinus_serif_bold, FontWeight.Bold),
    Font(R.font.libertinus_serif_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.libertinus_serif_bolditalic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.libertinus_serif_semibold, FontWeight.SemiBold),
    Font(R.font.libertinus_serif_semibolditalic, FontWeight.SemiBold, FontStyle.Italic)

)

// Optional: For UI elements like buttons or captions, a sans-serif can be a good contrast.
// Let's use Source Sans 3 as an example.
val uiFontFamily = FontFamily(
    Font(R.font.ubuntu_light, FontWeight.Light),
    Font(R.font.ubuntu_regular, FontWeight.Normal)
)
// Set of Material typography styles to start with
val Typography = Typography(
//    bodyLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
//    )
    headlineLarge = TextStyle(
        fontFamily = libertinusSerifFontFamily,
        fontWeight = FontWeight.Bold, // Bold for high impact
        fontSize = 34.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    // A smaller, but still important headline. For section titles.
    headlineMedium = TextStyle(
        fontFamily = libertinusSerifFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    // Standard titles, like in your app bar or dialogs. Still prominent.
    titleLarge = TextStyle(
        fontFamily = libertinusSerifFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    // The most important style: your main body text for paragraphs.
    bodyLarge = TextStyle(
        fontFamily = libertinusSerifFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp, // Slightly larger than default for a more "book-like" feel
        lineHeight = 26.sp, // Generous line height ( ~1.5x) is crucial for serif readability
        letterSpacing = 0.15.sp // A tiny bit of spacing can help
    ),
    // Secondary body text, slightly smaller.
    bodyMedium = TextStyle(
        fontFamily = libertinusSerifFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.25.sp
    ),
    // Style for Buttons, Tabs, and other functional UI elements.
    // Using a sans-serif here provides a clean, functional contrast.
    labelLarge = TextStyle(
        fontFamily = uiFontFamily, // Using the contrasting sans-serif font
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )

    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)