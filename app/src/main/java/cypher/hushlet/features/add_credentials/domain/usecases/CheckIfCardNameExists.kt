package cypher.hushlet.features.add_credentials.domain.usecases

import cypher.hushlet.core.domain.repositories.CardsRepository
import javax.inject.Inject

class CheckIfCardNameExists @Inject constructor(
    private val repository: CardsRepository
) {
    suspend operator fun invoke(cardName: String): Boolean {
        return repository.checkIfCardNameTaken(cardName)
    }
}