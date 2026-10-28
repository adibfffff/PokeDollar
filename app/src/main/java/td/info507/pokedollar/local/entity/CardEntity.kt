import androidx.room.Entity
import androidx.room.PrimaryKey
import td.info507.pokedollar.domain.data.Card

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey val id: String,
    val name: String,
    val image: String?,
    val price: Double?,
    val setName: String?,
    val quantity: Int
) {
    fun toCard(): Card {
        return Card(
            id = id,
            name = name,
            image = image,
            price = price,
            setName = setName,
            quantity = quantity
        )
    }

    companion object {
        fun fromCard(card: Card): CardEntity {
            return CardEntity(
                id = card.id,
                name = card.name,
                image = card.image,
                price = card.price,
                setName = card.setName,
                quantity = card.quantity
            )
        }
    }
}