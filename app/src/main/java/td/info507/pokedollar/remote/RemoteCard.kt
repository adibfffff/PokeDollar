package td.info507.pokedollar.remote

import kotlinx.serialization.Serializable
import td.info507.pokedollar.domain.data.Card

// Colle au JSON d'une carte TCGdex (recherche ET détail)
@Serializable
data class RemoteCard(
    val id: String,
    val name: String,
    val image: String? = null,
    val set: RemoteSet? = null,           // absent dans la recherche
    val pricing: RemotePricing? = null    // absent dans la recherche
) {
    // Conversion vers le modèle de l'app
    fun toCard(): Card = Card(
        id = id,
        name = name,
        image = if (image != null) "$image/high.webp" else null,
        price = pricing?.cardmarket?.avg,
        setName = set?.name
    )
}

@Serializable
data class RemoteSet(val name: String)

@Serializable
data class RemotePricing(val cardmarket: RemoteCardmarket? = null)

@Serializable
data class RemoteCardmarket(val avg: Double? = null)