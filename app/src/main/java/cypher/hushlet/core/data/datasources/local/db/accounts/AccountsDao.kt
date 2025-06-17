package cypher.hushlet.core.data.datasources.local.db.credentials

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cypher.hushlet.core.data.datasources.local.db.models.AccountDto
import cypher.hushlet.core.data.datasources.local.db.models.CardDto
import cypher.hushlet.core.utils.AppConstants

@Dao
interface AccountsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAccount(account: AccountTable): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMultipleAccounts(accounts: List<AccountTable>): List<Long>

    @Update
    suspend fun updateAccount(account: AccountTable): Int

    @Delete
    suspend fun deleteAccount(cardsTable: AccountTable): Int

    @Query("DELETE FROM ${AppConstants.ACCOUNT_TABLE}")
    suspend fun deleteAllAccounts()

    @Query("SELECT id, title, url, isFavourite, updatedAt FROM ${AppConstants.ACCOUNT_TABLE} WHERE isArchived = 0 ORDER BY title ASC")
    suspend fun getActiveAccountsList(): List<AccountDto>

    @Query("SELECT id, title, url, isFavourite, updatedAt FROM ${AppConstants.ACCOUNT_TABLE} WHERE isArchived = 1 ORDER BY title ASC")
    suspend fun getArchivedAccountsList(): List<AccountDto>

    @Query("SELECT * FROM ${AppConstants.ACCOUNT_TABLE} WHERE id is :pk")
    suspend fun getSingleAccount(pk: Int): AccountTable

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
    suspend fun searchActiveAccounts(query: String): List<AccountDto>

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
    suspend fun searchArchivedAccounts(query: String): List<AccountDto>

}