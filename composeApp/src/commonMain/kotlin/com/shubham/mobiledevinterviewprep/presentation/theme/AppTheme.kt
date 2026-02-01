package com.shubham.mobiledevinterviewprep.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * App color palette - Modern interview prep theme.
 * 
 * Architecture Decision: Using Material 3 color scheme for
 * consistent theming across platforms. Custom colors maintain
 * brand identity while following Material Design guidelines.
 */

// Primary colors - Deep blue for professionalism
private val PrimaryLight = Color(0xFF1A56DB)
private val OnPrimaryLight = Color(0xFFFFFFFF)
private val PrimaryContainerLight = Color(0xFFD6E3FF)
private val OnPrimaryContainerLight = Color(0xFF001B3E)

// Secondary colors - Teal for accents
private val SecondaryLight = Color(0xFF006A60)
private val OnSecondaryLight = Color(0xFFFFFFFF)
private val SecondaryContainerLight = Color(0xFF74F8E5)
private val OnSecondaryContainerLight = Color(0xFF00201C)

// Tertiary colors - Purple for emphasis
private val TertiaryLight = Color(0xFF7C5800)
private val OnTertiaryLight = Color(0xFFFFFFFF)
private val TertiaryContainerLight = Color(0xFFFFDEA6)
private val OnTertiaryContainerLight = Color(0xFF271900)

// Background and surface
private val BackgroundLight = Color(0xFFFAFAFC)
private val OnBackgroundLight = Color(0xFF1A1C1E)
private val SurfaceLight = Color(0xFFFFFFFF)
private val OnSurfaceLight = Color(0xFF1A1C1E)
private val SurfaceVariantLight = Color(0xFFE1E2EC)
private val OnSurfaceVariantLight = Color(0xFF44474F)

// Error colors
private val ErrorLight = Color(0xFFBA1A1A)
private val OnErrorLight = Color(0xFFFFFFFF)

// Dark theme colors
private val PrimaryDark = Color(0xFFACC7FF)
private val OnPrimaryDark = Color(0xFF002F64)
private val PrimaryContainerDark = Color(0xFF00458D)
private val OnPrimaryContainerDark = Color(0xFFD6E3FF)

private val SecondaryDark = Color(0xFF53DBC8)
private val OnSecondaryDark = Color(0xFF003731)
private val SecondaryContainerDark = Color(0xFF005048)
private val OnSecondaryContainerDark = Color(0xFF74F8E5)

private val TertiaryDark = Color(0xFFF5BF48)
private val OnTertiaryDark = Color(0xFF412D00)
private val TertiaryContainerDark = Color(0xFF5E4200)
private val OnTertiaryContainerDark = Color(0xFFFFDEA6)

private val BackgroundDark = Color(0xFF1A1C1E)
private val OnBackgroundDark = Color(0xFFE2E2E6)
private val SurfaceDark = Color(0xFF1A1C1E)
private val OnSurfaceDark = Color(0xFFE2E2E6)
private val SurfaceVariantDark = Color(0xFF44474F)
private val OnSurfaceVariantDark = Color(0xFFC4C6D0)

private val ErrorDark = Color(0xFFFFB4AB)
private val OnErrorDark = Color(0xFF690005)

/**
 * Light color scheme for the app.
 */
private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    error = ErrorLight,
    onError = OnErrorLight
)

/**
 * Dark color scheme for the app.
 */
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    error = ErrorDark,
    onError = OnErrorDark
)

/**
 * Custom difficulty colors for visual distinction.
 */
object DifficultyColors {
    val Easy = Color(0xFF22C55E)
    val Medium = Color(0xFFF59E0B)
    val Hard = Color(0xFFEF4444)
    
    val EasyContainer = Color(0xFFDCFCE7)
    val MediumContainer = Color(0xFFFEF3C7)
    val HardContainer = Color(0xFFFEE2E2)
}

/**
 * Main app theme composable.
 */
@Composable
fun InterviewPrepTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
