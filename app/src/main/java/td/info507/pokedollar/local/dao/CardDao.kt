package td.info507.pokedollar.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import td.info507.pokedollar.local.entity.CardEntity


// Opérations sur la table "cards"
@Dao
interface CardDao {

    @Query("SELECT * FROM cards")
    fun getAll(): Flow<List<CardEntity>>

    // null si elle n'est pas dans la collection
    @Query("SELECT * FROM cards WHERE id = :id")
    fun getById(id: String): Flow<CardEntity?>

    // Ajoute la carte, ou la met à jour si l'id existe déjà
    @Upsert
    suspend fun upsert(card: CardEntity)

    @Delete
    suspend fun delete(card: CardEntity)

    // Lecture unique (pas un Flow), pour modifier la carte juste après
    @Query("SELECT * FROM cards WHERE id = :id")
    suspend fun findById(id: String): CardEntity?
}