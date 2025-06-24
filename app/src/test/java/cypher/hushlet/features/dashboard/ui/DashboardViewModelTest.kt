package cypher.hushlet.features.dashboard.ui

import com.nhaarman.mockitokotlin2.whenever
import cypher.hushlet.core.domain.models.AccountListItemDto
import cypher.hushlet.core.domain.models.CardListItemDto
import cypher.hushlet.features.dashboard.domain.models.DashboardData
import cypher.hushlet.features.dashboard.domain.usecases.GetAccountDetails
import cypher.hushlet.features.dashboard.domain.usecases.GetCardDetails
import cypher.hushlet.features.dashboard.domain.usecases.GetDashboardData
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class DashboardViewModelTest {
    private enum class DashboardDataStates {
        FavrtAccountsWithFavrtCards, FavrtAccountsWithRecentCards,
        FavrtAccountsWithNoCards, FavrtCardsWithRecentAccounts, FavrtCardsWithNoAccounts, NoCardsWithNoAccounts
    }

    private lateinit var viewModel: DashboardViewModel
    private val getDashboardData = mockk<GetDashboardData>()
    private val getCardDetails = mockk<GetCardDetails>()
    private val getAccountDetails = mockk<GetAccountDetails>()

    private val favoriteCards = listOf<CardListItemDto>()
    private val recentCards = listOf<CardListItemDto>()
    private val favoriteAccounts = listOf<AccountListItemDto>()
    private val recentAccounts = listOf<AccountListItemDto>()


    private fun mockFlows(state: DashboardDataStates): DashboardData {
        return when (state) {
            DashboardDataStates.FavrtAccountsWithFavrtCards -> {
                DashboardData(
                    isCardsAvailable = true,
                    isFavoriteCardsAvailable = true,
                    isFavoriteAccountsAvailable = true,
                    isAccountsAvailable = true,
                    cardsList = favoriteCards,
                    accountsList = favoriteAccounts
                )
            }

            DashboardDataStates.FavrtAccountsWithRecentCards -> {
                DashboardData(
                    isCardsAvailable = true,
                    isAccountsAvailable = true,
                    isFavoriteAccountsAvailable = true,
                    isFavoriteCardsAvailable = false,
                    accountsList = favoriteAccounts,
                    cardsList = recentCards,
                )
            }

            DashboardDataStates.FavrtAccountsWithNoCards -> {
                DashboardData(
                    isCardsAvailable = false,
                    isAccountsAvailable = true,
                    isFavoriteAccountsAvailable = true,
                    isFavoriteCardsAvailable = false,
                    accountsList = favoriteAccounts,
                    cardsList = listOf(),
                )
            }

            DashboardDataStates.FavrtCardsWithNoAccounts -> {
                DashboardData(
                    isCardsAvailable = true,
                    isFavoriteCardsAvailable = true,
                    isFavoriteAccountsAvailable = false,
                    isAccountsAvailable = false,
                    cardsList = favoriteCards,
                    accountsList = listOf()
                )
            }

            DashboardDataStates.FavrtCardsWithRecentAccounts -> {
                DashboardData(
                    isCardsAvailable = true,
                    isFavoriteCardsAvailable = true,
                    isFavoriteAccountsAvailable = false,
                    isAccountsAvailable = true,
                    cardsList = favoriteCards,
                    accountsList = recentAccounts
                )
            }

            DashboardDataStates.NoCardsWithNoAccounts -> {
                DashboardData(
                    isCardsAvailable = false,
                    isFavoriteCardsAvailable = false,
                    isFavoriteAccountsAvailable = false,
                    isAccountsAvailable = false,
                    cardsList = listOf(),
                    accountsList = listOf()
                )
            }
        }
    }

    @Test
    fun `test invoke of get dashboard data is called once on first time`() {

    }

    @Test
    fun `test loading dashboard ui state is emitted on first time`() {

    }

    @Test
    fun `test show empty state ui event is emitted when there is no card and account data available`() {

    }
}