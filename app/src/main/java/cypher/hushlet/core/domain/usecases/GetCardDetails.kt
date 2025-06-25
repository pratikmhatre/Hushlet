package cypher.hushlet.core.domain.usecases

import cypher.hushlet.core.domain.models.CardDto
import cypher.hushlet.core.domain.repositories.CardsRepository

class GetCardDetails(private val repository: CardsRepository) {
    suspend fun invoke(id: Long): CardDto? = repository.getSingleCard(id)
}