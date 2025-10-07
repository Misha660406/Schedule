package com.example.schedule.shared.ui.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

data class Typography(
    val h1: TextStyle,
    val h2: TextStyle,
    val h3: TextStyle,
    val h4: TextStyle,
    val bodyMain: TextStyle,
    val bodySecondary: TextStyle,
    val bodyTertiary: TextStyle,
)

data class ColorScheme(
    val textPrimary: Color,
    val textSecondary: Color,
    val accent: Color,
    val error: Color,
    val background: Color,
    val surface: Color,
    val surfaceActive: Color,
    val divider: Color,
    val pressedButton: Color,
    val chipsSelect: Color,
)

internal val LocalColors = staticCompositionLocalOf<ColorScheme> {
    error("No Colors provided")
}

internal val LocalTypography = staticCompositionLocalOf<Typography> {
    error("No Typography provided")
}

object ScheduleTheme {

    val colors: ColorScheme
        @Composable
        get() = LocalColors.current

    val typography: Typography
        @Composable
        get() = LocalTypography.current
}