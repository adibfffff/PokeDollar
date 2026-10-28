package td.info507.pokedollar.ui.feature.home

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import td.info507.pokedollar.R
import td.info507.pokedollar.ui.component.CardItem
import androidx.compose.runtime.getValue

// Écran d'accueil
@Composable
fun HomeScreen(onCardClicked: (String) -> Unit, viewModel: HomeViewModel = viewModel()) {

    val total by viewModel.total.collectAsStateWithLifecycle()
    val topCards by viewModel.topCards.collectAsStateWithLifecycle()
    val isCollectionEmpty by viewModel.isCollectionEmpty.collectAsStateWithLifecycle()

    val message = stringResource(R.string.home_share_empty)
    val shareText = stringResource(R.string.home_share_text, total)
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {//permet de ne pas mettre l icone sous la batterie t l heure
        //icon de partage

        IconButton(
            onClick = {
                if (isCollectionEmpty) {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                } else {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain" // le type de contenu a partager
                    intent.putExtra(Intent.EXTRA_TEXT, shareText)
                    context.startActivity(intent)
                }
            },
            modifier = Modifier
                .align(Alignment.TopEnd)

        ) {
            Icon(
                painter = painterResource(R.drawable.ic_share),
                contentDescription = stringResource(R.string.home_share),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
        }


        //_---------------------------------------_
        //Column principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = stringResource(R.string.home_collection),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(R.string.common_price, 57.46),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.home_expensiveCard),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 24.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                topCards.forEach { card ->
                    CardItem(
                        card = card,
                        onClick = { onCardClicked(card.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

