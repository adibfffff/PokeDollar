import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import td.info507.pokedollar.ui.feature.home.HomeScreen
import td.info507.pokedollar.ui.feature.detail.CardDetailScreen
import td.info507.pokedollar.ui.feature.collection.CollectionScreen
import td.info507.pokedollar.ui.feature.collection.ProfileScreen
import td.info507.pokedollar.ui.feature.collection.SearchScreen


@Composable
fun NavHost(){
    val backStack = rememberNavBackStack(Destination.Home)

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Destination.Home> {
                HomeScreen()
            }
            entry<Destination.CardDetail> { key ->
                CardDetailScreen(cardId = key.cardId)
            }
            entry<Destination.Collection> {
                CollectionScreen()
            }
            entry<Destination.Profile> {
                ProfileScreen()
            }
            entry<Destination.Search> {
                SearchScreen()
            }
        }
    )
}