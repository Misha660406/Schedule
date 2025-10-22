package com.example.schedule.shared.ui.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.schedule.feature.schedule.R

private val LightColorScheme = ColorScheme(
    textPrimary = Black,
    textSecondary = Secondary,
    accent = Accent,
    error = Red,
    background = White,
    surface = SurfaceLight,
    surfaceActive = SurfaceActive,
    divider = Divider,
    pressedButton = PressedButtonLight,
    chipsSelect = ChipsSelect,
    imageNoSchedule = R.drawable.no_lessons_light,
    imageNoGroup = R.drawable.no_group_light,
    imageWeekend = R.drawable.weekend,
    imageCalendar = R.drawable.calendar_dark,
    imageCalendarClicked = R.drawable.calendar_light,
    imageHome = R.drawable.home_dark,
    imageHomeClicked = R.drawable.home_light,
    imageSettings = R.drawable.settings_dark,
    imageSettingsClicked = R.drawable.settings_light,
)

private val DarkColorScheme = ColorScheme(
    textPrimary = White,
    textSecondary = Secondary,
    accent = Accent,
    error = Red,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceActive = SurfaceActive,
    divider = Divider,
    pressedButton = PressedButtonDark,
    chipsSelect = ChipsSelect,
    imageNoSchedule = R.drawable.no_lessons_dark,
    imageNoGroup = R.drawable.no_group_dark,
    imageWeekend = R.drawable.weekend,
    imageCalendar = R.drawable.calendar_light,
    imageCalendarClicked = R.drawable.calendar_light,
    imageHome = R.drawable.home_light,
    imageHomeClicked = R.drawable.home_light,
    imageSettings = R.drawable.settings_light,
    imageSettingsClicked = R.drawable.settings_light,
)

val typography = Typography(
    h1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 28.sp
    ),
    h2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 24.sp
    ),
    h3 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    h4 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),
    bodyMain = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),
    bodySecondary = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),
    bodyTertiary = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
)

@Composable
fun ScheduleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(
        LocalColors provides colorScheme,
        LocalTypography provides typography,
        content = content
    )
}