package td.info507.pokedollar.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import td.info507.pokedollar.domain.data.Card
import td.info507.pokedollar.repository.CardRepository

// Récupère la collection (données statiques pour l'instant, Room au TD5)
object GetCollectionUseCase {
    fun invoke(): Flow<List<Card>> {
        return CardRepository.getCollection()
    }
}