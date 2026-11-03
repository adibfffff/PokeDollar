package td.info507.pokedollar.domain.usecase

import td.info507.pokedollar.repository.CardRepository

// Retire un exemplaire (supprime la carte si c'était le dernier)
object RemoveCopyUseCase {
    suspend fun invoke(id: String): Result<Unit> {
        return runCatching { CardRepository.removeOneCopy(id) }
    }
}