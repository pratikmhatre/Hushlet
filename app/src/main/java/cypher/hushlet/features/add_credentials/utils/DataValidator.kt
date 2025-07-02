package cypher.hushlet.features.add_credentials.utils

import cypher.hushlet.R
import cypher.hushlet.features.add_credentials.domain.usecases.CheckIfAccountNameExists
import cypher.hushlet.features.add_credentials.domain.usecases.CheckIfCardNameExists
import cypher.hushlet.features.add_credentials.ui.models.ErrorData

object DataValidator {

    fun validateCardNumber(cardNumber: String): ErrorData {
        return if (cardNumber.trim().isBlank()) {
            ErrorData(true, errorMessage = R.string.error_card_number_empty)
        } else {
            ErrorData(false)
        }
    }

    suspend fun validateCardName(
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

    suspend fun validateAccountName(
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

    fun validateUserName(userName: String): ErrorData {
        return if (userName.trim().isBlank()) {
            ErrorData(true, R.string.error_username_empty)
        } else if (userName.trim().length < 6) {
            ErrorData(false, warning = R.string.warning_username_short)
        } else {
            ErrorData(false)
        }
    }

    fun validatePassword(password: String): ErrorData {
        return if (password.trim().isBlank()) {
            ErrorData(true, R.string.error_password_empty)
        } else if (password.trim().length < 4) {
            ErrorData(false, warning = R.string.warning_password_short)
        } else {
            ErrorData(false)
        }
    }
}