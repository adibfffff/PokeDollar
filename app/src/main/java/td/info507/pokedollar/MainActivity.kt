package td.info507.pokedollar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import td.info507.pokedollar.ui.theme.PokeDollarTheme
import td.info507.pokedollar.local.PokeDollarDatabaseHolder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PokeDollarDatabaseHolder.initialize(this)
        enableEdgeToEdge()
        setContent {
            PokeDollarTheme {
                // Surface : remplit l'écran avec la couleur de fond du thème
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TestScreen()
                }
            }
        }
    }
}

//test
@Composable
fun TestScreen() {
    Box(contentAlignment = Alignment.Center) {
        Text(
            text = "PokeDollar",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}