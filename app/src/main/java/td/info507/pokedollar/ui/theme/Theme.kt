package td.info507.pokedollar.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable


private val ColorScheme = darkColorScheme(
    primary = Gold500,
    onPrimary = Black900,
    secondary = Gold300,
    background = Black900,
    onBackground = White,
    surface = Black800,
    onSurface = White,
    surfaceVariant = Black700,
    onSurfaceVariant = Grey400
)

// Thème appliqué à toute l'app
@Composable
fun PokeDollarTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
    )
}