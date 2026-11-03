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

    // Carte de l'API, ou celle de la collection si pas de connexion
    var card by mutableStateOf<Card?>(null)
        private set

    // 0 si la carte n'est pas dans la collection
    var ownedQuantity by mutableStateOf(0)
        private set

    var hasError by mutableStateOf(false)
        private set
    private var isSaving = false

    // Évite de tout recharger après une rotation
    private var loadedId: String? = null

    fun load(cardId: String) {
        if (loadedId == cardId) return
        loadedId = cardId

        // Room : la carte dans ma collection
        viewModelScope.launch {
            GetCollectionCardUseCase.invoke(cardId).collect { owned ->
                ownedQuantity = owned?.quantity ?: 0
                if (card == null && owned != null) {
                    card = owned   // en attendant la réponse de l'API
                }
            }
        }

        // API : la carte complète avec le prix à jour
        viewModelScope.launch {
            GetCardDetailUseCase.invoke(cardId)
                .onSuccess { remote -> card = remote }
                .onFailure { if (card == null) hasError = true }   // erreur seulement s'il n'y a rien à afficher
        }
    }

    fun addOne() {
        val current = card ?: return
        if (isSaving) return
        isSaving = true
        viewModelScope.launch {
            AddCopyUseCase.invoke(current)
            isSaving = false
        }
    }

    // Supprime la carte si c'était le dernier exemplaire
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