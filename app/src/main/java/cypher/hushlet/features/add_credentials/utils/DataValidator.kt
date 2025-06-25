package cypher.hushlet.features.add_credentials.utils

import cypher.hushlet.R
import cypher.hushlet.features.add_credentials.domain.usecases.CheckIfAccountNameExists
import cypher.hushlet.features.add_credentials.domain.usecases.CheckIfCardNameExists
import cypher.hushlet.features.add_credentials.ui.states.ErrorData
import cypher.hushlet.features.add_credentials.ui.states.ErrorState

object DataValidator {
    suspend fun validateAccountData(
        title: String, userName: String, password: String, checkAccountNameExists: CheckIfAccountNameExists
    ): Pair<Boolean, ErrorState> {
        val titleValidation = validateAccountName(title, checkAccountNameExists)
        val userNameValidation = validateUserName(userName)
        val passwordValidation = validatePassword(password)
        return Pair(
            listOf(titleValidation, userNameValidation, passwordValidation).any { it.hasError },
            ErrorState.AccountDataErrors(
                accountName = titleValidation, username = userNameValidation, password = passwordValidation
            )
        )
    }

    suspend fun validateCardData(
        cardName: String, cardNumber: String, checkAccountNameExists: CheckIfCardNameExists
    ): Pair<Boolean, ErrorState> {
        val cardNameValidation = validateCardName(cardName, checkAccountNameExists)
        val cardNumberValidation = validateCardNumber(cardNumber)
        return Pair(
            listOf(cardNameValidation, cardNumberValidation).any { it.hasError }, ErrorState.CardDataErrors(
                cardName = cardNameValidation, cardNumber = cardNumberValidation
            )
        )
    }

    private fun validateCardNumber(cardNumber: String): ErrorData {
        return if (cardNumber.trim().isBlank()) {
            ErrorData(true, errorMessage = R.string.error_card_number_empty)
        } else {
            ErrorData(false)
        }
    }

    private suspend fun validateCardName(
        title: String,
        checkAccountNameExists: CheckIfCardNameExists
    ): ErrorData {
        return if (title.trim().isBlank()) {
            ErrorData(true, errorMessage = R.string.error_account_name_empty)
        } else if (checkAccountNameExists(title)) {
            ErrorData(true, errorMessage = R.string.error_account_name_taken)
        } else {
            ErrorData(false)
        }
    }

    private suspend fun validateAccountName(
        title: String,
        checkAccountNameExists: CheckIfAccountNameExists
    ): ErrorData {
        return if (title.trim().isBlank()) {
            ErrorData(true, errorMessage = R.string.error_account_name_empty)
        } else if (checkAccountNameExists(title)) {
            ErrorData(true, errorMessage = R.string.error_account_name_taken)
        } else {
            ErrorData(false)
        }
    }

    private fun validateUserName(userName: String): ErrorData {
        return if (userName.trim().isBlank()) {
            ErrorData(true, R.string.error_username_empty)
        } else if (userName.trim().length < 6) {
            ErrorData(false, warning = R.string.warning_username_short)
        } else {
            ErrorData(false)
        }
    }

    private fun validatePassword(password: String): ErrorData {
        return if (password.trim().isBlank()) {
            ErrorData(true, R.string.error_password_empty)
        } else if (password.trim().length < 4) {
            ErrorData(false, warning = R.string.warning_password_short)
        } else {
            ErrorData(false)
        }
    }
}