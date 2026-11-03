package td.info507.pokedollar.domain.data

// Préférences de l'utilisateur (ne dépend ni de DataStore ni de Compose)
data class UserPreferences(
    val pseudo: String    // "" si aucun pseudo enregistré
)
