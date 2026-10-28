package td.info507.pokedollar.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import td.info507.pokedollar.domain.data.Card
import td.info507.pokedollar.local.PokeDollarDatabaseHolder


// 💁 LE GUICHET : seul à aller chercher les données (et à les traduire en Card)
object CardRepository {

    // Toute la collection
    fun getCollection(): Flow<List<Card>> {
        return PokeDollarDatabaseHolder.database?.cardDao()?.getAll()
            ?.map { entities -> entities.map { it.toCard() } }   // chaque liste : Entity → Card
            ?: flowOf(emptyList())                               // base pas prête → liste vide
    }

    // Une carte par son id (null si elle n'est pas dans la collection)
    fun getCard(id: String): Flow<Card?> {
        return PokeDollarDatabaseHolder.database?.cardDao()?.getById(id)
            ?.map { entity -> entity?.toCard() }   // une seule carte, peut-être absente
            ?: flowOf(null)
    }

    // Ajoute la carte, ou met à jour sa quantité si elle existe déjà
    suspend fun saveCard(card: Card) {
        PokeDollarDatabaseHolder.database?.cardDao()?.upsert(CardEntity.fromCard(card))
    }

    // Supprime la carte de la collection
    suspend fun removeCard(card: Card) {
        PokeDollarDatabaseHolder.database?.cardDao()?.delete(CardEntity.fromCard(card))
    }
}