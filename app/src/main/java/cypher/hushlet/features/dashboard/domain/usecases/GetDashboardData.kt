package cypher.hushlet.features.dashboard.domain.usecases

import cypher.hushlet.core.domain.models.AccountListItemDto
import cypher.hushlet.core.domain.models.CardListItemDto
import cypher.hushlet.core.utils.AppConstants
import cypher.hushlet.core.domain.repositories.AccountsRepository
import cypher.hushlet.core.domain.repositories.CardsRepository
import cypher.hushlet.features.dashboard.domain.models.DashboardContent
import cypher.hushlet.features.dashboard.domain.models.DashboardData

class GetDashboardData constructor(
    private val cardsRepository: CardsRepository, private val accountsRepository: AccountsRepository
) {
    suspend fun invoke(): DashboardData {
        val favoriteCards = cardsRepository.getFavouriteActiveCards()
        val cardSectionContent = if (favoriteCards.isNotEmpty()) {
            DashboardContent.FavoriteContent(favoriteCards)
        } else {
            val recentCards =
                cardsRepository.getRecentlyAddedCards(AppConstants.RECENTLY_ADDED_ITEMS_COUNT)
            if (recentCards.isNotEmpty()) {
                DashboardContent.RecentContent(recentCards)
            } else {
                DashboardContent.NoContent()
            }
        }


        val favoriteAccounts = accountsRepository.getFavouriteAccounts()
        val accountSectionContent = if (favoriteAccounts.isNotEmpty()) {
            DashboardContent.FavoriteContent(favoriteAccounts)
        } else {
            val recentAccounts =
                accountsRepository.getRecentlyAddedAccounts(AppConstants.RECENTLY_ADDED_ITEMS_COUNT)
            if (recentAccounts.isNotEmpty()) {
                DashboardContent.RecentContent(recentAccounts)
            } else {
                DashboardContent.NoContent()
            }
        }

        return DashboardData(cardSectionContent, accountSectionContent)
    }
}