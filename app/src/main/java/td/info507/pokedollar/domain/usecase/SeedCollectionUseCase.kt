package td.info507.pokedollar.domain.usecase

import td.info507.pokedollar.domain.data.Card
import td.info507.pokedollar.repository.CardRepository

// TEMPORAIRE : remplit la base avec des cartes de test
object SeedCollectionUseCase {
    suspend fun invoke(): Result<Unit> {
        return runCatching {
            val cards = listOf(
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
            cards.forEach { card -> CardRepository.saveCard(card) }   // enregistre chaque carte
        }
    }
}