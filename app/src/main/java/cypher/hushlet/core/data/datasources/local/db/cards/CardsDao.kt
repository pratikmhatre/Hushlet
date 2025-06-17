package cypher.hushlet.core.data.datasources.local.db.cards

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cypher.hushlet.core.data.datasources.local.db.models.CardDto
import cypher.hushlet.core.utils.AppConstants

@Dao
interface CardsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCard(cardsTable: CardsTable): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMultipleCard(cardsList: List<CardsTable>): List<Long>

    @Update
    suspend fun updateCard(cardsTable: CardsTable): Int

    @Delete
    suspend fun deleteCard(cardsTable: CardsTable): Int

    @Query("DELETE FROM ${AppConstants.CARD_TABLE}")
    suspend fun deleteAllCards()

    @Query("SELECT id, cardNumber, cardName, cardType, cardHolderName,isFavourite, updatedAt FROM ${AppConstants.CARD_TABLE} WHERE isArchived = 0 AND isFavourite = 1 ORDER BY updatedAt DESC")
    suspend fun getFavouriteCardsList(): List<CardDto>

    @Query("SELECT id, cardNumber, cardName, cardType, cardHolderName,isFavourite, updatedAt FROM ${AppConstants.CARD_TABLE} WHERE isArchived = 0 ORDER BY cardName ASC")
    suspend fun getActiveCardsList(): List<CardDto>

    @Query("SELECT id, cardNumber, cardName, cardType, cardHolderName,isFavourite, updatedAt FROM ${AppConstants.CARD_TABLE} WHERE isArchived = 1 ORDER BY cardName ASC")
    suspend fun getArchivedCardsList(): List<CardDto>

    @Query("SELECT * FROM ${AppConstants.CARD_TABLE} WHERE id is :pk")
    suspend fun getSingleCard(pk: Long): CardsTable?

    @Query(
        """
    SELECT id, cardNumber, cardName, cardType, cardHolderName, isFavourite, updatedAt 
    FROM ${AppConstants.CARD_TABLE}
    WHERE (
        cardName COLLATE NOCASE LIKE '%' || :query || '%' OR 
        cardHolderName COLLATE NOCASE LIKE '%' || :query || '%' OR 
        notes COLLATE NOCASE LIKE '%' || :query || '%'
    )
    AND isArchived = 0
    ORDER BY cardName ASC
"""
    )
    suspend fun searchActiveCards(query: String): List<CardDto>

    @Query(
        """
    SELECT id, cardNumber, cardName, cardType, cardHolderName, isFavourite, updatedAt 
    FROM ${AppConstants.CARD_TABLE}
    WHERE (
        cardName COLLATE NOCASE LIKE '%' || :query || '%' OR 
        cardHolderName COLLATE NOCASE LIKE '%' || :query || '%' OR 
        notes COLLATE NOCASE LIKE '%' || :query || '%'
    )
    AND isArchived = 1
    ORDER BY cardName ASC
"""
    )
    suspend fun searchArchivedCards(query: String): List<CardDto>



}