package cypher.hushlet.core.data.repositories

import cypher.hushlet.core.data.datasources.local.db.accounts.AccountsDao
import cypher.hushlet.core.domain.models.AccountDto
import cypher.hushlet.core.domain.models.AccountListItemDto
import cypher.hushlet.core.domain.repositories.AccountsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountsRepositoryImpl @Inject constructor(private val accountsDao: AccountsDao) :
    AccountsRepository {
    override suspend fun addAccount(account: AccountDto): Long =
        accountsDao.addAccount(account.toEntity())

    override suspend fun addMultipleAccounts(accounts: List<AccountDto>): List<Long> =
        accountsDao.addMultipleAccounts(accounts.map { it.toEntity() })

    override suspend fun updateAccount(account: AccountDto): Int =
        accountsDao.updateAccount(account.toEntity())

    override suspend fun deleteAccount(account: AccountDto): Int =
        accountsDao.deleteAccount(account.toEntity())

    override suspend fun deleteAllAccounts() = accountsDao.deleteAllAccounts()

    override suspend fun getFavouriteAccounts(): Flow<List<AccountListItemDto>> =
        accountsDao.getFavouriteAccountsList()

    override suspend fun getAllActiveAccounts(): Flow<List<AccountListItemDto>> =
        accountsDao.getActiveAccountsList()

    override suspend fun getAllArchivedAccounts(): List<AccountListItemDto> =
        accountsDao.getArchivedAccountsList()

    override suspend fun searchActiveAccounts(query: String): List<AccountListItemDto> =
        accountsDao.searchActiveAccounts(query)

    override suspend fun searchArchivedAccounts(query: String): List<AccountListItemDto> =
        accountsDao.searchArchivedAccounts(query)

    override suspend fun getRecentlyAddedAccounts(count: Int): Flow<List<AccountListItemDto>> =
        accountsDao.getRecentlyAddedAccounts(count)

    override suspend fun getAccountDetails(id: Long): AccountDto? =
        accountsDao.getSingleAccount(id)?.toDto()

    override suspend fun checkIfAccountNameTaken(accName: String): Boolean =
        accountsDao.getAccountByAccountName(accName) != null
}