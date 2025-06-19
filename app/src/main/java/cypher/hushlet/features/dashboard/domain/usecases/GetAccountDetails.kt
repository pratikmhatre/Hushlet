package cypher.hushlet.features.dashboard.domain.usecases

import cypher.hushlet.core.domain.models.AccountDto
import cypher.hushlet.core.domain.repositories.AccountsRepository

class GetAccountDetails(private val repository: AccountsRepository) {
    suspend fun invoke(id: Long): AccountDto? = repository.getAccountDetails(id)
}