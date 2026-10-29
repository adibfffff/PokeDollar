package td.info507.pokedollar.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import td.info507.pokedollar.domain.data.Card
import td.info507.pokedollar.local.PokeDollarDatabaseHolder
import td.info507.pokedollar.local.entity.CardEntity
import td.info507.pokedollar.remote.TcgdexApi


// 💁 LE GUICHET : seul à aller chercher les données (et à les traduire en Card)
object CardRepository {
    // ── API (TCGdex) ──
    // Toute la collection
    fun getCollection(): Flow<List<Card>> {
        return PokeDollarDatabaseHolder.database?.cardDao()?.getAll()
            ?.map { entities -> entities.map { it.toCard() } }
            ?: flowOf(emptyList())
    }

    // Une carte par son id (null si elle n'est pas dans la collection)
    fun getCard(id: String): Flow<Card?> {
        return PokeDollarDatabaseHolder.database?.cardDao()?.getById(id)
            ?.map { entity -> entity?.toCard() }   //
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
    // ── API ──

    // Recherche par nom sur l'API → liste de Card (sans prix ni série)
    suspend fun searchCards(name: String): List<Card> {
        return TcgdexApi.searchCards(name).map { remoteCard -> remoteCard.toCard() }
    }

    // Détail d'une carte sur l'API → Card complète (avec prix et série)
    suspend fun fetchCard(id: String): Card {
        return TcgdexApi.getCard(id).toCard()
    }

    // +1 exemplaire : lit la quantité actuelle en base, puis écrit la nouvelle
    suspend fun addOneCopy(card: Card) {
        val dao = PokeDollarDatabaseHolder.database?.cardDao() ?: return
        val existing = dao.findById(card.id)
        val newQuantity = (existing?.quantity ?: 0) + 1
        dao.upsert(CardEntity.fromCard(card.copy(quantity = newQuantity)))
    }

    // −1 exemplaire : supprime la carte si c'était la dernière
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