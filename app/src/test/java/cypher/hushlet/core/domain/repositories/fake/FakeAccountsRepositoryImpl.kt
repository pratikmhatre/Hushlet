package cypher.hushlet.core.domain.repositories.fake

import cypher.hushlet.core.domain.models.AccountDto
import cypher.hushlet.core.domain.models.AccountListItemDto
import cypher.hushlet.core.domain.repositories.AccountsRepository

class FakeAccountsRepositoryImpl : AccountsRepository {
    private val accounts = mutableListOf<AccountDto>()

    private fun assignAccountId(account: AccountDto): AccountDto {
        val newId = (accounts.size + 1).toLong()
        return account.copy(id = newId)
    }

    override suspend fun addAccount(account: AccountDto): Long {
        val accountToAdd = assignAccountId(account)
        accounts.add(accountToAdd)
        return accountToAdd.id
    }

    override suspend fun addMultipleAccounts(accountsToAdd: List<AccountDto>): List<Long> {
        val accountsList = accountsToAdd.map { assignAccountId(it) }
        accounts.addAll(accountsList)
        return accountsList.map { it.id }
    }

    override suspend fun updateAccount(account: AccountDto): Int {
        accounts.removeAll {
            it.id == account.id
        }
        accounts.add(account)
        return account.id.toInt()
    }

    override suspend fun deleteAccount(account: AccountDto): Int {
        accounts.removeAll {
            it.id == account.id
        }
        return account.id.toInt()
    }

    override suspend fun deleteAllAccounts() {
        accounts.clear()
    }

    override suspend fun getFavouriteAccounts(): List<AccountListItemDto> {
        return accounts.filter { it.isFavourite }.map { it.toListItemDto() }
    }

    override suspend fun getAllActiveAccounts(): List<AccountListItemDto> {
        return accounts.filter { it.isArchived.not() }.map { it.toListItemDto() }
    }

    override suspend fun getAllArchivedAccounts(): List<AccountListItemDto> {
        return accounts.filter { it.isArchived }.map { it.toListItemDto() }
    }

    override suspend fun getAccountDetails(id: Long): AccountDto? {
        return accounts.firstOrNull { it.id == id }
    }

    override suspend fun searchActiveAccounts(query: String): List<AccountListItemDto> {
        return accounts.filter {
            it.isArchived.not() && (it.title.contains(query) || it.username?.contains(
                query
            ) == true)
        }.map { it.toListItemDto() }
    }

    override suspend fun searchArchivedAccounts(query: String): List<AccountListItemDto> {
        return accounts.filter {
            it.isArchived && (it.title.contains(query) || it.username?.contains(
                query
            ) == true)
        }.map { it.toListItemDto() }
    }

    override suspend fun getRecentlyAddedAccounts(count: Int): List<AccountListItemDto> {
        return accounts.sortedByDescending { it.updatedAt }.take(count).map { it.toListItemDto() }
    }

    override suspend fun checkIfAccountNameTaken(accName: String): Boolean =
        accounts.any { it.title.equals(accName, ignoreCase = true) }
}