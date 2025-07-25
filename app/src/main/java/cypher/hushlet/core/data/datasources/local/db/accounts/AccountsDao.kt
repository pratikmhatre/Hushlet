package cypher.hushlet.core.data.datasources.local.db.accounts

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cypher.hushlet.core.domain.models.AccountListItemDto
import cypher.hushlet.core.utils.AppConstants
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAccount(account: AccountEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMultipleAccounts(accounts: List<AccountEntity>): List<Long>

    @Update
    suspend fun updateAccount(account: AccountEntity): Int

    @Delete
    suspend fun deleteAccount(cardsTable: AccountEntity): Int

    @Query("DELETE FROM ${AppConstants.ACCOUNT_TABLE}")
    suspend fun deleteAllAccounts()

    @Query("SELECT id, accountName, url, isFavourite, updatedAt FROM ${AppConstants.ACCOUNT_TABLE} WHERE isArchived = 0 AND isFavourite = 1 ORDER BY updatedAt DESC")
    fun getFavouriteAccountsList(): Flow<List<AccountListItemDto>>

    @Query("SELECT id, accountName, url, isFavourite, updatedAt FROM ${AppConstants.ACCOUNT_TABLE} WHERE isArchived = 0 ORDER BY accountName ASC")
    suspend fun getActiveAccountsList(): List<AccountListItemDto>

    @Query("SELECT id, accountName, url, isFavourite, updatedAt FROM ${AppConstants.ACCOUNT_TABLE} WHERE isArchived = 1 ORDER BY accountName ASC")
    suspend fun getArchivedAccountsList(): List<AccountListItemDto>

    @Query("SELECT * FROM ${AppConstants.ACCOUNT_TABLE} WHERE id is :pk")
    suspend fun getSingleAccount(pk: Long): AccountEntity?

    @Query("SELECT * FROM ${AppConstants.ACCOUNT_TABLE} WHERE accountName = :accName COLLATE NOCASE LIMIT 1")
    suspend fun getAccountByAccountName(accName: String): AccountEntity?

    @Query(
        """
    SELECT id, accountName, url, isFavourite,updatedAt 
    FROM ${AppConstants.ACCOUNT_TABLE}
    WHERE (
        accountName COLLATE NOCASE LIKE '%' || :query || '%' OR 
        url COLLATE NOCASE LIKE '%' || :query || '%' OR 
        notes COLLATE NOCASE LIKE '%' || :query || '%'
    )
    AND isArchived = 0
    ORDER BY accountName ASC
"""
    )
    suspend fun searchActiveAccounts(query: String): List<AccountListItemDto>

    @Query(
        """
    SELECT id, accountName, url, isFavourite,updatedAt 
    FROM ${AppConstants.ACCOUNT_TABLE}
    WHERE (
        accountName COLLATE NOCASE LIKE '%' || :query || '%' OR 
        url COLLATE NOCASE LIKE '%' || :query || '%' OR 
        notes COLLATE NOCASE LIKE '%' || :query || '%'
    )
    AND isArchived = 1
    ORDER BY accountName ASC
"""
    )
    suspend fun searchArchivedAccounts(query: String): List<AccountListItemDto>

    @Query("SELECT id, accountName, url, isFavourite, updatedAt FROM ${AppConstants.ACCOUNT_TABLE} WHERE isArchived = 0 ORDER BY createdAt DESC LIMIT :count")
    fun getRecentlyAddedAccounts(count: Int): Flow<List<AccountListItemDto>>

}