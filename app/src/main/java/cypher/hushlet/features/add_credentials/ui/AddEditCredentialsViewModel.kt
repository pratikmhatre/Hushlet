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
import cypher.hushlet.features.add_credentials.ui.states.UiState
import cypher.hushlet.features.add_credentials.utils.DataValidator.validateAccountName
import cypher.hushlet.features.add_credentials.utils.DataValidator.validateCardName
import cypher.hushlet.features.add_credentials.utils.DataValidator.validateCardNumber
import cypher.hushlet.features.add_credentials.utils.DataValidator.validatePassword
import cypher.hushlet.features.add_credentials.utils.DataValidator.validateUserName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
    private var isEditJourney = false
    private var cardDto: CardDto? = null
    private var accountDto: AccountDto? = null

    private var _uiStateFlow = MutableStateFlow<UiState>(UiState.AddCardState)
    val uiStateFlow = _uiStateFlow.asStateFlow()

    var accountNameState = mutableStateOf(FormFieldState())
        private set

    var userNameState = mutableStateOf(FormFieldState())
        private set

    var passwordState = mutableStateOf(FormFieldState())
        private set

    var urlState = mutableStateOf(FormFieldState())
        private set

    var notesState = mutableStateOf(FormFieldState())
        private set

    var favouriteState = mutableStateOf(false)
        private set

    var cardNameState = mutableStateOf(FormFieldState())
        private set
    var cardNumberState = mutableStateOf(FormFieldState())
        private set
    var cardHolderNameState = mutableStateOf(FormFieldState())
        private set
    var expMonthState = mutableStateOf(FormFieldState())
        private set
    var expYearState = mutableStateOf(FormFieldState())
        private set
    var secCodeState = mutableStateOf(FormFieldState())
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
        if (isEditJourney) {
            viewModelScope.launch {
                if (isAccount) accountDto = getAccountDetails(cardDto!!.id) else cardDto =
                    getCardDetails(accountDto!!.id)
            }
        } else {
            _uiStateFlow.value =
                if (isAccount) UiState.AddAccountState else UiState.AddCardState
        }
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

        return accountNameState.value.errorData.hasError || userNameState.value.errorData.hasError || passwordState.value.errorData.hasError
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
            saveAccountData(
                title = accountNameState.value.value,
                userName = userNameState.value.value,
                password = passwordState.value.value,
                isFavourite = favouriteState.value,
                url = urlState.value.value,
                notes = notesState.value.value
            )
        }
    }

    fun onCardSubmit() {
        viewModelScope.launch {
            saveCardData(
                cardName = cardNameState.value.value,
                cardNumber = cardNumberState.value.value,
                expiryMonth = expMonthState.value.value,
                expiryYear = expYearState.value.value,
                securityCode = secCodeState.value.value.toIntOrNull(),
                cardType = null,
                cardHolderName = cardHolderNameState.value.value,
                notes = notesState.value.value,
                isFavourite = favouriteState.value
            )
        }
    }

    private suspend fun saveAccountData(
        title: String, userName: String, password: String, isFavourite: Boolean, url: String?, notes: String = ""
    ) {
        val isDataInValid = validateAccountData(title, userName, password, checkIfAccountNameExists)
        if (isDataInValid) return

        val account = AccountDto(
            id = 0,
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

    }

    private suspend fun saveCardData(
        cardName: String,
        cardNumber: String,
        expiryMonth: String?,
        expiryYear: String?,
        securityCode: Int?,
        cardType: String?,
        cardHolderName: String?,
        notes: String,
        isFavourite: Boolean
    ) {
        val isCardDataValid = validateCardData(cardName, cardNumber, checkIfCardNameExists)
        if (isCardDataValid.not()) return

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
        if (isEditJourney) updateCard(cardDto) else saveNewCard(cardDto)
    }

}
