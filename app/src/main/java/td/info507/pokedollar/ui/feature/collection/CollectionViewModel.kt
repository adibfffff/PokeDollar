package td.info507.pokedollar.ui.feature.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import td.info507.pokedollar.domain.data.Card
import td.info507.pokedollar.domain.usecase.GetCollectionUseCase

class CollectionViewModel : ViewModel() {
    val cards : StateFlow<List<Card>> =
        GetCollectionUseCase.invoke()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}