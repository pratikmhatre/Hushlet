package cypher.hushlet.features.dashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cypher.hushlet.features.dashboard.domain.models.DashboardContent
import cypher.hushlet.core.domain.usecases.GetAccountDetails
import cypher.hushlet.core.domain.usecases.GetCardDetails
import cypher.hushlet.features.dashboard.domain.usecases.GetDashboardData
import cypher.hushlet.features.dashboard.ui.events.DashboardUiEvents
import cypher.hushlet.features.dashboard.ui.events.DashboardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
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
            getDashboardData.invoke().collectLatest {data ->
                if (data.cardSectionContent !is DashboardContent.NoContent || data.accountSectionContent !is DashboardContent.NoContent) {
                    _dashboardUiEvents.emit(DashboardUiEvents.HideEmptyState)
                    _dashboardUiState.emit(DashboardUiState.DashboardDataState(data))
                } else {
                    _dashboardUiEvents.emit(DashboardUiEvents.ShowEmptyState)
                }
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