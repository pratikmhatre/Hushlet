package cypher.hushlet.data.datasources.local.db.credentials

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cypher.hushlet.data.datasources.local.db.cards.CardsTable
import cypher.hushlet.utils.AppConstants

@Dao
interface CredentialsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCredential(cred: CredentialsTable)

    @Update
    suspend fun updateCredential(cred: CredentialsTable)

    @Delete
    suspend fun deleteCredential(cardsTable: CredentialsTable)

    @Query("DELETE FROM ${AppConstants.CREDENTIALS_TABLE}")
    suspend fun deleteAllCredentials()

    @Query("SELECT * FROM ${AppConstants.CREDENTIALS_TABLE} WHERE isArchived = 0 ORDER BY title ASC")
    suspend fun getAllCredentials(): List<CredentialsTable>

    @Query("SELECT * FROM ${AppConstants.CREDENTIALS_TABLE} WHERE id is :pk")
    suspend fun getSingleCredential(pk: Int): CredentialsTable

    @Query("SELECT * FROM ${AppConstants.CREDENTIALS_TABLE} WHERE (title LIKE '%' || :query || '%' or url LIKE '%' || :query || '%' or notes LIKE '%' || :query || '%' or username LIKE '%' || :query || '%') AND isArchived = 0 ORDER BY title ASC")
    suspend fun searchCards(query: String): List<CredentialsTable>

}