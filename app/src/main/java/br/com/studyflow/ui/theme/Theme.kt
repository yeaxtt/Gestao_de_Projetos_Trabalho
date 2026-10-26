package br.com.studyflow.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Paleta do protótipo no Figma
val Navy = Color(0xFF1E293B)
val OnNavy = Color(0xFFE2E8F1)
val Background = Color(0xFFFAF8FF)
val CardColor = Color(0xFFF4F3FA)
val Ink = Color(0xFF1B1B21)
val Muted = Color(0xFF45464F)
val OutlineColor = Color(0xFF757780)
val OutlineSoft = Color(0xFFC5C6D0)
val Primary = Color(0xFF3659C9)
val NavBarColor = Color(0xFFEEEDF4)
val Indicator = Color(0xFFDCE1FF)

// Cards de resumo da tela inicial
val PendingColor = Color(0xFFFFF6D5)
val DoneColor = Color(0xFFE3F4E4)
val LateColor = Color(0xFFFDE3E1)

val CardShape = RoundedCornerShape(12.dp)
val ChipShape = RoundedCornerShape(8.dp)
val FieldShape = RoundedCornerShape(4.dp)

private val StudyFlowColors = lightColorScheme(
    primary = Primary, onPrimary = Color.White,
    secondary = Color(0xFF52658F), secondaryContainer = Indicator, onSecondaryContainer = Ink,
    background = Background, onBackground = Ink, surface = Background, onSurface = Ink,
    surfaceVariant = Color(0xFFE2E1EC), onSurfaceVariant = Muted,
    surfaceContainer = NavBarColor, surfaceContainerHigh = Color(0xFFE9E7EF), surfaceContainerHighest = Color(0xFFE3E1E9),
    outline = OutlineColor, outlineVariant = OutlineSoft
)

@Composable
fun StudyFlowTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = StudyFlowColors, content = content)
}
