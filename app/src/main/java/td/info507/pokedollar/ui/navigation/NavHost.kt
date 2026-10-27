package td.info507.pokedollar.ui.navigation

import Destination
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import td.info507.pokedollar.R
import td.info507.pokedollar.ui.feature.collection.CollectionScreen
import td.info507.pokedollar.ui.feature.detail.CardDetailScreen
import td.info507.pokedollar.ui.feature.home.HomeScreen
import td.info507.pokedollar.ui.feature.profile.ProfileScreen
import td.info507.pokedollar.ui.feature.search.SearchScreen


@Composable
fun NavHost(){
    val backStack = rememberNavBackStack(Destination.Home)
    val currentDestination = backStack.lastOrNull()

    val itemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        indicatorColor = MaterialTheme.colorScheme.surfaceVariant,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant

    )
    Scaffold (
        bottomBar = {NavigationBar(containerColor = MaterialTheme.colorScheme.surface){
            NavigationBarItem(
                selected = currentDestination == Destination.Home,
                onClick = {
                    backStack.clear()
                    backStack.add(Destination.Home)
                },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_home),
                        contentDescription = stringResource(R.string.nav_home)
                    )
                },
                colors = itemColors
            )

            NavigationBarItem(
                selected = currentDestination == Destination.Search,
                onClick = {
                    backStack.clear()
                    backStack.add(Destination.Search)
                },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = stringResource(R.string.nav_search)
                    )
                },
                colors = itemColors)
            NavigationBarItem(
                selected = currentDestination == Destination.Collection,
                onClick = {
                    backStack.clear()
                    backStack.add(Destination.Collection)
                },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_collection),
                        contentDescription = stringResource(R.string.nav_collection)
                    )
                },
                colors = itemColors)

            NavigationBarItem(
                selected = currentDestination == Destination.Profile,
                onClick = {
                    backStack.clear()
                    backStack.add(Destination.Profile)
                },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_profile),
                        contentDescription = stringResource(R.string.nav_profile)
                    )
                },
                colors = itemColors)

        } }
    )
    { innerPadding ->
    NavDisplay(
        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),//on donne un padding egal a la bottom bar
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
    )}
}