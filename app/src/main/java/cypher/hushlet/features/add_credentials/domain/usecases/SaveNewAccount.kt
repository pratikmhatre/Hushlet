package cypher.hushlet.features.add_credentials.domain.usecases

import cypher.hushlet.core.domain.models.AccountDto
import cypher.hushlet.core.domain.repositories.AccountsRepository
import javax.inject.Inject

class SaveNewAccount @Inject constructor(private val accountsRepository: AccountsRepository) {
    suspend operator fun invoke(accountDto: AccountDto): Long {
        return accountsRepository.addAccount(accountDto)
    }
}