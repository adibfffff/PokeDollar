package td.info507.pokedollar.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import td.info507.pokedollar.domain.data.Card

// Même forme que le JSON de TCGdex
@Serializable
data class RemoteCard(
    val id: String,
    val name: String,
    val image: String? = null,
    val set: RemoteSet? = null,
    val pricing: RemotePricing? = null
) {
    fun toCard(): Card = Card(
        id = id,
        name = name,
        image = if (image != null) "$image/high.webp" else null,
        price = pricing?.cardmarket?.avg ?: pricing?.cardmarket?.avgHolo,   // sinon prix holo
        setName = set?.name
    )
}

@Serializable
data class RemoteSet(val name: String)

@Serializable
data class RemotePricing(val cardmarket: RemoteCardmarket? = null)

@Serializable
data class RemoteCardmarket(
    val avg: Double? = null,
    @SerialName("avg-holo") val avgHolo: Double? = null   // certaines cartes n'ont que le prix holo
)