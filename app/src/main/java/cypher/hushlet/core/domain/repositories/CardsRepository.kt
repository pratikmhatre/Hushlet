package cypher.hushlet.core.domain.repositories

import cypher.hushlet.core.domain.models.CardDto
import cypher.hushlet.core.domain.models.CardListItemDto

interface CardsRepository {
    suspend fun addCard(card: CardDto): Long
    suspend fun addMultipleCards(cards: List<CardDto>): List<Long>
    suspend fun updateCard(card: CardDto): Int
    suspend fun deleteCard(card: CardDto) : Int
    suspend fun deleteAllCards()
    suspend fun getFavouriteActiveCards(): List<CardListItemDto>
    suspend fun getAllActiveCards(): List<CardListItemDto>
    suspend fun getArchivedCards(): List<CardListItemDto>
    suspend fun getSingleCard(id: Long): CardDto?
    suspend fun searchActiveCards(query: String): List<CardListItemDto>
    suspend fun searchArchivedCards(query: String): List<CardListItemDto>
    suspend fun getRecentlyAddedCards(count: Int): List<CardListItemDto>
}