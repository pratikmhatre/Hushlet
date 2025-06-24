package cypher.hushlet.features.dashboard.ui

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cypher.hushlet.R
import cypher.hushlet.features.dashboard.domain.models.DashboardContent
import cypher.hushlet.features.dashboard.domain.usecases.GetAccountDetails
import cypher.hushlet.features.dashboard.domain.usecases.GetCardDetails
import cypher.hushlet.features.dashboard.domain.usecases.GetDashboardData
import cypher.hushlet.features.dashboard.ui.events.DashboardUiEvents
import cypher.hushlet.features.dashboard.ui.events.DashboardUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel constructor(
    private val getDashboardData: GetDashboardData,
    private val getCardDetails: GetCardDetails,
    private val getAccountDetails: GetAccountDetails
) : ViewModel() {
    private val _dashboardUiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val dashboardUiState = _dashboardUiState.asStateFlow()

    private val _dashboardUiEvents = MutableSharedFlow<DashboardUiEvents>()
    val dashboardUiEvents = _dashboardUiEvents.asSharedFlow()

    init {
        fetchDashboardData()
    }

    private fun fetchDashboardData() {
        viewModelScope.launch {
            _dashboardUiState.value = DashboardUiState.Loading
            val dashboardData = getDashboardData.invoke()
            if (dashboardData.cardSectionContent !is DashboardContent.NoContent || dashboardData.accountSectionContent !is DashboardContent.NoContent) {
                _dashboardUiEvents.emit(DashboardUiEvents.HideEmptyState)
                _dashboardUiState.emit(DashboardUiState.DashboardDataState(dashboardData))
            } else {
                _dashboardUiEvents.emit(DashboardUiEvents.ShowEmptyState)
            }
        }
    }

    fun onCardSelected(cardId: Long) {
        viewModelScope.launch {
            getCardDetails.invoke(cardId)?.let {
                _dashboardUiEvents.emit(DashboardUiEvents.ShowCardDetailsDialog(it))
            }
        }
    }

    fun onAccountSelected(accountId: Long) {
        viewModelScope.launch {
            getAccountDetails.invoke(accountId)?.let {
                _dashboardUiEvents.emit(DashboardUiEvents.ShowAccountDetailsDialog(it))
            }
        }
    }
}