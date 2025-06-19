package cypher.hushlet.core.data.datasources.local.db.cards

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import cypher.hushlet.core.data.datasources.local.db.HushletDb
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class CardsDaoTest {
    private lateinit var cardsDao: CardsDao
    private lateinit var hushletDb: HushletDb

    private fun createDatabase(): HushletDb {
        return Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            HushletDb::class.java
        ).allowMainThreadQueries().build()
    }

    @Before
    fun setUp() {
        hushletDb = createDatabase()
        cardsDao = hushletDb.getCardsDao()
    }

    @After
    fun tearDown() {
        hushletDb.close()
    }

    @Test
    fun addCardFunctionReturnsPrimaryKey() {
        runTest {
            val cardData = getActiveCards(1).first()
            val id = cardsDao.addCard(cardData)
            assertThat(id).isNotEqualTo(0L)
        }
    }

    @Test
    fun addCardFunctionIgnoresDuplicateInsertion() {
        runTest {
            val cardData = getActiveCards(1).first()
            val firstInsertion = cardsDao.addCard(cardData)
            val insertedCard = cardsDao.getSingleCard(firstInsertion)!!
            val secondInsertion = cardsDao.addCard(insertedCard)
            assertThat(secondInsertion).isEqualTo(-1L)
        }
    }

    @Test
    fun addCardFunctionAddsNewCardWithProperData() {
        runTest {
            val cardData = getActiveCards(1).first()
            val id = cardsDao.addCard(cardData)

            val insertedCard = cardsDao.getSingleCard(id)

            assertThat(insertedCard).isNotNull()
            assertThat(insertedCard!!.cardNumber).isEqualTo(cardData.cardNumber)
            assertThat(insertedCard.expiryMonth).isEqualTo(cardData.expiryMonth)
            assertThat(insertedCard.expiryYear).isEqualTo(cardData.expiryYear)
            assertThat(insertedCard.securityCode).isEqualTo(cardData.securityCode)
            assertThat(insertedCard.cardName).isEqualTo(cardData.cardName)
            assertThat(insertedCard.cardType).isEqualTo(cardData.cardType)
            assertThat(insertedCard.cardHolderName).isEqualTo(cardData.cardHolderName)
            assertThat(insertedCard.notes).isEqualTo(cardData.notes)
            assertThat(insertedCard.isFavourite).isEqualTo(cardData.isFavourite)
            assertThat(insertedCard.isArchived).isEqualTo(cardData.isArchived)
            assertThat(insertedCard.createdAt).isEqualTo(cardData.createdAt)
            assertThat(insertedCard.updatedAt).isEqualTo(cardData.updatedAt)
        }
    }

    @Test
    fun addMultipleCardFunctionStoresMultipleCardsAndReturnsListOfIds() {
        runTest {
            val activeCards = getActiveCards()
            val ids = cardsDao.addMultipleCard(activeCards)
            assertThat(ids.size).isEqualTo(activeCards.size)
        }
    }

    @Test
    fun updateCardUpdatesDetailsOfGivenCard() {
        runTest {
            val card = getActiveCards(1).first()
            val changedName = "Common Wealth Bank Card"
            val changedExpiryYear = "2045"

            val id = cardsDao.addCard(card)
            val savedCardData = cardsDao.getSingleCard(id)

            assertThat(savedCardData).isNotNull()

            savedCardData!!.cardName = changedName
            savedCardData.expiryYear = changedExpiryYear

            val pk = cardsDao.updateCard(savedCardData)

            val updatedCardData = cardsDao.getSingleCard(pk.toLong())

            assertThat(updatedCardData).isNotNull()
            assertThat(updatedCardData!!.cardName).isEqualTo(changedName)
            assertThat(updatedCardData.expiryYear).isEqualTo(changedExpiryYear)
        }
    }

    @Test
    fun updateCardReturnsZeroIfNoMatchingCardFoundToUpdate() {
        runTest {
            val cardsList = getActiveCards(2)
            val firstCard = cardsList[0]
            val secondCard = cardsList[1]

            cardsDao.addCard(firstCard)

            val id = cardsDao.updateCard(secondCard)

            assertThat(id).isEqualTo(0L)
        }
    }

    @Test
    fun deleteCardRemovesCardDetails() {
        runTest {
            val cardsList = getActiveCards()
            val insertedCards = cardsDao.addMultipleCard(cardsList)

            val cardIdToDelete = insertedCards[2]
            val cardToDelete = cardsDao.getSingleCard(cardIdToDelete)

            assertThat(cardToDelete).isNotNull()

            cardsDao.deleteCard(cardToDelete!!)

            val id = cardsDao.getSingleCard(cardToDelete.id!!)
            assertThat(id).isNull()
        }
    }

    @Test
    fun deleteCardReturnsZeroIfNoMatchingCardFoundToDelete() {
        runTest {
            val cardsList = getActiveCards(2)
            val firstCard = cardsList[0]
            val secondCard = cardsList[1]

            cardsDao.addCard(firstCard)

            val id = cardsDao.deleteCard(secondCard)

            assertThat(id).isEqualTo(0L)
        }
    }

    @Test
    fun deleteAllCardRemovesAllStoredCards() {
        runTest {
            val activeCards = getActiveCards()
            val archivedCards = getArchivedCards()

            cardsDao.addMultipleCard(activeCards)
            cardsDao.addMultipleCard(archivedCards)

            assertThat(cardsDao.getActiveCardsList().size + cardsDao.getArchivedCardsList().size).isEqualTo(
                activeCards.size + archivedCards.size
            )

            cardsDao.deleteAllCards()

            assertThat(cardsDao.getActiveCardsList().size + cardsDao.getArchivedCardsList().size).isEqualTo(
                0
            )

        }
    }

    @Test
    fun getActiveCardsListReturnsAllActiveCardsInAscendingOrderOfCardNames() {
        runTest {
            val cardsList = ArrayList(getActiveCards())
            cardsDao.addMultipleCard(cardsList)

            cardsList.sortBy { it.cardName }

            val savedCards = cardsDao.getActiveCardsList()
            cardsList.forEachIndexed { index, entry ->
                val savedCard = savedCards[index]
                assertThat(savedCard.cardName).isEqualTo(entry.cardName)
                assertThat(savedCard.cardNumber).isEqualTo(entry.cardNumber)
                assertThat(savedCard.cardHolderName).isEqualTo(entry.cardHolderName)
                assertThat(savedCard.cardType).isEqualTo(entry.cardType)
                assertThat(savedCard.isFavourite).isEqualTo(entry.isFavourite)
                assertThat(savedCard.updatedAt).isEqualTo(entry.updatedAt)
            }
        }
    }

    @Test
    fun getActiveCardsListReturnsEmptyListIfNoActiveCardsAvailable() {
        runTest {
            val archivedCards = getArchivedCards()
            cardsDao.addMultipleCard(archivedCards)

            val allActiveCards = cardsDao.getActiveCardsList()
            assertThat(allActiveCards).isEmpty()
        }
    }

    @Test
    fun getActiveCardsListReturnsOnlyNonArchivedCards() {
        runTest {
            val activeCards = getActiveCards()
            val archivedCards = getArchivedCards()

            cardsDao.addMultipleCard(activeCards)
            cardsDao.addMultipleCard(archivedCards)

            val allActiveCards = cardsDao.getActiveCardsList()
            assertThat(allActiveCards.size).isEqualTo(activeCards.size)
        }

    }

    @Test
    fun getArchivedCardReturnsEmptyListIfNoArchivedCardsAvailable() {
        runTest {
            val activeCards = getActiveCards()
            cardsDao.addMultipleCard(activeCards)

            val allArchivedCards = cardsDao.getArchivedCardsList()
            assertThat(allArchivedCards).isEmpty()
        }

    }

    @Test
    fun getArchivedCardReturnsOnlyArchivedCards() {
        runTest {
            val activeCards = getActiveCards()
            val archivedCards = getArchivedCards()

            cardsDao.addMultipleCard(activeCards)
            cardsDao.addMultipleCard(archivedCards)

            val allArchivedCards = cardsDao.getArchivedCardsList()
            assertThat(allArchivedCards.size).isEqualTo(archivedCards.size)
        }

    }

    @Test
    fun getSingleCardReturnsNullIfNoMatchingCardIsFound() {
        runTest {
            val cardsList = getActiveCards()

            cardsDao.addMultipleCard(cardsList)

            val cardData = cardsDao.getSingleCard(30)
            assertThat(cardData).isNull()
        }
    }

    @Test
    fun getSingleCardReturnsCardTableWithCorrectData() {
        runTest {
            val cardData = getActiveCards(1).first()
            val id = cardsDao.addCard(cardData)
            val savedCardData = cardsDao.getSingleCard(id)

            assertThat(savedCardData).isNotNull()
            assertThat(cardData.cardNumber).isEqualTo(savedCardData!!.cardNumber)
            assertThat(cardData.cardName).isEqualTo(savedCardData.cardName)
            assertThat(cardData.cardType).isEqualTo(savedCardData.cardType)
            assertThat(cardData.securityCode).isEqualTo(savedCardData.securityCode)
        }
    }

    @Test
    fun searchActiveCardsReturnEmptyListIfNoMatchingActiveCardsToTheQueryFound() {
        runTest {
            val cardsList = getActiveCards()
            val searchQuery = "random_query"
            cardsDao.addMultipleCard(cardsList)

            val results = cardsDao.searchActiveCards(searchQuery)
            assertThat(results).isEmpty()
        }
    }

    @Test
    fun searchActiveCardsReturnsListOfActiveCardsMatchingQueryInAscendingOrderOfCardNames() {
        runTest {
            val cardsList = getActiveCards()
            val searchQuery = "Visa"
            cardsDao.addMultipleCard(cardsList)

            val results = cardsDao.searchActiveCards(searchQuery)

            assertThat(results.size).isEqualTo(cardsList.filter {
                it.cardName.contains(
                    searchQuery,
                    ignoreCase = true
                )
            }.size)
        }
    }

    @Test
    fun searchArchivedCardsReturnEmptyListIfNoMatchingArchivedCardsToTheQueryFound() {
        runTest {
            val cardsList = getArchivedCards()
            val searchQuery = "randomquery"
            cardsDao.addMultipleCard(cardsList)

            val results = cardsDao.searchArchivedCards(searchQuery)
            assertThat(results).isEmpty()
        }
    }

    @Test
    fun searchArchivedCardsReturnsListOfOnlyArchivedCardsMatchingQueryInAscendingOrderOfCardNames() {
        runTest {
            val cardsList = getArchivedCards()
            val searchQuery = "Amex"
            cardsDao.addMultipleCard(cardsList)

            val results = cardsDao.searchArchivedCards(searchQuery)

            assertThat(results.size).isEqualTo(cardsList.filter {
                it.cardName.contains(
                    searchQuery,
                    ignoreCase = true
                )
            }.size)
        }

    }

    @Test
    fun getFavouriteCardsReturnsOnlyFavouriteCardsInDescendingOrderOfDateUpdated() {
        runTest {
            val activeCards = getActiveCards()
            cardsDao.addMultipleCard(activeCards)

            val favouriteCards =
                cardsDao.getFavouriteCardsList().filter { it.isFavourite }.toMutableList()
                    .sortedByDescending { it.updatedAt }

            val savedFavouriteCards = cardsDao.getFavouriteCardsList()

            assertThat(favouriteCards.size).isEqualTo(savedFavouriteCards.size)

            assertTrue(savedFavouriteCards.all { it.isFavourite })

            favouriteCards.forEachIndexed { index, favrt ->
                val savedFavouriteCards = savedFavouriteCards[index]
                assertThat(favrt.cardNumber).isEqualTo(savedFavouriteCards.cardNumber)
                assertThat(favrt.cardName).isEqualTo(savedFavouriteCards.cardName)
                assertThat(favrt.cardType).isEqualTo(savedFavouriteCards.cardType)
                assertThat(favrt.cardHolderName).isEqualTo(savedFavouriteCards.cardHolderName)
                assertThat(favrt.isFavourite).isEqualTo(savedFavouriteCards.isFavourite)
                assertThat(favrt.updatedAt).isEqualTo(savedFavouriteCards.updatedAt)
            }
        }
    }


    private fun getActiveCards(count: Int = 5): List<CardEntity> {
        val cardsList = arrayListOf(
            CardEntity(
                id = 0,
                cardNumber = "1234567890123456",
                expiryMonth = "12",
                expiryYear = "2022",
                securityCode = 123,
                cardName = "Visa Card",
                cardType = null,
                cardHolderName = "John Doe",
                notes = "",
                isFavourite = false,
                isArchived = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = 100004
            ),
            CardEntity(
                id = 0,
                cardNumber = "6543210987654321",
                expiryMonth = "01",
                expiryYear = "2025",
                securityCode = 987,
                cardName = "Mastercard Card",
                cardType = null,
                cardHolderName = "Jane Smith",
                notes = "",
                isFavourite = false,
                isArchived = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = 100001
            ),
            CardEntity(
                id = 0,
                cardNumber = "1112223334444555",
                expiryMonth = "07",
                expiryYear = "2027",
                securityCode = 888,
                cardName = "Visa Electron Card",
                cardType = null,
                cardHolderName = "Alice Johnson",
                notes = "",
                isFavourite = true,
                isArchived = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = 100007
            ),
            CardEntity(
                id = 0,
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
                updatedAt = 100000
            ),
            CardEntity(
                id = 0,
                cardNumber = "9876543210987654",
                expiryMonth = "11",
                expiryYear = "2023",
                securityCode = 666,
                cardName = "Diners Visa Card",
                cardType = null,
                cardHolderName = "Charlie Davis",
                notes = "",
                isFavourite = true,
                isArchived = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = 100005
            )
        )
        return cardsList.take(count)
    }

    private fun getArchivedCards() = listOf(
        CardEntity(
            id = 0,
            cardNumber = "97234444555",
            expiryMonth = "07",
            expiryYear = "2037",
            securityCode = 888,
            cardName = "Visa Atom Card",
            cardType = null,
            cardHolderName = "Monalisa Hugh",
            notes = "",
            isFavourite = false,
            isArchived = true,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        ),
        CardEntity(
            id = 0,
            cardNumber = "7890123427632",
            expiryMonth = "03",
            expiryYear = "2032",
            securityCode = null,
            cardName = "Amex Card",
            cardType = null,
            cardHolderName = "George Brown",
            notes = "",
            isFavourite = false,
            isArchived = true,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        ),
        CardEntity(
            id = 0,
            cardNumber = "2783497237632",
            expiryMonth = "06",
            expiryYear = "2042",
            securityCode = null,
            cardName = "Amex Gift Card",
            cardType = null,
            cardHolderName = "Json Bourne",
            notes = "",
            isFavourite = false,
            isArchived = true,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    )
}