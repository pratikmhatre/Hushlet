package cypher.hushlet.features.dashboard.domain.usecases

import cypher.hushlet.core.utils.AppConstants
import cypher.hushlet.core.domain.repositories.AccountsRepository
import cypher.hushlet.core.domain.repositories.CardsRepository
import cypher.hushlet.features.dashboard.domain.models.DashboardData

class GetDashboardData constructor(
    private val cardsRepository: CardsRepository, private val accountsRepository: AccountsRepository
) {
    suspend fun invoke(): DashboardData {
        val favoriteCards = cardsRepository.getFavouriteActiveCards()
        val cardsToDisplay = favoriteCards.ifEmpty {
            cardsRepository.getRecentlyAddedCards(AppConstants.RECENTLY_ADDED_ITEMS_COUNT)
        }

        val favoriteAccounts = accountsRepository.getFavouriteAccounts()
        val accountsToDisplay = favoriteAccounts.ifEmpty {
            accountsRepository.getRecentlyAddedAccounts(AppConstants.RECENTLY_ADDED_ITEMS_COUNT)
        }

        return DashboardData(
            isCardsAvailable = cardsToDisplay.isNotEmpty(),
            isFavoriteCardsAvailable = favoriteCards.isNotEmpty(),
            cardsList = cardsToDisplay,
            isFavoriteAccountsAvailable = favoriteAccounts.isNotEmpty(),
            accountsList = accountsToDisplay, isAccountsAvailable = accountsToDisplay.isNotEmpty()
        )
    }
}