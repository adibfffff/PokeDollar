package td.info507.pokedollar.domain.data

// Modèle d'une carte (ne dépend ni de Room, ni de l'API, ni de Compose)
data class Card(
    val id: String,
    val name: String,
    val image: String?,
    val price: Double?,
    val setName: String?,
    val quantity: Int = 1    // nombre d'exemplaires possédés
)