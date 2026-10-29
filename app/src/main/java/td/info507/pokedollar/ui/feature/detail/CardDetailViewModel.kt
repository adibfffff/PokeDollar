package td.info507.pokedollar.ui.feature.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import td.info507.pokedollar.domain.data.Card
import td.info507.pokedollar.domain.usecase.GetCardDetailUseCase
import td.info507.pokedollar.domain.usecase.GetCollectionCardUseCase
import td.info507.pokedollar.domain.usecase.AddCopyUseCase
import td.info507.pokedollar.domain.usecase.RemoveCopyUseCase

class CardDetailViewModel : ViewModel() {

    // La carte affichée (API, ou collection si hors-ligne)
    var card by mutableStateOf<Card?>(null)
        private set

    // Nombre d'exemplaires possédés (0 = pas dans la collection)
    var ownedQuantity by mutableStateOf(0)
        private set

    var isLoading by mutableStateOf(true)
        private set

    var hasError by mutableStateOf(false)
        private set

    private var isSaving = false

    // La carte telle qu'elle est dans Room (null si pas possédée)
    private var ownedCard: Card? = null

    // Id déjà chargé (évite de tout relancer après une rotation)
    private var loadedId: String? = null

    // Appelée par l'écran avec l'id reçu par la navigation
    fun load(cardId: String) {
        if (loadedId == cardId) return
        loadedId = cardId

        // ① Room : suit la carte dans la collection (mise à jour automatique)
        viewModelScope.launch {
            GetCollectionCardUseCase.invoke(cardId).collect { owned ->
                ownedCard = owned
                ownedQuantity = owned?.quantity ?: 0
                if (card == null && owned != null) {
                    card = owned          // en attendant l'API (ou si elle échoue)
                }
            }
        }

        // ② API : la carte complète (image HD, série, prix à jour)
        viewModelScope.launch {
            isLoading = true
            GetCardDetailUseCase.invoke(cardId)
                .onSuccess { remote -> card = remote }
                .onFailure { if (card == null) hasError = true }   // erreur seulement si rien à afficher
            isLoading = false
        }
    }

    // Bouton Ajouter : +1 exemplaire
    fun addOne() {
        val current = card ?: return
        if (isSaving) return
        isSaving = true
        viewModelScope.launch {
            AddCopyUseCase.invoke(current)
            isSaving = false
        }
    }

    // Corbeille : −1 exemplaire (supprime la carte si c'était la dernière)
    fun removeOne() {
        val current = card ?: return
        if (isSaving) return
        isSaving = true
        viewModelScope.launch {
            RemoveCopyUseCase.invoke(current.id)
            isSaving = false
        }
    }

}