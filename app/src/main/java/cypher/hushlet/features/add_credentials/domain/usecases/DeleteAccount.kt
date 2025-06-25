package cypher.hushlet.features.add_credentials.domain.usecases

import cypher.hushlet.core.domain.models.AccountDto
import cypher.hushlet.core.domain.repositories.AccountsRepository
import javax.inject.Inject

class DeleteAccount @Inject constructor(
    private val repository: AccountsRepository
) {
    suspend operator fun invoke(accountDto: AccountDto): Int {
        return repository.deleteAccount(accountDto)
    }
}