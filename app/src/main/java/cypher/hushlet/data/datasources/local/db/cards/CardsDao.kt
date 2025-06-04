package cypher.hushlet.data.datasources.local.db.cards

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cypher.hushlet.utils.AppConstants

@Dao
interface CardsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCard(cardsTable: CardsTable)

    @Update
    suspend fun updateCard(cardsTable: CardsTable)

    @Delete
    suspend fun deleteCard(cardsTable: CardsTable)

    @Query("DELETE FROM ${AppConstants.CARDS_TABLE}")
    suspend fun deleteAllCards()

    @Query("SELECT * FROM ${AppConstants.CARDS_TABLE} WHERE isArchived = 0 ORDER BY cardName ASC")
    suspend fun getAllCards(): List<CardsTable>

    @Query("SELECT * FROM ${AppConstants.CARDS_TABLE} WHERE id is :pk")
    suspend fun getSingleCard(pk:Int): CardsTable

    @Query("SELECT * FROM ${AppConstants.CARDS_TABLE} WHERE (cardName LIKE '%' || :query || '%' or cardHolderName LIKE '%' || :query || '%' or notes LIKE '%' || :query || '%') AND  isArchived = 0 ORDER BY cardName ASC")
    suspend fun searchCards(query: String): List<CardsTable>


}