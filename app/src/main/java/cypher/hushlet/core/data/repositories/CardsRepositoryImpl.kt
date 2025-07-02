package cypher.hushlet.core.data.repositories

import cypher.hushlet.core.data.datasources.local.db.cards.CardsDao
import cypher.hushlet.core.domain.models.CardDto
import cypher.hushlet.core.domain.models.CardListItemDto
import cypher.hushlet.core.domain.repositories.CardsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CardsRepositoryImpl @Inject constructor(
    private val cardsDao: CardsDao,
) : CardsRepository {
    override suspend fun addCard(card: CardDto): Long = cardsDao.addCard(card.toEntity())

    override suspend fun addMultipleCards(cards: List<CardDto>): List<Long> =
        cardsDao.addMultipleCard(cards.map { it.toEntity() })

    override suspend fun updateCard(card: CardDto) = cardsDao.updateCard(card.toEntity())

    override suspend fun deleteCard(card: CardDto): Int = cardsDao.deleteCard(card.toEntity())

    override suspend fun deleteAllCards() = cardsDao.deleteAllCards()

    override suspend fun getFavouriteActiveCards(): Flow<List<CardListItemDto>> =
        cardsDao.getFavouriteCardsList()

    override suspend fun getAllActiveCards(): List<CardListItemDto> = cardsDao.getActiveCardsList()

    override suspend fun getArchivedCards(): List<CardListItemDto> = cardsDao.getArchivedCardsList()

    override suspend fun getSingleCard(id: Long): CardDto? = cardsDao.getSingleCard(id)?.toDto()

    override suspend fun searchActiveCards(query: String): List<CardListItemDto> =
        cardsDao.searchActiveCards(query)

    override suspend fun searchArchivedCards(query: String): List<CardListItemDto> =
        cardsDao.searchArchivedCards(query)

    override suspend fun getRecentlyAddedCards(count: Int): Flow<List<CardListItemDto>> =
        cardsDao.getRecentlyAddedCards(count)

    override suspend fun checkIfCardNameTaken(cardName: String): Boolean =
        cardsDao.getCardByCardName(cardName) != null
}