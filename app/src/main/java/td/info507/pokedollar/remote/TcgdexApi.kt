package td.info507.pokedollar.remote

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

// Appels à l'API TCGdex
object TcgdexApi {
    private const val BASE_URL = "https://api.tcgdex.net/v2/fr"

    private val client = HttpClientFactory.create()


    suspend fun searchCards(name: String): List<RemoteCard> = client.get(
        urlString = "$BASE_URL/cards"
    ) {
        parameter("name", name)
    }.body()
}