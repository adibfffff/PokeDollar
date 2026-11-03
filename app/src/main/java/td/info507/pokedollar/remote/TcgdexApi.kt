package td.info507.pokedollar.remote

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

// Requêtes vers l'API TCGdex
object TcgdexApi {
    private const val BASE_URL = "https://api.tcgdex.net/v2/fr"
    private val client = HttpClientFactory.create()

    // La recherche ne renvoie ni prix ni série
    suspend fun searchCards(name: String): List<RemoteCard> =
        client.get(urlString = "$BASE_URL/cards") {
            parameter("name", name)
        }.body()

    // Le détail renvoie aussi la série et le prix
    suspend fun getCard(id: String): RemoteCard =
        client.get(urlString = "$BASE_URL/cards/$id").body()
}