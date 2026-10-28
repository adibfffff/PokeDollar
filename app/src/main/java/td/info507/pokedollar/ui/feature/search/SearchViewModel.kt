package td.info507.pokedollar.ui.feature.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import td.info507.pokedollar.domain.data.Card
import td.info507.pokedollar.domain.usecase.SearchCardsUseCase

class SearchViewModel : ViewModel() {

    // Les cartes trouvées
    var results by mutableStateOf<List<Card>>(emptyList())
        private set

    // Vrai pendant la requête
    var isLoading by mutableStateOf(false)
        private set

    // Vrai si la requête a échoué
    var hasError by mutableStateOf(false)
        private set

    // Lancée par l'écran quand l'utilisateur valide sa recherche
    fun search(name: String) {
        if (name.isBlank()) return

        viewModelScope.launch {
            isLoading = true
            hasError = false

            SearchCardsUseCase.invoke(name)
                .onSuccess { cards -> results = cards }
                .onFailure {
                    hasError = true
                    results = emptyList()
                }

            isLoading = false
        }
    }
}