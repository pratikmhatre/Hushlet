package cypher.hushlet.features.add_credentials.ui.states

import cypher.hushlet.core.domain.models.AccountDto
import cypher.hushlet.core.domain.models.CardDto

sealed class UiState {
    class AddAccountState(val generatedPassword: String) : UiState()
    object AddCardState : UiState()
    class EditAccountState(val account: AccountDto) : UiState()
    class EditCardState(val card: CardDto) : UiState()
}