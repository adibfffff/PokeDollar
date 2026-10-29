package td.info507.pokedollar.domain.usecase

import td.info507.pokedollar.domain.data.Card
import td.info507.pokedollar.repository.CardRepository

// Retire la carte de la collection
object RemoveCardUseCase {
    suspend fun invoke(card: Card): Result<Unit> {
        return runCatching { CardRepository.removeCard(card) }
    }
}