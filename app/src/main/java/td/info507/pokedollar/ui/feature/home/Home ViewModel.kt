package td.info507.pokedollar.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import td.info507.pokedollar.domain.data.Card
import td.info507.pokedollar.domain.usecase.GetCollectionUseCase

class HomeViewModel : ViewModel() {


    val total: StateFlow<Double> =
        GetCollectionUseCase.invoke()
            .map { cards -> cards.sumOf { (it.price ?: 0.0) * it.quantity } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0.0
            )


    val topCards: StateFlow<List<Card>> =
        GetCollectionUseCase.invoke()
            .map { cards -> cards.sortedByDescending { it.price ?: 0.0 }.take(3) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )


    val isCollectionEmpty: StateFlow<Boolean> =
        GetCollectionUseCase.invoke()
            .map { cards -> cards.isEmpty() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = true
            )
}