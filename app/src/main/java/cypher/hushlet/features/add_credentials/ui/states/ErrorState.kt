package cypher.hushlet.features.add_credentials.ui.states

data class ErrorData(var hasError: Boolean = false, var errorMessage: Int? = null, var warning: Int? = null)

sealed class ErrorState(val hasError: Boolean) {
    object NoErrors : ErrorState(false)
    data class CardDataErrors(
        val cardName: ErrorData? = null,
        val cardNumber: ErrorData? = null,
        val expiryMonth: ErrorData? = null,
        val expiryYear: ErrorData? = null,
        val cardHolderName: ErrorData? = null
    ) : ErrorState(hasError = cardNumber?.hasError == true || expiryMonth?.hasError == true || expiryYear?.hasError == true || cardName?.hasError == true || cardHolderName?.hasError == true)

    data class AccountDataErrors(
        val accountName: ErrorData? = null,
        val username: ErrorData? = null,
        val password: ErrorData? = null,
        val url: ErrorData? = null
    ) : ErrorState(
        hasError = accountName?.hasError == true || username?.hasError == true || password?.hasError == true || url?.hasError == true
    )
}
