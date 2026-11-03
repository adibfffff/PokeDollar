package td.info507.pokedollar.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import td.info507.pokedollar.domain.data.CollectionSummary
import td.info507.pokedollar.repository.CardRepository

// Total, top 3 et collection vide pour l'accueil
object GetCollectionSummaryUseCase {
    fun invoke(): Flow<CollectionSummary> {
        return CardRepository.getCollection().map { cards ->
            CollectionSummary(
                total = cards.sumOf { (it.price ?: 0.0) * it.quantity },
                // On ignore les cartes sans prix, puis on garde les 3 plus chères
                topCards = cards.filter { it.price != null }.sortedByDescending { it.price ?: 0.0 }.take(3),
                isEmpty = cards.isEmpty()
            )
        }
    }
}
