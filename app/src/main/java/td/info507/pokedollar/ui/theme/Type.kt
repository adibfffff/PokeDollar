package td.info507.pokedollar.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
package td.info507.pokedollar.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import td.info507.pokedollar.R

// Famille Montserrat (3 épaisseurs)
private val MontserratFontFamily: FontFamily = FontFamily(
    Font(resId = R.font.montserrat_black, weight = FontWeight.Black),
    Font(resId = R.font.montserrat_bold, weight = FontWeight.Bold),
    Font(resId = R.font.montserrat_medium, weight = FontWeight.Medium),
)

val Typography = Typography(
    // Valeur totale de la collection (gros chiffre)
    titleLarge = TextStyle(
        fontFamily = MontserratFontFamily,
        fontSize = 40.sp,
        fontWeight = FontWeight.Black
    ),
    // Titre d'un écran
    titleMedium = TextStyle(
        fontFamily = MontserratFontFamily,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold
    ),
    // Nom d'une carte
    bodyLarge = TextStyle(
        fontFamily = MontserratFontFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    ),
    // Infos secondaires (extension, prix…)
    bodyMedium = TextStyle(
        fontFamily = MontserratFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    ),
)


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