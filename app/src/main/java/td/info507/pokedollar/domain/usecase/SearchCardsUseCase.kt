package td.info507.pokedollar.domain.usecase

import td.info507.pokedollar.domain.data.Card
import td.info507.pokedollar.repository.CardRepository

// Recherche des cartes par nom sur l'API (erreurs attrapées, pas de plantage)
object SearchCardsUseCase {
    suspend fun invoke(name: String): Result<List<Card>> {
        return runCatching { CardRepository.searchCards(name) }
    }
}