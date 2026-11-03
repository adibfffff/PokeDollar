package td.info507.pokedollar.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// Fichier de préférences clé/valeur
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

// Même principe que PokeDollarDatabaseHolder
data object DataStoreHolder {
    var dataStore: DataStore<Preferences>? = null

    fun initialize(context: Context) {
        dataStore = context.dataStore
    }
}
