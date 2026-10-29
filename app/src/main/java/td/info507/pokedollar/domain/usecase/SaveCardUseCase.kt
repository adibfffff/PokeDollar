package td.info507.pokedollar.domain.usecase

import td.info507.pokedollar.domain.data.Card
import td.info507.pokedollar.repository.CardRepository

// Enregistre la carte dans la collection (ajout ou nouvelle quantité)
object SaveCardUseCase {
    suspend fun invoke(card: Card): Result<Unit> {
        return runCatching { CardRepository.saveCard(card) }
    }
}