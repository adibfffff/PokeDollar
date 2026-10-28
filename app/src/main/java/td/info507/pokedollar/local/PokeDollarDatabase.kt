package td.info507.pokedollar.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import td.info507.pokedollar.local.dao.CardDao


// 📒 LE CLASSEUR : la base de données
// entities = les PAGES du classeur (ici une seule : la table "cards")
@Database(entities = [CardEntity::class], version = 1, exportSchema = false)
abstract class PokeDollarDatabase : RoomDatabase() {

    // 🧑 LE DOCUMENTALISTE : seul à savoir lire/écrire dans la page des cartes
    abstract fun cardDao(): CardDao

    companion object {
        // Nom du classeur sur le téléphone
        private const val DB_NAME = "pokedollar_database"

        // Le classeur unique (null tant qu'il n'est pas fabriqué)
        @Volatile
        private var INSTANCE: PokeDollarDatabase? = null

        // Fabrique le classeur s'il n'existe pas, puis rend toujours LE MÊME
        fun get(context: Context): PokeDollarDatabase {
            if (INSTANCE == null) {
                synchronized(this) {   // sécurité : un seul classeur fabriqué à la fois
                    Room.databaseBuilder(context, PokeDollarDatabase::class.java, DB_NAME).build()
                        .also { INSTANCE = it }
                }
            }
            return INSTANCE!!
        }
    }
}

// 🗄️ L'ÉTAGÈRE : là où tout le monde vient chercher le classeur
data object PokeDollarDatabaseHolder {
    // La place sur l'étagère (vide au début)
    var database: PokeDollarDatabase? = null

    // Poser le classeur sur l'étagère
    fun initialize(context: Context) {
        database = PokeDollarDatabase.get(context)
    }
}