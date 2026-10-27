package td.info507.pokedollar.ui.feature.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource
import td.info507.pokedollar.R

// Écran de la collection (vide pour l'instant)
@Composable
fun CollectionScreen() {
    val cards = List(100) { index -> "Carte $index" }   // temporaire : 100 éléments
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        item {
            Text(
                stringResource(R.string.collection_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        items(items = cards, key = { it }) { element ->
            Text(
                text = element,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

