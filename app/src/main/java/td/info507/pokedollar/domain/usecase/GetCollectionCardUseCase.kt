package td.info507.pokedollar.domain.usecase

import kotlinx.coroutines.flow.Flow
import td.info507.pokedollar.domain.data.Card
import td.info507.pokedollar.repository.CardRepository

// La carte dans ma collection (null si je ne l'ai pas)
object GetCollectionCardUseCase {
    fun invoke(id: String): Flow<Card?> {
        return CardRepository.getCard(id)
    }
}