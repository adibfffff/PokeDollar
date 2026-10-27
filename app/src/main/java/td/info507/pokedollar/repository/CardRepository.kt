package td.info507.pokedollar.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import td.info507.pokedollar.domain.data.Card
import td.info507.pokedollar.local.PokeDollarDatabaseHolder
import td.info507.pokedollar.local.entity.CardEntity
import td.info507.pokedollar.remote.TcgdexApi

// Sait où sont les cartes : dans Room (collection) ou sur l'API (recherche)
object CardRepository {

    // Ajouter une carte à la collection
    suspend fun addCard(card: Card) {
        PokeDollarDatabaseHolder.database?.cardDao()?.upsert(CardEntity.fromCard(card))
    }

    // Retirer une carte de la collection
    suspend fun removeCard(card: Card) {
        PokeDollarDatabaseHolder.database?.cardDao()?.delete(CardEntity.fromCard(card))
    }

    // Récupérer la collection (se met à jour toute seule)
    fun getCollection(): Flow<List<Card>> {
        return PokeDollarDatabaseHolder.database?.cardDao()?.getAll()
            ?.map { list -> list.map { it.toCard() } }
            ?: flowOf(emptyList())
    }

    // Chercher des cartes sur l'API
    suspend fun searchCards(name: String): List<Card> {
        return TcgdexApi.searchCards(name).map { it.toCard() }
    }
}