package cypher.hushlet.core.domain.repositories

import cypher.hushlet.core.domain.models.AccountDto
import cypher.hushlet.core.domain.models.AccountListItemDto

interface AccountsRepository {
    suspend fun addAccount(account: AccountDto): Long
    suspend fun addMultipleAccounts(accounts: List<AccountDto>): List<Long>
    suspend fun updateAccount(account: AccountDto): Int
    suspend fun deleteAccount(account: AccountDto): Int
    suspend fun deleteAllAccounts()
    suspend fun getFavouriteAccounts(): List<AccountListItemDto>
    suspend fun getAllActiveAccounts(): List<AccountListItemDto>
    suspend fun getAllArchivedAccounts(): List<AccountListItemDto>
    suspend fun getAccountDetails(id: Long): AccountDto?
    suspend fun searchActiveAccounts(query: String): List<AccountListItemDto>
    suspend fun searchArchivedAccounts(query: String): List<AccountListItemDto>
    suspend fun getRecentlyAddedAccounts(count: Int): List<AccountListItemDto>
}