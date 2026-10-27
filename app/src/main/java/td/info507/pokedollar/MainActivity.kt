package td.info507.pokedollar

import android.R.id.shareText
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import td.info507.pokedollar.ui.theme.PokeDollarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokeDollarTheme {
                // Fond de l'écran = couleur "background" du thème
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

// Écran d'accueil
@Composable
fun HomeScreen() {
    val isCollectionEmpty = false
    val message = stringResource(R.string.home_share_empty)
    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()) {//permet de ne pas mettre l icone sous la batterie t l heure
        //icon de partage

        IconButton(
            onClick = {
                if(isCollectionEmpty){
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
                else{
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        modifier = Modifier.fillMaxWidth(),
                        painter = painterResource(id = R.drawable.ic_card_placeholder),
                        contentDescription = stringResource(R.string.common_card)

                    )
                    Text(
                        text = stringResource(R.string.common_price, 15.0),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        modifier = Modifier.fillMaxWidth(),
                        painter = painterResource(id = R.drawable.ic_card_placeholder),
                        contentDescription = stringResource(R.string.common_card)
                    )
                    Text(
                        text = stringResource(R.string.common_price, 10.0),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        modifier = Modifier.fillMaxWidth(),
                        painter = painterResource(id = R.drawable.ic_card_placeholder),
                        contentDescription = stringResource(R.string.common_card)
                    )
                    Text(
                        text = stringResource(R.string.common_price, 5.0),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

// Aperçu dans Android Studio (sans émulateur)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PokeDollarTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreen()
        }
    }
}
