package cypher.hushlet.features.dashboard.domain.models

import cypher.hushlet.core.domain.models.AccountListItemDto
import cypher.hushlet.core.domain.models.CardListItemDto

data class DashboardData(
    val cardSectionContent: DashboardContent<CardListItemDto>,
    val accountSectionContent: DashboardContent<AccountListItemDto>
)

sealed class DashboardContent<T> {
    data class FavoriteContent<T>(val data: List<T>) : DashboardContent<T>()
    data class RecentContent<T>(val data: List<T>) : DashboardContent<T>()
    data class NoContent<T>(val data: List<T> = emptyList<T>()) : DashboardContent<T>()
}