package td.info507.pokedollar.domain.usecase

import kotlinx.coroutines.flow.Flow
import td.info507.pokedollar.domain.data.UserPreferences
import td.info507.pokedollar.repository.UserRepository

// Les préférences de l'utilisateur (pseudo), mises à jour automatiquement
object GetUserPreferencesUseCase {
    fun invoke(): Flow<UserPreferences> {
        return UserRepository.getPreferences()
    }
}
