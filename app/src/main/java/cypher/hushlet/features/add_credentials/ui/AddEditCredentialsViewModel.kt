package cypher.hushlet.features.add_credentials.ui

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
import cypher.hushlet.features.add_credentials.ui.states.ErrorState
import cypher.hushlet.features.add_credentials.ui.states.UiState
import cypher.hushlet.features.add_credentials.utils.DataValidator.validateAccountData
import cypher.hushlet.features.add_credentials.utils.DataValidator.validateCardData
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

    private var _errorState = MutableStateFlow<ErrorState>(ErrorState.NoErrors)
    val errorState = _errorState.asStateFlow()

    private var _uiStateFlow = MutableStateFlow<UiState>(UiState.AddCardState)
    val uiStateFlow = _uiStateFlow.asStateFlow()

    init {
        if (isEditJourney) {
            viewModelScope.launch {
                if (isAccount) accountDto = getAccountDetails(cardDto!!.id) else cardDto =
                    getCardDetails(accountDto!!.id)
            }
        } else {
            _uiStateFlow.value =
                if (isAccount) UiState.AddAccountState(generatedPassword = "") else UiState.AddCardState
        }
    }

    fun onDeleteConfirmed() {
        viewModelScope.launch {
            if (isAccount) deleteAccount(accountDto!!) else deleteCard(cardDto!!)
        }
    }

    fun saveAccountData(
        title: String, userName: String, password: String, isFavourite: Boolean, url: String?, notes: String = ""
    ) {
        viewModelScope.launch {
            val dataValidation = validateAccountData(title, userName, password, checkIfAccountNameExists)
            if (dataValidation.first) {
                _errorState.value = dataValidation.second
                return@launch
            }

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
    }

    fun saveCardData(
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
        viewModelScope.launch {

            val dataValidation = validateCardData(cardName, cardNumber, checkIfCardNameExists)
            if (dataValidation.first) {
                _errorState.value = dataValidation.second
                return@launch
            }

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

}
