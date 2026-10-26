package td.info507.pokedollar.domain.data


data class Card(
    val id: String,
    val nom: String,
    val image: String?,
    val prix: Double?,
    val serie: String?
)