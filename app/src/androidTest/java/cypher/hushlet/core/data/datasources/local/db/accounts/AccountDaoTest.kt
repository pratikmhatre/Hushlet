package cypher.hushlet.core.data.datasources.local.db.accounts

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import app.cash.turbine.test
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
class AccountDaoTest {

    private lateinit var accountsDao: AccountsDao
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
        accountsDao = hushletDb.getAccountsDao()
    }

    @After
    fun tearDown() {
        hushletDb.close()
    }

    @Test
    fun addAccountFunctionReturnsPrimaryKey() {
        runTest {
            val accountData = getActiveAccounts(1).first()
            val id = accountsDao.addAccount(accountData)
            assertThat(id).isNotEqualTo(0L)
        }
    }

    @Test
    fun addAccountFunctionIgnoresDuplicateInsertion() {
        runTest {
            val accountData = getActiveAccounts(1).first()
            val firstInsertion = accountsDao.addAccount(accountData)
            val insertedAccount = accountsDao.getSingleAccount(firstInsertion)
            val secondInsertion = accountsDao.addAccount(insertedAccount!!)
            assertThat(secondInsertion).isEqualTo(-1L)
        }
    }

    @Test
    fun addAccountFunctionAddsNewAccountWithProperData() {
        runTest {
            val accountData = getActiveAccounts(1).first()
            val id = accountsDao.addAccount(accountData)

            val insertedAccount = accountsDao.getSingleAccount(id)

            assertThat(insertedAccount).isNotNull()
            assertThat(insertedAccount!!.accountName).isEqualTo(accountData.accountName)
            assertThat(insertedAccount.username).isEqualTo(accountData.username)
            assertThat(insertedAccount.password).isEqualTo(accountData.password)
            assertThat(insertedAccount.url).isEqualTo(accountData.url)
            assertThat(insertedAccount.notes).isEqualTo(accountData.notes)
            assertThat(insertedAccount.isFavourite).isEqualTo(accountData.isFavourite)
            assertThat(insertedAccount.isArchived).isEqualTo(accountData.isArchived)
            assertThat(insertedAccount.createdAt).isEqualTo(accountData.createdAt)
            assertThat(insertedAccount.updatedAt).isEqualTo(accountData.updatedAt)
        }
    }

    @Test
    fun addMultipleAccountFunctionStoresMultipleAccountsAndReturnsListOfIds() {
        runTest {
            val activeAccounts = getActiveAccounts()
            val ids = accountsDao.addMultipleAccounts(activeAccounts)
            assertThat(ids.size).isEqualTo(activeAccounts.size)
        }
    }


    @Test
    fun updateAccountUpdatesDetailsOfGivenAccount() {
        runTest {
            val account = getActiveAccounts(1).first()
            val changedTitle = "Common Wealth Bank"
            val changedUsername = "stellaT"

            val id = accountsDao.addAccount(account)
            val savedAccountData = accountsDao.getSingleAccount(id)

            assertThat(savedAccountData).isNotNull()

            val accountToUpdate = AccountEntity(
                id = savedAccountData!!.id,
                accountName = changedTitle,
                username = changedUsername,
                password = savedAccountData.password,
                url = savedAccountData.url,
                notes = savedAccountData.notes,
                isFavourite = savedAccountData.isFavourite,
                isArchived = savedAccountData.isArchived,
                createdAt = savedAccountData.createdAt,
                updatedAt = savedAccountData.updatedAt
            )

            val pk = accountsDao.updateAccount(accountToUpdate)

            val updatedAccountData = accountsDao.getSingleAccount(pk.toLong())

            assertThat(updatedAccountData).isNotNull()
            assertThat(updatedAccountData!!.accountName).isEqualTo(changedTitle)
            assertThat(updatedAccountData.username).isEqualTo(changedUsername)
        }
    }

    @Test
    fun updateAccountReturnsZeroIfNoMatchingAccountFoundToUpdate() {
        runTest {
            val accountsList = getActiveAccounts(2)
            val firstAccount = accountsList[0]
            val secondAccount = accountsList[1]

            accountsDao.addAccount(firstAccount)

            val id = accountsDao.updateAccount(secondAccount)

            assertThat(id).isEqualTo(0L)
        }
    }

    @Test
    fun deleteAccountRemovesAccountDetails() {
        runTest {
            val accountsList = getActiveAccounts()
            val insertedAccounts = accountsDao.addMultipleAccounts(accountsList)

            val accountToDelete = insertedAccounts[2]
            val accToDelete = accountsDao.getSingleAccount(accountToDelete)

            assertThat(accToDelete).isNotNull()

            accountsDao.deleteAccount(accToDelete!!)

            val id = accountsDao.getSingleAccount(accToDelete.id)
            assertThat(id).isNull()
        }
    }

    @Test
    fun deleteAccountReturnsZeroIfNoMatchingAccountFoundToDelete() {
        runTest {
            val accountsList = getActiveAccounts(2)
            val firstAccount = accountsList[0]
            val secondAccount = accountsList[1]

            accountsDao.addAccount(firstAccount)

            val id = accountsDao.deleteAccount(secondAccount)

            assertThat(id).isEqualTo(0L)
        }
    }

    @Test
    fun deleteAllAccountRemovesAllStoredAccounts() {
        runTest {
            val activeAccounts = getActiveAccounts()
            val archivedAccounts = getArchivedAccounts()

            accountsDao.addMultipleAccounts(activeAccounts)
            accountsDao.addMultipleAccounts(archivedAccounts)

            assertThat(accountsDao.getActiveAccountsList().size + accountsDao.getArchivedAccountsList().size).isEqualTo(
                activeAccounts.size + archivedAccounts.size
            )

            accountsDao.deleteAllAccounts()

            assertThat(accountsDao.getActiveAccountsList().size + accountsDao.getArchivedAccountsList().size).isEqualTo(
                0
            )

        }
    }

    @Test
    fun getActiveAccountsListReturnsAllActiveAccountsInAscendingOrderOfAccountNames() {
        runTest {
            val accountList = ArrayList(getActiveAccounts())
            accountsDao.addMultipleAccounts(accountList)

            accountList.sortBy { it.accountName }

            val savedAccounts = accountsDao.getActiveAccountsList()
            accountList.forEachIndexed { index, entry ->
                val savedAccount = savedAccounts[index]
                assertThat(savedAccount.accountName).isEqualTo(entry.accountName)
                assertThat(savedAccount.isFavourite).isEqualTo(entry.isFavourite)
                assertThat(savedAccount.url).isEqualTo(entry.url)
            }
        }
    }

    @Test
    fun getActiveAccountsListReturnsEmptyListIfNoActiveAccountsAvailable() {
        runTest {
            val archivedAccounts = getArchivedAccounts()
            accountsDao.addMultipleAccounts(archivedAccounts)

            val allActiveAccounts = accountsDao.getActiveAccountsList()
            assertThat(allActiveAccounts).isEmpty()
        }
    }

    @Test
    fun getActiveAccountsListReturnsOnlyNonArchivedAccounts() {
        runTest {
            val activeAccounts = getActiveAccounts()
            val archivedAccounts = getArchivedAccounts()

            accountsDao.addMultipleAccounts(activeAccounts)
            accountsDao.addMultipleAccounts(archivedAccounts)

            val allActiveAccounts = accountsDao.getActiveAccountsList()
            assertThat(allActiveAccounts.size).isEqualTo(activeAccounts.size)
        }

    }

    @Test
    fun getArchivedAccountReturnsEmptyListIfNoArchivedAccountsAvailable() {
        runTest {
            val activeAccounts = getActiveAccounts()
            accountsDao.addMultipleAccounts(activeAccounts)

            val allArchivedAccounts = accountsDao.getArchivedAccountsList()
            assertThat(allArchivedAccounts).isEmpty()
        }

    }

    @Test
    fun getArchivedAccountReturnsOnlyArchivedAccounts() {
        runTest {
            val activeAccounts = getActiveAccounts()
            val archivedAccounts = getArchivedAccounts()

            accountsDao.addMultipleAccounts(activeAccounts)
            accountsDao.addMultipleAccounts(archivedAccounts)

            val allArchivedAccounts = accountsDao.getArchivedAccountsList()
            assertThat(allArchivedAccounts.size).isEqualTo(archivedAccounts.size)
        }

    }

    @Test
    fun getSingleAccountReturnsNullIfNoMatchingAccountIsFound() {
        runTest {
            val accountsList = getActiveAccounts()

            accountsDao.addMultipleAccounts(accountsList)

            val accountData = accountsDao.getSingleAccount(30)
            assertThat(accountData).isNull()
        }
    }

    @Test
    fun getSingleAccountReturnsAccountTableWithCorrectData() {
        runTest {
            val accountData = getActiveAccounts(1).first()
            val id = accountsDao.addAccount(accountData)
            val savedAccountData = accountsDao.getSingleAccount(id)

            assertThat(savedAccountData).isNotNull()
            assertThat(accountData.accountName).isEqualTo(savedAccountData!!.accountName)
            assertThat(accountData.username).isEqualTo(savedAccountData.username)
            assertThat(accountData.password).isEqualTo(savedAccountData.password)
            assertThat(accountData.url).isEqualTo(savedAccountData.url)
            assertThat(accountData.notes).isEqualTo(savedAccountData.notes)
            assertThat(accountData.isFavourite).isEqualTo(savedAccountData.isFavourite)
            assertThat(accountData.isArchived).isEqualTo(savedAccountData.isArchived)
            assertThat(accountData.createdAt).isEqualTo(savedAccountData.createdAt)
            assertThat(accountData.updatedAt).isEqualTo(savedAccountData.updatedAt)
        }
    }

    @Test
    fun searchActiveAccountsReturnEmptyListIfNoMatchingActiveAccountsToTheQueryFound() {
        runTest {
            val accountsList = getActiveAccounts()
            val searchQuery = "random_query"
            accountsDao.addMultipleAccounts(accountsList)

            val results = accountsDao.searchActiveAccounts(searchQuery)
            assertThat(results).isEmpty()
        }
    }

    @Test
    fun searchActiveAccountsReturnsListOfActiveAccountsMatchingQueryInAscendingOrderOfAccountNames() {
        runTest {
            val accountsList = getActiveAccounts()
            val searchQuery = "Visa"
            accountsDao.addMultipleAccounts(accountsList)

            val results = accountsDao.searchActiveAccounts(searchQuery)

            assertThat(results.size).isEqualTo(accountsList.filter {
                it.accountName.contains(
                    searchQuery,
                    ignoreCase = true
                )
            }.size)
        }
    }

    @Test
    fun searchArchivedAccountsReturnEmptyListIfNoMatchingArchivedAccountsToTheQueryFound() {
        runTest {
            val accountsList = getArchivedAccounts()
            val searchQuery = "randomquery"
            accountsDao.addMultipleAccounts(accountsList)

            val results = accountsDao.searchArchivedAccounts(searchQuery)
            assertThat(results).isEmpty()
        }
    }

    @Test
    fun searchArchivedAccountsReturnsListOfOnlyArchivedAccountsMatchingQueryInAscendingOrderOfAccountNames() {
        runTest {
            val accountsList = getArchivedAccounts()
            val searchQuery = "Amex"
            accountsDao.addMultipleAccounts(accountsList)

            val results = accountsDao.searchArchivedAccounts(searchQuery)

            assertThat(results.size).isEqualTo(accountsList.filter {
                it.accountName.contains(
                    searchQuery,
                    ignoreCase = true
                )
            }.size)
        }

    }

    @Test
    fun getFavouriteAccountsReturnsOnlyFavouriteAccountsInDescendingOrderOfDateUpdated() {
        runTest {
            val activeAccounts = getActiveAccounts()
            accountsDao.addMultipleAccounts(activeAccounts)

            val favouriteAccounts = activeAccounts.filter { it.isFavourite }.toMutableList()
                .sortedByDescending { it.updatedAt }

            accountsDao.getFavouriteAccountsList().test {
                val savedFavorites = awaitItem()
                assertThat(favouriteAccounts.size).isEqualTo(savedFavorites.size)

                assertTrue(savedFavorites.all { it.isFavourite })
                favouriteAccounts.forEachIndexed { index, favrt ->
                    val savedFavouriteAccount = savedFavorites[index]
                    assertThat(favrt.accountName).isEqualTo(savedFavouriteAccount.accountName)
                    assertThat(favrt.url).isEqualTo(savedFavouriteAccount.url)
                }
            }


        }
    }


    private fun getActiveAccounts(count: Int = 6) = listOf(
        AccountEntity(
            id = 0,
            accountName = "Gmail",
            username = "john.doe@gmail.com",
            password = "GmailPass123!",
            url = "https://mail.google.com",
            notes = "Personal email account",
            isFavourite = true,
            isArchived = false,
            createdAt = System.currentTimeMillis(),
            updatedAt = 1005
        ),
        AccountEntity(
            id = 0,
            accountName = "Facebook",
            username = "john.doe.fb",
            password = "FbPass@2025",
            url = "https://facebook.com",
            notes = null,
            isFavourite = false,
            isArchived = false,
            createdAt = System.currentTimeMillis(),
            updatedAt = 1001
        ),
        AccountEntity(
            id = 0,
            accountName = "Twitter",
            username = "johntweets",
            password = "Tw!t#567",
            url = "https://twitter.com",
            notes = "Work-related account",
            isFavourite = false,
            isArchived = false,
            createdAt = System.currentTimeMillis(),
            updatedAt = 1003
        ),
        AccountEntity(
            id = 0,
            accountName = "Netflix",
            username = "johnflix",
            password = "Netf!ix2025",
            url = "https://netflix.com",
            notes = "Shared with family",
            isFavourite = true,
            isArchived = false,
            createdAt = System.currentTimeMillis(),
            updatedAt = 1002
        ),
        AccountEntity(
            id = 0,
            accountName = "Amazon",
            username = "johnshopper",
            password = "Am@zon#2025",
            url = "https://amazon.com",
            notes = "Prime account",
            isFavourite = false,
            isArchived = false,
            createdAt = System.currentTimeMillis(),
            updatedAt = 1004
        ),
        AccountEntity(
            id = 0,
            accountName = "GitHub",
            username = "johndev",
            password = "GH_token_234!",
            url = "https://github.com",
            notes = "Used for work repositories",
            isFavourite = true,
            isArchived = false,
            createdAt = System.currentTimeMillis(),
            updatedAt = 1006
        )
    ).take(count)

    private fun getArchivedAccounts() = listOf(
        AccountEntity(
            id = 0,
            accountName = "Reddit",
            username = "john_reddit",
            password = "R3ddit@2025",
            url = "https://reddit.com",
            notes = null,
            isFavourite = false,
            isArchived = true,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        ),
        AccountEntity(
            id = 0,
            accountName = "Spotify",
            username = "johnmusic",
            password = "Music#L0ver",
            url = "https://spotify.com",
            notes = "Student plan",
            isFavourite = true,
            isArchived = true,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        ),
        AccountEntity(
            id = 0,
            accountName = "Zoom",
            username = "john.zoom",
            password = "Zoom@meet123",
            url = "https://zoom.us",
            notes = "Company meetings",
            isFavourite = false,
            isArchived = true,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        ),
        AccountEntity(
            id = 0,
            accountName = "Bank App",
            username = "john.bank",
            password = "B@nk#Secure789",
            url = "https://mybank.com",
            notes = "Never share this password",
            isFavourite = true,
            isArchived = true,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    )

}