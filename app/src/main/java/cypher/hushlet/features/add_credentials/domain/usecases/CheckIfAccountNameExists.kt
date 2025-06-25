package cypher.hushlet.features.add_credentials.domain.usecases

import cypher.hushlet.core.domain.repositories.AccountsRepository
import javax.inject.Inject

class CheckIfAccountNameExists @Inject constructor(
    private val repository: AccountsRepository
) {
    suspend operator fun invoke(accountName: String): Boolean {
        return repository.checkIfAccountNameTaken(accountName)
    }
}