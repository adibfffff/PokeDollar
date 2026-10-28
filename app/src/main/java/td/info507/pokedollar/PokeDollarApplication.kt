package td.info507.pokedollar

import android.app.Application
import td.info507.pokedollar.local.PokeDollarDatabaseHolder

// 🔑 L'OUVERTURE DU MATIN : créée une seule fois au démarrage, avant tous les écrans
class PokeDollarApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // On pose le classeur sur l'étagère
        PokeDollarDatabaseHolder.initialize(this)
    }
}