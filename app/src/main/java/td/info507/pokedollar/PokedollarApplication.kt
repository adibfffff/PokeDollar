package td.info507.pokedollar

import android.app.Application
import td.info507.pokedollar.local.PokeDollarDatabaseHolder

// Créé une seule fois au lancement de l app
class PokeDollarApplication : Application() {
    override fun onCreate() {
        super.onCreate()// on lance d abord onCreate d origine
        PokeDollarDatabaseHolder.initialize(this)
    }
}