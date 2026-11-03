package td.info507.pokedollar.domain.usecase

import td.info507.pokedollar.domain.data.Card
import td.info507.pokedollar.repository.CardRepository

// runCatching : une erreur réseau ne fait pas planter l'app
object SearchCardsUseCase {
    suspend fun invoke(name: String): Result<List<Card>> {
        return runCatching {
            CardRepository.searchCards(name).sortedBy { it.image == null }   // sans image à la fin
        }
    }
}