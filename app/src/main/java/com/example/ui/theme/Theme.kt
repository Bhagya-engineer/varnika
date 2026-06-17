package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = DeepForestGreen,         // #C9D8A8 (Muted Sage Green Accent)
    onPrimary = SandBackground,        // #2F261F (Deep Mocha Brown - perfect high-contrast contrast)
    primaryContainer = SoftSageGreen,  // #E7DDC7 (Warm Cream Beige Secondary Accent)
    onPrimaryContainer = SandBackground, // #2F261F
    secondary = SoftSageGreen,         // #E7DDC7
    onSecondary = SandBackground,
    secondaryContainer = WarmBeige,    // #4A3D33 (Soft Cocoa Brown Card BG)
    onSecondaryContainer = WarmDarkCharcoal, // #F5F1E8 (Soft Ivory)
    background = SandBackground,       // #2F261F (Mocha Base)
    onBackground = WarmDarkCharcoal,   // #F5F1E8 (Ivory)
    surface = LightBeigeSurface,       // #3A3028 (Warm Espresso Secondary Background)
    onSurface = WarmDarkCharcoal,      // #F5F1E8 (Ivory)
    surfaceVariant = WarmBeige,        // #4A3D33 (Soft Cocoa Brown Card BG)
    onSurfaceVariant = TaupeStone,     // #C8BFB3 (Muted Warm Gray)
    outline = BorderStone              // rgba(255,255,255,0.08) Translucent white border
  )

private val LightColorScheme =
  darkColorScheme( // Forces Light to be the same gorgeous, warm-luxury dark workspace, matching premium editorial feel
    primary = DeepForestGreen,
    onPrimary = SandBackground,
    primaryContainer = SoftSageGreen,
    onPrimaryContainer = SandBackground,
    secondary = SoftSageGreen,
    onSecondary = SandBackground,
    secondaryContainer = WarmBeige,
    onSecondaryContainer = WarmDarkCharcoal,
    background = SandBackground,
    onBackground = WarmDarkCharcoal,
    surface = LightBeigeSurface,
    onSurface = WarmDarkCharcoal,
    surfaceVariant = WarmBeige,
    onSurfaceVariant = TaupeStone,
    outline = BorderStone
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
