package td.info507.pokedollar.ui.feature.detail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

// Écran de détail d'une carte (vide pour l'instant)
@Composable
fun CardDetailScreen(cardId: String, navigateBack: () -> Unit) {
    Text(text = "Détail de $cardId")
}