package td.info507.pokedollar.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import td.info507.pokedollar.domain.data.Card
import td.info507.pokedollar.repository.CardRepository

// Collection triée de la plus chère à la moins chère
object GetCollectionUseCase {
    fun invoke(): Flow<List<Card>> {
        return CardRepository.getCollection()
            .map { cards -> cards.sortedByDescending { it.price ?: 0.0 } }   // sans prix = à la fin
    }
}
