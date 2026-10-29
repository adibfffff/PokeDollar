package td.info507.pokedollar.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// Le fichier de préférences "user_preferences" (clé → valeur), créé une seule fois
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

// Comme PokeDollarDatabaseHolder : là où tout le monde vient chercher le DataStore
data object DataStoreHolder {
    var dataStore: DataStore<Preferences>? = null

    fun initialize(context: Context) {
        dataStore = context.dataStore
    }
}
