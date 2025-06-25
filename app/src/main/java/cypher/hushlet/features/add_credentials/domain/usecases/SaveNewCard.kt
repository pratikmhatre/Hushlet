package cypher.hushlet.features.add_credentials.domain.usecases

import cypher.hushlet.core.domain.models.CardDto
import cypher.hushlet.core.domain.repositories.CardsRepository
import javax.inject.Inject

class SaveNewCard @Inject constructor(private val cardsRepository: CardsRepository) {
    suspend operator fun invoke(cardDto: CardDto): Long {
        return cardsRepository.addCard(cardDto)
    }
}