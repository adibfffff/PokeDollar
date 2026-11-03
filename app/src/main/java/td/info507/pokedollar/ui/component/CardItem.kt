package td.info507.pokedollar.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import td.info507.pokedollar.R
import td.info507.pokedollar.domain.data.Card

// Une carte dans la grille : image, prix et série
@Composable
fun CardItem(card: Card, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Image de la carte, ou image par défaut si pas d'URL
        // Format d'une carte Pokémon : 63 × 88 mm
        val cardModifier = Modifier
            .fillMaxWidth()
            .aspectRatio(63f / 88f)

        if (card.image != null) {
            SubcomposeAsyncImage(
                model = card.image,
                contentDescription = card.name,
                modifier = cardModifier,
                loading = {
                    // pendant le chargement
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                },
                error = {
                    // si le téléchargement échoue
                    Image(
                        painter = painterResource(R.drawable.ic_card_placeholder),
                        contentDescription = card.name
                    )
                }
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ic_card_placeholder),
                contentDescription = card.name,
                modifier = cardModifier
            )
        }

        if (card.price != null) {
            Text(
                text = stringResource(R.string.common_price, card.price),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (card.setName != null) {
            Text(
                text = card.setName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}