package cypher.hushlet.features.dashboard.ui.events

import cypher.hushlet.features.dashboard.domain.models.DashboardData

sealed class DashboardUiState {
    data class Success(val dashboardData: DashboardData) : DashboardUiState()
    object Loading : DashboardUiState()
}