package td.info507.pokedollar.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import td.info507.pokedollar.local.entity.CardEntity

// Les actions autorisées sur la table "cards"
@Dao
interface CardDao {

    // Ajoute la carte, ou la met à jour si l'id existe déjà
    @Upsert
    suspend fun upsert(card: CardEntity)

    // Supprime la carte
    @Delete
    suspend fun delete(card: CardEntity)

    // Récupère toutes les cartes (se met à jour tout seul)
    @Query("SELECT * FROM cards")
    fun getAll(): Flow<List<CardEntity>>
}