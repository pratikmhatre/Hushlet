package cypher.hushlet.core.domain.usecases

import cypher.hushlet.core.domain.models.AccountDto
import cypher.hushlet.core.domain.repositories.AccountsRepository
import javax.inject.Inject

class UpdateAccount @Inject constructor(
    private val repository: AccountsRepository
) {
    suspend operator fun invoke(accountDto: AccountDto): Int {
        return repository.updateAccount(accountDto)
    }
}