package cypher.hushlet.features.add_credentials.ui.models

data class ErrorData(var hasError: Boolean = false, var errorMessage: Int? = null, var warning: Int? = null)

data class FormFieldState(val value: String = "", val errorData: ErrorData = ErrorData(), val hasError: Boolean = errorData.hasError)