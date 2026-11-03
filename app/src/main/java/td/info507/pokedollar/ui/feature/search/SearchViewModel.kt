package td.info507.pokedollar.ui.feature.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import td.info507.pokedollar.domain.data.Card
import td.info507.pokedollar.domain.usecase.SearchCardsUseCase

class SearchViewModel : ViewModel() {

    // Gardé ici pour retrouver le texte en revenant sur l'onglet
    var query by mutableStateOf("")
        private set

    var results by mutableStateOf<List<Card>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var hasError by mutableStateOf(false)
        private set

    // Pour savoir si on affiche « aucune carte trouvée »
    var hasSearched by mutableStateOf(false)
        private set

    // La recherche en cours, pour pouvoir l'annuler
    private var searchJob: Job? = null

    // Appelée à chaque lettre tapée
    fun search(name: String) {
        query = name
        searchJob?.cancel()   // annule la recherche d'avant

        // Pas de requête en dessous de 2 lettres
        if (name.trim().length < 2) {
            results = emptyList()
            hasSearched = false
            hasError = false
            isLoading = false
            return
        }

        searchJob = viewModelScope.launch {
            delay(300)   // attend que l'utilisateur arrête de taper
            isLoading = true
            hasError = false

            val result = SearchCardsUseCase.invoke(name.trim())
            if (!isActive) return@launch   // une autre recherche a été lancée entre-temps

            result
                .onSuccess { cards ->
                    results = cards
                    hasSearched = true
                }
                .onFailure {
                    hasError = true
                    results = emptyList()
                }

            isLoading = false
        }
    }
}