package cypher.hushlet.features.add_credentials.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cypher.hushlet.core.domain.models.AccountDto
import cypher.hushlet.core.domain.models.CardDto
import cypher.hushlet.core.domain.usecases.GetAccountDetails
import cypher.hushlet.core.domain.usecases.GetCardDetails
import cypher.hushlet.features.add_credentials.domain.usecases.DeleteAccount
import cypher.hushlet.features.add_credentials.domain.usecases.DeleteCard
import cypher.hushlet.features.add_credentials.domain.usecases.SaveNewAccount
import cypher.hushlet.features.add_credentials.domain.usecases.SaveNewCard
import cypher.hushlet.core.domain.usecases.UpdateAccount
import cypher.hushlet.core.domain.usecases.UpdateCard
import cypher.hushlet.features.add_credentials.domain.usecases.CheckIfAccountNameExists
import cypher.hushlet.features.add_credentials.domain.usecases.CheckIfCardNameExists
import cypher.hushlet.features.add_credentials.ui.models.FormFieldState
import cypher.hushlet.features.add_credentials.ui.states.UiEvent
import cypher.hushlet.features.add_credentials.ui.states.UiState
import cypher.hushlet.features.add_credentials.utils.DataValidator.validateAccountName
import cypher.hushlet.features.add_credentials.utils.DataValidator.validateCardName
import cypher.hushlet.features.add_credentials.utils.DataValidator.validateCardNumber
import cypher.hushlet.features.add_credentials.utils.DataValidator.validatePassword
import cypher.hushlet.features.add_credentials.utils.DataValidator.validateUserName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditCredentialsViewModel @Inject constructor(
    private val saveNewAccount: SaveNewAccount,
    private val saveNewCard: SaveNewCard,
    private val updateAccount: UpdateAccount,
    private val updateCard: UpdateCard,
    private val deleteAccount: DeleteAccount,
    private val deleteCard: DeleteCard,
    private val checkIfAccountNameExists: CheckIfAccountNameExists,
    private val checkIfCardNameExists: CheckIfCardNameExists,
    private val getAccountDetails: GetAccountDetails,
    private val getCardDetails: GetCardDetails
) : ViewModel() {

    private var isAccount = true
    private var cardDto: CardDto? = null
    private var accountDto: AccountDto? = null

    private var _uiStateFlow = MutableStateFlow<UiState>(UiState.AddCardState)
    val uiStateFlow = _uiStateFlow.asStateFlow()

    private var _uiEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow = _uiEventFlow.asSharedFlow()

    var accountNameState = MutableStateFlow(FormFieldState())
        private set

    var userNameState = MutableStateFlow(FormFieldState())
        private set

    var passwordState = MutableStateFlow(FormFieldState())
        private set

    var urlState = MutableStateFlow(FormFieldState())
        private set

    var notesState = MutableStateFlow(FormFieldState())
        private set

    var favouriteState = mutableStateOf(false)
        private set

    var cardNameState = MutableStateFlow(FormFieldState())
        private set
    var cardNumberState = MutableStateFlow(FormFieldState())
        private set
    var cardHolderNameState = MutableStateFlow(FormFieldState())
        private set
    var expMonthState = MutableStateFlow(FormFieldState())
        private set
    var expYearState = MutableStateFlow(FormFieldState())
        private set
    var secCodeState = MutableStateFlow(FormFieldState())
        private set


    fun onAccountNameChanged(value: String) {
        accountNameState.value = accountNameState.value.copy(value = value)
    }

    fun onUserNameChanged(value: String) {
        userNameState.value = userNameState.value.copy(value = value)
    }

    fun onPasswordChanged(value: String) {
        passwordState.value = passwordState.value.copy(value = value)
    }

    fun onUrlChanged(value: String) {
        urlState.value = urlState.value.copy(value = value)
    }

    fun onNotesChanged(value: String) {
        notesState.value = notesState.value.copy(value = value)
    }

    fun onCardNameChanged(value: String) {
        cardNameState.value = cardNameState.value.copy(value = value)
    }

    fun onCardNumberChanged(value: String) {
        cardNumberState.value = cardNumberState.value.copy(value = value)
    }

    fun onCardHolderNameChanged(value: String) {
        cardHolderNameState.value = cardHolderNameState.value.copy(value = value)
    }

    fun onExpMonthChanged(value: String) {
        expMonthState.value = expMonthState.value.copy(value = value)
    }

    fun onExpYearChanged(value: String) {
        expYearState.value = expYearState.value.copy(value = value)
    }

    fun onSecCodeChanged(value: String) {
        secCodeState.value = secCodeState.value.copy(value = value)
    }

    fun onFavouriteChanged(value: Boolean) {
        favouriteState.value = value
    }


    init {
        resetForm()
        _uiStateFlow.value =
            if (isAccount) UiState.AddAccountState else UiState.AddCardState
    }

    fun onAccountIdReceived(id: Long) {
        viewModelScope.launch {
            accountDto = getAccountDetails(id)
            accountDto?.run {
                isAccount = true
                accountNameState.value = accountNameState.value.copy(value = title)
                userNameState.value = userNameState.value.copy(value = username ?: "")
                passwordState.value = passwordState.value.copy(value = password)
                urlState.value = urlState.value.copy(value = url ?: "")
                notesState.value = notesState.value.copy(value = notes ?: "")
                favouriteState.value = isFavourite
            }
            _uiStateFlow.value = UiState.EditAccountState
        }
    }


    private fun resetForm() {
        accountNameState.value = accountNameState.value.copy(value = "")
        userNameState.value = userNameState.value.copy(value = "")
        passwordState.value = passwordState.value.copy(value = "")
        urlState.value = urlState.value.copy(value = "")
        notesState.value = notesState.value.copy(value = "")
        favouriteState.value = false
    }

    fun onDeleteConfirmed() {
        viewModelScope.launch {
            if (isAccount) deleteAccount(accountDto!!) else deleteCard(cardDto!!)
        }
    }

    suspend fun validateAccountData(
        title: String, userName: String, password: String, checkAccountNameExists: CheckIfAccountNameExists
    ): Boolean {

        accountNameState.value =
            accountNameState.value.copy(errorData = validateAccountName(title, checkAccountNameExists))
        userNameState.value = userNameState.value.copy(errorData = validateUserName(userName))
        passwordState.value = passwordState.value.copy(errorData = validatePassword(password))

        return accountNameState.value.errorData.hasError.not() && userNameState.value.errorData.hasError.not() && passwordState.value.errorData.hasError.not()
    }

    suspend fun validateCardData(
        cardName: String, cardNumber: String, checkAccountNameExists: CheckIfCardNameExists
    ): Boolean {
        cardNameState.value = cardNameState.value.copy(errorData = validateCardName(cardName, checkAccountNameExists))
        cardHolderNameState.value = cardHolderNameState.value.copy(errorData = validateCardNumber(cardNumber))
        return cardNameState.value.hasError || cardHolderNameState.value.hasError
    }


    fun onAccountSubmit() {
        viewModelScope.launch {
            val isEditJourney = _uiStateFlow.value is UiState.EditAccountState

            val title = accountNameState.value.value
            val userName = userNameState.value.value
            val password = passwordState.value.value
            val isFavourite = favouriteState.value
            val url = urlState.value.value
            val notes = notesState.value.value

            val isDataValid = validateAccountData(title, userName, password, checkIfAccountNameExists)
            if (isDataValid.not()) return@launch

            val account = AccountDto(
                id = if (isEditJourney) accountDto!!.id else 0,
                title = title,
                username = userName,
                password = password,
                isFavourite = isFavourite,
                isArchived = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                url = url,
                notes = notes
            )
            if (isEditJourney) updateAccount(account) else saveNewAccount(account)
            _uiEventFlow.emit(UiEvent.OnDataSaved)
        }
    }

    fun onCardSubmit() {
        viewModelScope.launch {
            val cardName = cardNameState.value.value
            val cardNumber = cardNumberState.value.value
            val expiryMonth = expMonthState.value.value
            val expiryYear = expYearState.value.value
            val securityCode = secCodeState.value.value.toIntOrNull()
            val cardType = null
            val cardHolderName = cardHolderNameState.value.value
            val notes = notesState.value.value
            val isFavourite = favouriteState.value

            val isCardDataValid = validateCardData(cardName, cardNumber, checkIfCardNameExists)
            if (isCardDataValid.not()) return@launch

            val cardDto = CardDto(
                id = 0,
                cardName = cardName,
                cardNumber = cardNumber,
                cardHolderName = cardHolderName,
                expiryMonth = expiryMonth,
                expiryYear = expiryYear,
                securityCode = securityCode,
                cardType = cardType,
                notes = notes,
                isFavourite = isFavourite,
                isArchived = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            if (_uiStateFlow.value is UiState.EditCardState) updateCard(cardDto) else saveNewCard(cardDto)
            _uiEventFlow.emit(UiEvent.OnDataSaved)
        }
    }
}
