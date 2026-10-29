package td.info507.pokedollar.ui.feature.detail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.SubcomposeAsyncImage
import td.info507.pokedollar.R
import td.info507.pokedollar.ui.component.PrimaryButton

// Détail d'une carte : image, nom, série, prix, ajout / retrait
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailScreen(
    cardId: String,
    navigateBack: () -> Unit,
    viewModel: CardDetailViewModel = viewModel()
) {
    // Charge la carte une fois à l'ouverture de l'écran
    LaunchedEffect(cardId) { viewModel.load(cardId) }

    val context = LocalContext.current
    val addedMessage = stringResource(R.string.detail_added)
    val card = viewModel.card   // copie locale (pour le test != null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Barre du haut : juste la flèche retour
        TopAppBar(
            title = { },
            navigationIcon = {
                IconButton(onClick = navigateBack) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = stringResource(R.string.common_back)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                navigationIconContentColor = MaterialTheme.colorScheme.primary
            )
        ) // fin TopAppBar

        if (card == null) {
            // Pas encore de carte : chargement ou erreur
            Box(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                if (viewModel.hasError) {
                    Text(
                        text = stringResource(R.string.detail_error),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } // fin Box
        } else {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Grande image
                SubcomposeAsyncImage(
                    model = card.image,
                    contentDescription = card.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(63f / 88f),
                    loading = {
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    },
                    error = {
                        Image(
                            painter = painterResource(R.drawable.ic_card_placeholder),
                            contentDescription = card.name
                        )
                    }
                ) // fin SubcomposeAsyncImage

                // 2. Nom + série à gauche, prix + quantité à droite
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = card.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        if (card.setName != null) {
                            Text(
                                text = card.setName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } // fin Column gauche

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = if (card.price != null) {
                                stringResource(R.string.common_price, card.price)
                            } else {
                                stringResource(R.string.detail_no_price)
                            },
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        if (viewModel.ownedQuantity > 0) {
                            Text(
                                text = stringResource(R.string.common_quantity, viewModel.ownedQuantity),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } // fin Column droite
                } // fin Row infos

                // 3. Bouton Ajouter (4f) + corbeille (1f) si possédée
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PrimaryButton(
                        text = stringResource(R.string.detail_add),
                        onClick = {
                            viewModel.addOne()
                            Toast.makeText(context, addedMessage, Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(4f)
                    )
                    if (viewModel.ownedQuantity > 0) {
                        IconButton(
                            onClick = { viewModel.removeOne() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_delete),
                                contentDescription = stringResource(R.string.detail_remove),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                } // fin Row boutons
            } // fin Column contenu
        }
    } // fin Column écran
} // fin CardDetailScreen