package td.info507.pokedollar.domain.data

// Résumé de la collection affiché sur l'accueil
data class CollectionSummary(
    val total: Double,          // valeur totale (prix × quantité)
    val topCards: List<Card>,   // les 3 cartes les plus chères (avec un prix)
    val isEmpty: Boolean        // vrai si aucune carte
)
