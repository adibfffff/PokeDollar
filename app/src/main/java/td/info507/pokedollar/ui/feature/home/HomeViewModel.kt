package td.info507.pokedollar.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import td.info507.pokedollar.domain.data.CollectionSummary
import td.info507.pokedollar.domain.usecase.GetCollectionSummaryUseCase

class HomeViewModel : ViewModel() {

    val summary: StateFlow<CollectionSummary> =
        GetCollectionSummaryUseCase.invoke()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CollectionSummary(total = 0.0, topCards = emptyList(), isEmpty = true)
            )
}
