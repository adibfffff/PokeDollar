package td.info507.pokedollar.remote

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

// requêtes vers l'API
object TcgdexApi {
    private const val BASE_URL = "https://api.tcgdex.net/v2/fr"
    private val client = HttpClientFactory.create()

    // Recherche par nom → tableau JSON → liste de cartes résumées (sans prix ni série)
    suspend fun searchCards(name: String): List<RemoteCard> =
        client.get(urlString = "$BASE_URL/cards") {
            parameter("name", name)
        }.body()

    // Détail d'une carte → un objet JSON → une carte complète (avec prix et série)
    suspend fun getCard(id: String): RemoteCard =
        client.get(urlString = "$BASE_URL/cards/$id").body()
}