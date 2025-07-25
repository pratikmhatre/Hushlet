package cypher.hushlet.features.dashboard.ui.events

import cypher.hushlet.core.domain.models.AccountDto
import cypher.hushlet.core.domain.models.CardDto

sealed class DashboardUiEvents {
    object OnCopySuccessful : DashboardUiEvents()
    data class ShowCardDetailsDialog(val cardDto: CardDto) : DashboardUiEvents()
    data class ShowAccountDetailsDialog(val accountDto: AccountDto) : DashboardUiEvents()
}