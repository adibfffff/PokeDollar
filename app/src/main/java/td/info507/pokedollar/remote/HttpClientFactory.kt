package td.info507.pokedollar.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

// Fabrique le client HTTP qui parle à TCGdex

object HttpClientFactory {

    // Crée et renvoie un client configuré
    fun create(): HttpClient = HttpClient(OkHttp) { // OkHttp = moteur qui envoie les requêtes sur Android

        // Erreur du serveur -> exception
        expectSuccess = true

        // Convertit le JSON reçu en objets Kotlin
        // ignoreUnknownKeys ignore les champs du JSON qu'on n utilise pas(trop d info depuis l api )
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }

        // Affiche les requêtes dans le Logcat
        install(Logging)

        // Chaque requête indique qu'on échange du JSON
        defaultRequest { contentType(ContentType.Application.Json) }
    }
}