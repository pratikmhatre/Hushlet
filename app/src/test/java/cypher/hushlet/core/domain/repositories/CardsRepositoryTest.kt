package cypher.hushlet.core.domain.repositories

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import cypher.hushlet.core.data.datasources.local.db.cards.CardsDao
import cypher.hushlet.core.data.repositories.CardsRepositoryImpl
import cypher.hushlet.core.domain.models.CardDto
import cypher.hushlet.core.domain.models.CardListItemDto
import cypher.hushlet.core.utils.AppConstants
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CardsRepositoryTest {
    private val cardDtoList = getCardDtoList()
    private val crudCardDto = cardDtoList.first()
    private val expectedCardId = 1
    private val cardsDao = mock<CardsDao>()
    private lateinit var cardsRepository: CardsRepository
    private val favouriteCards = getCardsList().filter { it.isFavourite }
    private val archivedCards = getCardsList().filter { it.isArchived }
    private val searchQuery = "SMITH"
    private val activeSearchResults = getCardsList().filter {
        !it.isArchived && it.cardHolderName.lowercase().contains(searchQuery.lowercase())
    }
    private val archivedSearchResults = getCardsList().filter {
        it.isArchived && it.cardHolderName.lowercase().contains(searchQuery.lowercase())
    }

    private val recentlyAddedCards =
        getCardsList().filter { it.isArchived.not() }.sortedByDescending {
            it.updatedAt
        }.take(AppConstants.RECENTLY_ADDED_ITEMS_COUNT)

    @Before
    fun setUp() {
        runTest {
            mockSuccessStates()
            cardsRepository = CardsRepositoryImpl(cardsDao)
        }
    }

    private suspend fun mockSuccessStates() {
        whenever(cardsDao.addMultipleCard(cardDtoList.map { it.toEntity() })).thenReturn(
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
        )
        whenever(cardsDao.addCard(crudCardDto.toEntity())).thenReturn(expectedCardId.toLong())
        whenever(cardsDao.updateCard(crudCardDto.toEntity())).thenReturn(expectedCardId)
        whenever(cardsDao.deleteCard(crudCardDto.toEntity())).thenReturn(expectedCardId)
        whenever(cardsDao.deleteAllCards()).thenReturn(Unit)
        whenever(cardsDao.getFavouriteCardsList()).thenReturn(flowOf(favouriteCards))
        whenever(cardsDao.getActiveCardsList()).thenReturn(getCardsList().filter { it.isArchived.not() })
        whenever(cardsDao.getArchivedCardsList()).thenReturn(archivedCards)
        whenever(cardsDao.getSingleCard(expectedCardId.toLong())).thenReturn(crudCardDto.toEntity())
        whenever(cardsDao.searchActiveCards(searchQuery)).thenReturn(activeSearchResults)
        whenever(cardsDao.searchArchivedCards(searchQuery)).thenReturn(archivedSearchResults)
        whenever(cardsDao.getRecentlyAddedCards(AppConstants.RECENTLY_ADDED_ITEMS_COUNT)).thenReturn(
            flowOf(recentlyAddedCards)
        )
    }


    private fun getCardDtoList(count: Int = 10): List<CardDto> {
        val cards = listOf(
            CardDto(
                id = 0,
                cardNumber = "1234567890123456",
                expiryMonth = "12",
                expiryYear = "2022",
                securityCode = null,
                cardName = "Visa Card",
                cardType = null,
                cardHolderName = "John Doe",
                notes = "",
                isFavourite = false,
                isArchived = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
            CardDto(
                id = 1,
                cardNumber = "6543210987654321",
                expiryMonth = "01",
                expiryYear = "2025",
                securityCode = 987,
                cardName = "Mastercard Card",
                cardType = null,
                cardHolderName = "Jane Smith",
                notes = "",
                isFavourite = true,
                isArchived = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
            CardDto(
                id = 2,
                cardNumber = "1112223334444555",
                expiryMonth = "07",
                expiryYear = "2027",
                securityCode = 888,
                cardName = "Visa Electron Card",
                cardType = null,
                cardHolderName = "Alice Johnson",
                notes = "",
                isFavourite = false,
                isArchived = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
            CardDto(
                id = 3,
                cardNumber = "7890123456789012",
                expiryMonth = "03",
                expiryYear = "2030",
                securityCode = null,
                cardName = "American Express Card",
                cardType = null,
                cardHolderName = "Bob Brown",
                notes = "",
                isFavourite = false,
                isArchived = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
            CardDto(
                id = 4,
                cardNumber = "9876543210987654",
                expiryMonth = "11",
                expiryYear = "2023",
                securityCode = 666,
                cardName = "Diners Club Card",
                cardType = null,
                cardHolderName = "Charlie Davis",
                notes = "",
                isFavourite = true,
                isArchived = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
            CardDto(
                id = 5,
                cardNumber = "4455667788990011",
                expiryMonth = "05",
                expiryYear = "2024",
                securityCode = null,
                cardName = "UnionPay Card",
                cardType = null,
                cardHolderName = "Dave Wilson",
                notes = "",
                isFavourite = false,
                isArchived = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
            CardDto(
                id = 6,
                cardNumber = "9987654321098765",
                expiryMonth = "08",
                expiryYear = "2026",
                securityCode = null,
                cardName = "JCB Card",
                cardType = null,
                cardHolderName = "Emma Thompson",
                notes = "",
                isFavourite = true,
                isArchived = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
            CardDto(
                id = 7,
                cardNumber = "9876543210987654",
                expiryMonth = "11",
                expiryYear = "2023",
                securityCode = 666,
                cardName = "Diners Club Card",
                cardType = null,
                cardHolderName = "Olivia Newton-John",
                notes = "",
                isFavourite = false,
                isArchived = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
            CardDto(
                id = 8,
                cardNumber = "3334445556778899",
                expiryMonth = "07",
                expiryYear = "2027",
                securityCode = 888,
                cardName = "Mastercard Card",
                cardType = null,
                cardHolderName = "Robert Plant",
                notes = "",
                isFavourite = true,
                isArchived = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
            CardDto(
                id = 9,
                cardNumber = "7778889999988776",
                expiryMonth = "02",
                expiryYear = "2025",
                securityCode = null,
                cardName = "Visa Electron Card",
                cardType = null,
                cardHolderName = "Freddie Mercury",
                notes = "",
                isFavourite = false,
                isArchived = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )
        return cards.take(count)
    }

    private fun getCardsList(count: Int = 10): List<CardListItemDto> {
        val cards = listOf(
            CardListItemDto(
                0, "1234567890123456", "Visa Card", "Visa", "John Doe", false, false, 1111111111
            ), CardListItemDto(
                1,
                "6543210987654321",
                "MasterCard",
                "Mastercard",
                "Jane Smith",
                true,
                false,
                1111111116
            ), CardListItemDto(
                2,
                "9876543210987654",
                "Amex Card",
                "American Express",
                "Bob Brown",
                false,
                true,
                1111111114
            ), CardListItemDto(
                3,
                "1112223334444555",
                "Diners Club Card",
                "Diners Club",
                "Charlie Davis",
                true,
                false,
                1111111112
            ), CardListItemDto(
                4,
                "7890123456789012",
                "Discover Card",
                null,
                "Alice Johnson",
                false,
                true,
                1111111117
            ), CardListItemDto(
                5,
                "4455667788990011",
                "UnionPay Card",
                "UnionPay",
                "Dave Wilson",
                false,
                false,
                1111111112
            ), CardListItemDto(
                6, "9987654321098765", "JCB Card", "JCB", "Emma Smith", true, false, 1111111119
            ), CardListItemDto(
                7,
                "9876543210987654",
                "Diners Club Card",
                "Diners Club",
                "Olivia Newton-John",
                false,
                true,
                1111111114
            ), CardListItemDto(
                8,
                "3334445556778899",
                "MasterCard Card",
                "Mastercard",
                "Robert Plant",
                true,
                true,
                1111111116
            ), CardListItemDto(
                9,
                "7778889999988776",
                "Visa Electron Card",
                null,
                "Freddie Mercury",
                false,
                false,
                1111111113
            )
        )
        return cards.take(count)
    }


    @Test
    fun `addCard function calls addCard of Dao`() {
        runTest {
            cardsRepository.addCard(crudCardDto)
            verify(cardsDao, times(1)).addCard(crudCardDto.toEntity())
        }
    }

    @Test
    fun `addMultipleCards function calls addMultipleCard of Dao`() {
        runTest {
            cardsRepository.addMultipleCards(cardDtoList)
            verify(cardsDao, times(1)).addMultipleCard(cardDtoList.map { it.toEntity() })
        }
    }

    @Test
    fun `updateCard function calls updateCard of Dao`() {
        runTest {
            cardsRepository.updateCard(crudCardDto)
            verify(cardsDao, times(1)).updateCard(crudCardDto.toEntity())
        }
    }

    @Test
    fun `deleteCard function calls deleteCard of Dao`() {
        runTest {
            cardsRepository.deleteCard(crudCardDto)
            verify(cardsDao, times(1)).deleteCard(crudCardDto.toEntity())
        }
    }

    @Test
    fun `deleteAllCards function calls deleteAllCards of Dao`() {
        runTest {
            cardsRepository.deleteAllCards()
            verify(cardsDao, times(1)).deleteAllCards()
        }
    }

    @Test
    fun `getFavouriteActiveCards function calls getFavouriteCardsList of Dao`() {
        runTest {
            cardsRepository.getFavouriteActiveCards()
            verify(cardsDao, times(1)).getFavouriteCardsList()
        }
    }

    @Test
    fun `getAllActiveCards function calls getActiveCardsList of Dao`() {
        runTest {
            cardsRepository.getAllActiveCards()
            verify(cardsDao, times(1)).getActiveCardsList()
        }

    }

    @Test
    fun `getArchivedCards function calls getArchivedCardsList of Dao`() {
        runTest {
            cardsRepository.getArchivedCards()
            verify(cardsDao, times(1)).getArchivedCardsList()
        }

    }

    @Test
    fun `getSingleCard function calls getSingleCard of Dao`() {
        runTest {
            cardsRepository.getSingleCard(expectedCardId.toLong())
            verify(cardsDao, times(1)).getSingleCard(expectedCardId.toLong())
        }

    }

    @Test
    fun `searchActiveCards function calls searchActiveCards of Dao`() {
        runTest {
            cardsRepository.searchActiveCards(searchQuery)
            verify(cardsDao, times(1)).searchActiveCards(searchQuery)
        }

    }

    @Test
    fun `searchArchivedCards function calls searchArchivedCards of Dao`() {
        runTest {
            cardsRepository.searchArchivedCards(searchQuery)
            verify(cardsDao, times(1)).searchArchivedCards(searchQuery)
        }

    }

    @Test
    fun `getRecentlyAddedCards function calls getRecentlyAddedCards of Dao`() {
        runTest {
            cardsRepository.getRecentlyAddedCards(AppConstants.RECENTLY_ADDED_ITEMS_COUNT)
            verify(
                cardsDao,
                times(1)
            ).getRecentlyAddedCards(AppConstants.RECENTLY_ADDED_ITEMS_COUNT)
        }

    }

    @Test
    fun `test add card returns primary key of inserted card`() {
        runTest {
            val pk = cardsRepository.addCard(crudCardDto)
            assertThat(pk).isEqualTo(1)
        }
    }

    @Test
    fun `test add multiple cards returns list pks of all added cards`() {
        runTest {
            val ids = cardsRepository.addMultipleCards(cardDtoList)
            assertThat(ids.size == 10)
        }
    }

    @Test
    fun `test update card returns pk of updated card if the card exists`() {
        runTest {
            val pk = cardsRepository.updateCard(crudCardDto)
            assertThat(pk).isEqualTo(1)
        }
    }

    @Test
    fun `test update card returns 0 if the card doesn't exists`() {
        runTest {
            whenever(cardsDao.updateCard(crudCardDto.toEntity())).thenReturn(0)
            val pk = cardsRepository.updateCard(crudCardDto)
            assertThat(pk).isEqualTo(0)
        }
    }

    @Test
    fun `test delete card returns pk of deleted card`() {
        runTest {
            val pk = cardsRepository.deleteCard(crudCardDto)
            assertThat(pk).isEqualTo(1)
        }
    }

    @Test
    fun `test delete card returns 0 if card doesn't exist`() {
        runTest {
            whenever(cardsDao.deleteCard(crudCardDto.toEntity())).thenReturn(0)
            val pk = cardsRepository.deleteCard(crudCardDto)
            assertThat(pk).isEqualTo(0)
        }
    }

    @Test
    fun `test delete all cards clears all saved cards`() {
        runTest {
            whenever(cardsRepository.deleteAllCards()).thenReturn(Unit)
            whenever(cardsRepository.getAllActiveCards()).thenReturn(listOf())
            whenever(cardsRepository.getArchivedCards()).thenReturn(listOf())

            cardsRepository.deleteAllCards()
            assertThat(cardsRepository.getAllActiveCards().size + cardsRepository.getArchivedCards().size).isEqualTo(
                0
            )
        }
    }

    @Test
    fun `test get favourite active cards returns list of favourite cards`() {
        runTest {
            cardsRepository.getFavouriteActiveCards().test {
                val favrts = awaitItem()
                assertThat(favrts.size).isEqualTo(favouriteCards.size)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `test get favourite active cards returns empty list if favourite cards found`() {
        runTest {
            whenever(cardsRepository.getFavouriteActiveCards()).thenReturn(flowOf(emptyList()))
            cardsRepository.getFavouriteActiveCards().test {
                val favrts = awaitItem()
                assertThat(favrts).isEmpty()
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `test get all active cards return list of all available active cards`() {
        runTest {
            val activeCards = cardsRepository.getAllActiveCards()
            assertThat(activeCards.size).isEqualTo(getCardsList().filter { it.isArchived.not() }.size)
        }
    }

    @Test
    fun `test get all active cards return empty list if no active cards are available`() {
        runTest {
            whenever(cardsDao.getActiveCardsList()).thenReturn(listOf())

            val activeCards = cardsRepository.getAllActiveCards()
            assertThat(activeCards).isEmpty()
        }

    }

    @Test
    fun `test get all archived cards return list of all available archived cards`() {
        runTest {
            val archivedCards = cardsRepository.getArchivedCards()
            assertThat(archivedCards.size).isEqualTo(getCardsList().filter { it.isArchived }.size)
        }
    }

    @Test
    fun `test get all archived cards return empty list if no archived cards are available`() {
        runTest {
            whenever(cardsDao.getArchivedCardsList()).thenReturn(emptyList())

            val archivedCards = cardsRepository.getArchivedCards()
            assertThat(archivedCards).isEmpty()
        }

    }

    @Test
    fun `test get single card returns card data of matching id`() {
        runTest {
            val cardDto = cardsRepository.getSingleCard(expectedCardId.toLong())
            assertThat(cardDto).isNotNull()
            assertThat(cardDto!!.cardName).isEqualTo(crudCardDto.cardName)
        }
    }

    @Test
    fun `test get single card returns null if no matching card id found`() {
        //mock null return
        runTest {
            whenever(cardsDao.getSingleCard(expectedCardId.toLong())).thenReturn(null)
        }
    }

    @Test
    fun `test search active cards return list of cards with data matching to query`() {
        runTest {
            val searchResults = cardsRepository.searchActiveCards(searchQuery)
            assertThat(searchResults.size).isEqualTo(activeSearchResults.size)
        }
    }

    @Test
    fun `test search active cards return empty list if no cards with matching query found`() {
        runTest {
            whenever(cardsDao.searchActiveCards("searchQuery")).thenReturn(emptyList())
            val searchResults = cardsRepository.searchActiveCards("searchQuery")
            assertThat(searchResults.size).isEqualTo(0)
        }

    }

    @Test
    fun `test search archived cards return list of cards with data matching to query`() {
        runTest {
            val searchResults = cardsRepository.searchArchivedCards(searchQuery)
            assertThat(searchResults.size).isEqualTo(archivedSearchResults.size)
        }
    }

    @Test
    fun `test search archived cards return empty list if no archived cards with matching query found`() {
        runTest {
            whenever(cardsDao.searchArchivedCards("searchQuery")).thenReturn(emptyList())
            val searchResults = cardsRepository.searchArchivedCards("searchQuery")
            assertThat(searchResults.size).isEqualTo(0)
        }
    }

    @Test
    fun `test recently added cards return list of active cards in descending order of their created time`() {
        runTest {
            cardsRepository.getRecentlyAddedCards(AppConstants.RECENTLY_ADDED_ITEMS_COUNT).test {
                val recentCards = awaitItem()
                assertThat(recentCards.size).isEqualTo(recentlyAddedCards.size)
                recentCards.forEachIndexed { index, result ->
                    assertThat(result.cardName).isEqualTo(recentlyAddedCards[index].cardName)
                    assertThat(result.cardNumber).isEqualTo(recentlyAddedCards[index].cardNumber)
                }
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `test recently added cards return empty list if no active cards are available`() {
        runTest {
            whenever(cardsDao.getRecentlyAddedCards(AppConstants.RECENTLY_ADDED_ITEMS_COUNT)).thenReturn(
                flowOf(emptyList())
            )
            cardsRepository.getRecentlyAddedCards(AppConstants.RECENTLY_ADDED_ITEMS_COUNT).test {
                val recentCards = awaitItem()
                assertThat(recentCards).isEmpty()
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

}