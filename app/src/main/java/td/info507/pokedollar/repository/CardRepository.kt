package td.info507.pokedollar.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import td.info507.pokedollar.domain.data.Card
import td.info507.pokedollar.local.PokeDollarDatabaseHolder
import td.info507.pokedollar.local.entity.CardEntity
import td.info507.pokedollar.remote.TcgdexApi


// Seul endroit qui accède à Room et à l'API
object CardRepository {
    // Room
    fun getCollection(): Flow<List<Card>> {
        return PokeDollarDatabaseHolder.database?.cardDao()?.getAll()
            ?.map { entities -> entities.map { it.toCard() } }
            ?: flowOf(emptyList())
    }

    // null si la carte n'est pas dans la collection
    fun getCard(id: String): Flow<Card?> {
        return PokeDollarDatabaseHolder.database?.cardDao()?.getById(id)
            ?.map { entity -> entity?.toCard() }
            ?: flowOf(null)
    }

    // API

    // La recherche ne renvoie ni prix ni série
    suspend fun searchCards(name: String): List<Card> {
        return TcgdexApi.searchCards(name).map { remoteCard -> remoteCard.toCard() }
    }

    suspend fun fetchCard(id: String): Card {
        return TcgdexApi.getCard(id).toCard()
    }

    // Lit la quantité actuelle puis écrit la nouvelle
    suspend fun addOneCopy(card: Card) {
        val dao = PokeDollarDatabaseHolder.database?.cardDao() ?: return
        val existing = dao.findById(card.id)
        val newQuantity = (existing?.quantity ?: 0) + 1
        dao.upsert(CardEntity.fromCard(card.copy(quantity = newQuantity)))
    }

    // Supprime la carte si c'était le dernier exemplaire
    suspend fun removeOneCopy(id: String) {
        val dao = PokeDollarDatabaseHolder.database?.cardDao() ?: return
        val existing = dao.findById(id) ?: return
        if (existing.quantity > 1) {
            dao.upsert(existing.copy(quantity = existing.quantity - 1))
        } else {
            dao.delete(existing)
        }
    }
}