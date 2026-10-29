package td.info507.pokedollar.domain.usecase

import td.info507.pokedollar.repository.UserRepository

// Enregistre le pseudo
object SavePseudoUseCase {
    suspend fun invoke(pseudo: String): Result<Unit> {
        return runCatching { UserRepository.savePseudo(pseudo) }
    }
}
