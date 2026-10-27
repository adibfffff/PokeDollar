package td.info507.pokedollar.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import td.info507.pokedollar.domain.data.Card

// Une ligne de la table "cards" = une carte de ma collection
@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey val id: String,
    val nom: String,
    val image: String?,
    val prix: Double?,
    val serie: String?
) {
    // Lire : ligne de la base -> carte de l'app
    fun toCard(): Card {
        return Card(id = id, nom = nom, image = image, prix = prix, serie = serie)
    }

    companion object {
        // Enregistrer : carte de l'app -> ligne de la base
        fun fromCard(card: Card): CardEntity {
            return CardEntity(id = card.id, nom = card.nom, image = card.image, prix = card.prix, serie = card.serie)
        }
    }
}