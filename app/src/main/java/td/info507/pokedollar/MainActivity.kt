package td.info507.pokedollar

import android.R.attr.color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        Text(text = stringResource(R.string.home_collection),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        Text("50.45€",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary)
        Text(text = stringResource(R.string.home_expensiveCard),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.width(100.dp),
                    painter = painterResource(id = R.drawable.ic_card_placeholder),
                    contentDescription = stringResource(R.string.common_card)

                )
                Text("10€",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.width(100.dp),
                    painter = painterResource(id = R.drawable.ic_card_placeholder),
                    stringResource(R.string.common_card)
                )
                Text("15€",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.width(100.dp),
                    painter = painterResource(id = R.drawable.ic_card_placeholder),
                    stringResource(R.string.common_card)
                )
                Text("9€",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

// Aperçu dans Android Studio (sans émulateur)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PokeDollarTheme {
        HomeScreen()
    }
}