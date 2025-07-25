package cypher.hushlet.features.dashboard.ui.events

import cypher.hushlet.core.domain.models.AccountDto
import cypher.hushlet.core.domain.models.CardDto

sealed class DetailsBottomSheetState(val shouldShow: Boolean) {
    data class ShowEditCardDetailsDialog(val cardDto: CardDto) : DetailsBottomSheetState(false)
    data class EditAccountDetailsBottomSheetState(val show: Boolean = false, val accountDto: AccountDto? = null) :
        DetailsBottomSheetState(show)

    data class AddAccountDetailsBottomSheetState(val show: Boolean = false) :
        DetailsBottomSheetState(show)
}