package cypher.hushlet.features.dashboard.domain.usecases

import cypher.hushlet.core.domain.models.AccountListItemDto
import cypher.hushlet.core.domain.models.CardListItemDto
import cypher.hushlet.core.utils.AppConstants
import cypher.hushlet.core.domain.repositories.AccountsRepository
import cypher.hushlet.core.domain.repositories.CardsRepository
import cypher.hushlet.features.dashboard.domain.models.AccountsListItem
import cypher.hushlet.features.dashboard.domain.models.DashboardContent
import cypher.hushlet.features.dashboard.domain.models.DashboardData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetDashboardData @Inject constructor(
    private val cardsRepository: CardsRepository, private val accountsRepository: AccountsRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun invoke(): Flow<DashboardData> {
        val cardSectionContent: Flow<DashboardContent<CardListItemDto>> =
            cardsRepository.getFavouriteActiveCards().flatMapLatest { favoriteCards ->

                if (favoriteCards.isNotEmpty()) {
                    flowOf(DashboardContent.FavoriteContent(favoriteCards))
                } else {
                    cardsRepository.getRecentlyAddedCards(AppConstants.RECENTLY_ADDED_ITEMS_COUNT).mapLatest {
                        if (it.isNotEmpty()) {
                            DashboardContent.RecentContent(it)
                        } else {
                            DashboardContent.NoContent()
                        }
                    }
                }
            }

        val accountsSectionContent: Flow<DashboardContent<AccountListItemDto>> =
            accountsRepository.getFavouriteAccounts().flatMapLatest { favoriteAccounts ->
                if (favoriteAccounts.isNotEmpty()) {
                    flowOf(DashboardContent.FavoriteContent(favoriteAccounts))
                } else {
                    accountsRepository.getRecentlyAddedAccounts(AppConstants.RECENTLY_ADDED_ITEMS_COUNT).map {
                        if (it.isNotEmpty()) {
                            DashboardContent.RecentContent(it)
                        } else {
                            DashboardContent.NoContent()
                        }
                    }
                }

            }
        return combine(cardSectionContent, accountsSectionContent) { cardData, accData ->
            DashboardData(
                cardData,
                accData
            )
        }
    }

    fun getAllAccounts(): Flow<List<AccountsListItem>> = flow {
        val resultList = ArrayList<AccountsListItem>()
        accountsRepository.getAllActiveAccounts().collect { accountsList ->
            val sortedAccounts = accountsList.sortedBy {
                it.accountName
            }.groupBy {
                it.accountName.first()
            }
            for (entry in sortedAccounts) {
                resultList.add(AccountsListItem.SectionHeader(entry.key.toString()))
                resultList.addAll(entry.value.map {
                    AccountsListItem.AccountData(it)
                })
            }
            emit(resultList)
        }
    }
}