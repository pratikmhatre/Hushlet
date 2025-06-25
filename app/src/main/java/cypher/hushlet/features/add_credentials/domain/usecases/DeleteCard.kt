package cypher.hushlet.features.add_credentials.domain.usecases

import cypher.hushlet.core.domain.models.CardDto
import cypher.hushlet.core.domain.repositories.CardsRepository
import javax.inject.Inject

class DeleteCard @Inject constructor(
    private val repository: CardsRepository
) {
    suspend operator fun invoke(cardDto: CardDto): Int {
        return repository.deleteCard(cardDto)
    }
}