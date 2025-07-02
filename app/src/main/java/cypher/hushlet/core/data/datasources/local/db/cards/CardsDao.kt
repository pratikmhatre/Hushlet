package cypher.hushlet.core.data.datasources.local.db.cards

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cypher.hushlet.core.domain.models.CardListItemDto
import cypher.hushlet.core.utils.AppConstants
import kotlinx.coroutines.flow.Flow

@Dao
interface CardsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCard(cardEntity: CardEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMultipleCard(cardsList: List<CardEntity>): List<Long>

    @Update
    suspend fun updateCard(cardEntity: CardEntity): Int

    @Delete
    suspend fun deleteCard(cardEntity: CardEntity): Int

    @Query("DELETE FROM ${AppConstants.CARD_TABLE}")
    suspend fun deleteAllCards()

    @Query("SELECT id, cardNumber, cardName, cardType, cardHolderName,isFavourite, isArchived, updatedAt FROM ${AppConstants.CARD_TABLE} WHERE isArchived = 0 AND isFavourite = 1 ORDER BY updatedAt DESC")
    fun getFavouriteCardsList(): Flow<List<CardListItemDto>>

    @Query("SELECT id, cardNumber, cardName, cardType, cardHolderName,isFavourite, isArchived, updatedAt FROM ${AppConstants.CARD_TABLE} WHERE isArchived = 0 ORDER BY cardName ASC")
    suspend fun getActiveCardsList(): List<CardListItemDto>

    @Query("SELECT id, cardNumber, cardName, cardType, cardHolderName,isFavourite,isArchived, updatedAt FROM ${AppConstants.CARD_TABLE} WHERE isArchived = 1 ORDER BY cardName ASC")
    suspend fun getArchivedCardsList(): List<CardListItemDto>

    @Query("SELECT * FROM ${AppConstants.CARD_TABLE} WHERE id is :pk")
    suspend fun getSingleCard(pk: Long): CardEntity?

    @Query("SELECT * FROM ${AppConstants.CARD_TABLE} WHERE cardName = :cardName COLLATE NOCASE LIMIT 1")
    suspend fun getCardByCardName(cardName: String): CardEntity?

    @Query(
        """
    SELECT id, cardNumber, cardName, cardType, cardHolderName, isFavourite,isArchived, updatedAt 
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
    suspend fun searchActiveCards(query: String): List<CardListItemDto>

    @Query(
        """
    SELECT id, cardNumber, cardName, cardType, cardHolderName, isFavourite, isArchived, updatedAt 
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
    suspend fun searchArchivedCards(query: String): List<CardListItemDto>

    @Query("SELECT id, cardNumber, cardName, cardType, cardHolderName,isFavourite, isArchived, updatedAt FROM ${AppConstants.CARD_TABLE} WHERE isArchived = 0 ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentlyAddedCards(limit: Int): Flow<List<CardListItemDto>>

}