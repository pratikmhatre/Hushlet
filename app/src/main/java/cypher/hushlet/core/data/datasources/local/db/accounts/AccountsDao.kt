package cypher.hushlet.core.data.datasources.local.db.accounts

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cypher.hushlet.core.domain.models.AccountListItemDto
import cypher.hushlet.core.utils.AppConstants

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

    @Query("SELECT id, title, url, isFavourite, updatedAt FROM ${AppConstants.ACCOUNT_TABLE} WHERE isArchived = 0 AND isFavourite = 1 ORDER BY updatedAt DESC")
    suspend fun getFavouriteAccountsList(): List<AccountListItemDto>

    @Query("SELECT id, title, url, isFavourite, updatedAt FROM ${AppConstants.ACCOUNT_TABLE} WHERE isArchived = 0 ORDER BY title ASC")
    suspend fun getActiveAccountsList(): List<AccountListItemDto>

    @Query("SELECT id, title, url, isFavourite, updatedAt FROM ${AppConstants.ACCOUNT_TABLE} WHERE isArchived = 1 ORDER BY title ASC")
    suspend fun getArchivedAccountsList(): List<AccountListItemDto>

    @Query("SELECT * FROM ${AppConstants.ACCOUNT_TABLE} WHERE id is :pk")
    suspend fun getSingleAccount(pk: Long): AccountEntity?

    @Query(
        """
    SELECT id, title, url, isFavourite,updatedAt 
    FROM ${AppConstants.ACCOUNT_TABLE}
    WHERE (
        title COLLATE NOCASE LIKE '%' || :query || '%' OR 
        url COLLATE NOCASE LIKE '%' || :query || '%' OR 
        notes COLLATE NOCASE LIKE '%' || :query || '%'
    )
    AND isArchived = 0
    ORDER BY title ASC
"""
    )
    suspend fun searchActiveAccounts(query: String): List<AccountListItemDto>

    @Query(
        """
    SELECT id, title, url, isFavourite,updatedAt 
    FROM ${AppConstants.ACCOUNT_TABLE}
    WHERE (
        title COLLATE NOCASE LIKE '%' || :query || '%' OR 
        url COLLATE NOCASE LIKE '%' || :query || '%' OR 
        notes COLLATE NOCASE LIKE '%' || :query || '%'
    )
    AND isArchived = 1
    ORDER BY title ASC
"""
    )
    suspend fun searchArchivedAccounts(query: String): List<AccountListItemDto>

    @Query("SELECT id, title, url, isFavourite, updatedAt FROM ${AppConstants.ACCOUNT_TABLE} WHERE isArchived = 0 ORDER BY updatedAt DESC LIMIT :count")
    suspend fun getRecentlyAddedAccounts(count: Int): List<AccountListItemDto>

}