package td.info507.pokedollar.repository

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import td.info507.pokedollar.domain.data.UserPreferences
import td.info507.pokedollar.local.DataStoreHolder

// Seul à lire / écrire les préférences dans DataStore
object UserRepository {
    // Clé du pseudo dans le DataStore
    private val pseudoKey = stringPreferencesKey("pseudo")

    // "" si pas de pseudo
    fun getPreferences(): Flow<UserPreferences> {
        return DataStoreHolder.dataStore?.data
            ?.map { preferences -> UserPreferences(pseudo = preferences[pseudoKey] ?: "") }
            ?: flowOf(UserPreferences(pseudo = ""))
    }

    suspend fun savePseudo(pseudo: String) {
        DataStoreHolder.dataStore?.edit { preferences -> preferences[pseudoKey] = pseudo }
    }
}
