package td.info507.pokedollar.domain.usecase

import td.info507.pokedollar.domain.data.Card
import td.info507.pokedollar.repository.CardRepository

// Détail complet d'une carte sur l'API (prix, série)
object GetCardDetailUseCase {
    suspend fun invoke(id: String): Result<Card> {
        return runCatching { CardRepository.fetchCard(id) }
    }
}