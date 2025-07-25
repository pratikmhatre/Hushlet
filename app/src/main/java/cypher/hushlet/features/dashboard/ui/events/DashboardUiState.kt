package cypher.hushlet.features.dashboard.ui.events

import cypher.hushlet.core.domain.models.AccountListItemDto
import cypher.hushlet.core.domain.models.CardListItemDto
import cypher.hushlet.features.dashboard.domain.models.AccountsListItem
import cypher.hushlet.features.dashboard.domain.models.DashboardData

sealed class DashboardUiState {
//    data class DashboardDataState(val dashboardData: DashboardData) : DashboardUiState()
//    object Loading : DashboardUiState()
    object EmptyState : DashboardUiState()
    data class AccountDataState(val accountList : List<AccountsListItem>): DashboardUiState()
}