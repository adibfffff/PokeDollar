package td.info507.pokedollar

import android.app.Application
import td.info507.pokedollar.local.DataStoreHolder
import td.info507.pokedollar.local.PokeDollarDatabaseHolder

// Lancée une seule fois, au démarrage de l'app
class PokeDollarApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Création de la base Room
        PokeDollarDatabaseHolder.initialize(this)
        // et du DataStore
        DataStoreHolder.initialize(this)
    }
}