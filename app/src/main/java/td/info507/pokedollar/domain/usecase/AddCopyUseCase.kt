package td.info507.pokedollar.domain.usecase

import td.info507.pokedollar.domain.data.Card
import td.info507.pokedollar.repository.CardRepository

// Ajoute un exemplaire de la carte à la collection
object AddCopyUseCase {
    suspend fun invoke(card: Card): Result<Unit> {
        return runCatching { CardRepository.addOneCopy(card) }
    }
}