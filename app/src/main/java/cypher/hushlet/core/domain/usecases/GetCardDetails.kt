package cypher.hushlet.core.domain.usecases

import cypher.hushlet.core.domain.models.CardDto
import cypher.hushlet.core.domain.repositories.CardsRepository
import javax.inject.Inject

class GetCardDetails @Inject constructor(private val repository: CardsRepository) {
    suspend operator fun invoke(id: Long): CardDto? = repository.getSingleCard(id)
}