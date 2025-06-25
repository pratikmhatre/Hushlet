package cypher.hushlet.core.domain.usecases

import cypher.hushlet.core.domain.models.CardDto
import cypher.hushlet.core.domain.repositories.CardsRepository
import javax.inject.Inject

class UpdateCard @Inject constructor(
    private val repository: CardsRepository
) {
    suspend operator fun invoke(cardDto: CardDto): Int {
        return repository.updateCard(cardDto)
    }
}