package td.info507.pokedollar.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import td.info507.pokedollar.local.dao.CardDao
import td.info507.pokedollar.local.entity.CardEntity

// La base de données : liste des tables + version
@Database(
    entities = [CardEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PokeDollarDatabase : RoomDatabase() {

    // Donne accès au DAO
    abstract fun cardDao(): CardDao

    companion object {
        private const val DB_NAME = "pokedollar.db"

        @Volatile
        private var INSTANCE: PokeDollarDatabase? = null

        // Crée la base une seule fois, puis renvoie toujours la même
        fun get(context: Context): PokeDollarDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    Room.databaseBuilder(context, PokeDollarDatabase::class.java, DB_NAME).build()
                        .also { INSTANCE = it }
                }
            }
            return INSTANCE!!
        }
    }
}

// Garde la base accessible partout dans l'app
data object PokeDollarDatabaseHolder {
    var database: PokeDollarDatabase? = null

    fun initialize(context: Context) {
        database = PokeDollarDatabase.get(context)
    }
}