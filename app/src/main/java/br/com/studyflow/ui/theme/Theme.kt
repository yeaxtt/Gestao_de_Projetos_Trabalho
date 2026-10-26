package br.com.studyflow.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// O app usa sempre o tema claro do protótipo no Figma (sem cores dinâmicas do Android 12+).
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = Navy,
    onPrimaryContainer = OnNavy,
    secondary = Color(0xFF52658F),
    secondaryContainer = Indicator,
    onSecondaryContainer = Ink,
    background = Background,
    onBackground = Ink,
    surface = Background,
    onSurface = Ink,
    surfaceVariant = Color(0xFFE2E1EC),
    onSurfaceVariant = Muted,
    surfaceContainer = NavBarColor,
    surfaceContainerHigh = Color(0xFFE9E7EF),
    surfaceContainerHighest = Color(0xFFE3E1E9),
    outline = OutlineColor,
    outlineVariant = OutlineSoft
)

@Composable
fun StudyFlowTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
