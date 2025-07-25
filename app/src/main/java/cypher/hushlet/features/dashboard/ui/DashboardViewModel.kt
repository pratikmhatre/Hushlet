package cypher.hushlet.features.dashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cypher.hushlet.core.domain.usecases.GetAccountDetails
import cypher.hushlet.core.domain.usecases.GetCardDetails
import cypher.hushlet.features.dashboard.domain.usecases.GetDashboardData
import cypher.hushlet.features.dashboard.ui.events.DetailsBottomSheetState
import cypher.hushlet.features.dashboard.ui.events.DashboardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardData: GetDashboardData,
    private val getCardDetails: GetCardDetails,
    private val getAccountDetails: GetAccountDetails
) : ViewModel() {
    private val _dashboardUiState = MutableStateFlow<DashboardUiState>(DashboardUiState.EmptyState)
    val dashboardUiState = _dashboardUiState.asStateFlow()

    private val _detailsBottomSheetState = MutableSharedFlow<DetailsBottomSheetState>()
    val detailsBottomSheetState = _detailsBottomSheetState.asSharedFlow()

    init {
        fetchAccountsData()
    }

    private fun fetchAccountsData() {
        viewModelScope.launch {
            getDashboardData.getAllAccounts().collect {
                if (it.isNotEmpty()) {
                    _dashboardUiState.value = DashboardUiState.AccountDataState(it)
                } else {
                    _dashboardUiState.value = DashboardUiState.EmptyState
                }
            }
        }
    }


    fun onCardSelected(cardId: Long) {
        viewModelScope.launch {
            getCardDetails.invoke(cardId)?.let {
                _detailsBottomSheetState.emit(DetailsBottomSheetState.ShowEditCardDetailsDialog(it))
            }
        }
    }

    fun onAddAccountClicked() {
        viewModelScope.launch {
            _detailsBottomSheetState.emit(
                DetailsBottomSheetState.AddAccountDetailsBottomSheetState(
                    true
                )
            )
        }
    }

    fun onAccountSelected(accountId: Long) {
        viewModelScope.launch {
            getAccountDetails.invoke(accountId)?.let {
                _detailsBottomSheetState.emit(DetailsBottomSheetState.EditAccountDetailsBottomSheetState(true, it))
            }
        }
    }
}