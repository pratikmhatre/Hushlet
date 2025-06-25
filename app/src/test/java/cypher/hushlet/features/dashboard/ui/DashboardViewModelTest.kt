package cypher.hushlet.features.dashboard.ui

import cypher.hushlet.core.domain.models.AccountListItemDto
import cypher.hushlet.core.domain.models.CardListItemDto
import cypher.hushlet.core.domain.usecases.GetAccountDetails
import cypher.hushlet.core.domain.usecases.GetCardDetails
import cypher.hushlet.features.dashboard.domain.usecases.GetDashboardData
import io.mockk.mockk
import org.junit.Test

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


//    private fun mockFlows(state: DashboardDataStates): DashboardData {}

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