package td.info507.pokedollar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import td.info507.pokedollar.ui.feature.home.HomeScreen
import td.info507.pokedollar.ui.feature.detail.CardDetailScreen
import td.info507.pokedollar.ui.feature.collection.CollectionScreen
import td.info507.pokedollar.ui.feature.profile.ProfileScreen
import td.info507.pokedollar.ui.feature.search.SearchScreen


@Composable
fun NavHost(){
    val backStack = rememberNavBackStack(Destination.Collection)

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Destination.Home> {
                HomeScreen(onCardClicked = {cardId -> backStack.add(Destination.CardDetail(cardId))})
            }
            entry<Destination.CardDetail> { key ->
                CardDetailScreen(cardId = key.cardId,
                    navigateBack = {backStack.removeLastOrNull()})
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