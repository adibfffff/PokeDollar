package td.info507.pokedollar.remote

import kotlinx.serialization.Serializable
import td.info507.pokedollar.domain.data.Card



@Serializable
data class RemoteCard(
    val id: String,
    val name: String,
    val image: String? = null,
    val set: RemoteSet? = null,
    val pricing: RemotePricing? = null
) {
    // Traduit la carte de l'API en carte de l'app
    fun toCard(): Card {
        return Card(
            id = id,
            nom = name,
            image = if (image != null) "$image/high.webp" else null,
            prix = pricing?.cardmarket?.avg,
            serie = set?.name
        )
    }
}

@Serializable
data class RemoteSet(
    val name: String
)

@Serializable
data class RemotePricing(
    val cardmarket: RemoteCardmarket? = null
)

@Serializable
data class RemoteCardmarket(
    val avg: Double? = null             // prix moyen en €
)