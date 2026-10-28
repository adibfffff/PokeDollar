package td.info507.pokedollar.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import td.info507.pokedollar.domain.data.Card

// Récupère la collection (données statiques pour l'instant, Room au TD5)
object GetCollectionUseCase {
    fun invoke(): Flow<List<Card>> {
        return flowOf(
            listOf(
                Card(
                    id = "sv01-081",
                    name = "Miraidon ex",
                    image = null,
                    price = 12.5,
                    setName = "Écarlate et Violet",
                    quantity = 1
                ),
                Card(
                    id = "sv01-125",
                    name = "Koraidon ex",
                    image = null,
                    price = 9.9,
                    setName = "Écarlate et Violet",
                    quantity = 2
                ),
                Card(
                    id = "sv01-086",
                    name = "Gardevoir ex",
                    image = null,
                    price = 18.0,
                    setName = "Écarlate et Violet",
                    quantity = 1
                ),
                Card(
                    id = "sv03.5-151",
                    name = "Mew ex",
                    image = null,
                    price = 35.0,
                    setName = "151",
                    quantity = 1
                ),
                Card(
                    id = "sv04-124",
                    name = "Rugit-Lune ex",
                    image = null,
                    price = 22.0,
                    setName = "Faille Paradoxe",
                    quantity = 3
                )
            )
        )
    }
}