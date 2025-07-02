package cypher.hushlet.features.add_credentials.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cypher.hushlet.R
import cypher.hushlet.features.add_credentials.ui.models.FormFieldState
import cypher.hushlet.features.add_credentials.ui.states.UiState

@Composable
fun AddEditCredentialScreen(modifier: Modifier = Modifier, viewModel: AddEditCredentialsViewModel = hiltViewModel()) {
    val uiState = viewModel.uiStateFlow.collectAsStateWithLifecycle()

    val accountNameState = viewModel.accountNameState.value
    val userNameState = viewModel.userNameState.value
    val passwordState = viewModel.passwordState.value
    val urlState = viewModel.urlState.value
    val notesState = viewModel.notesState.value
    val favouriteState = viewModel.favouriteState.value
    val cardNameState = viewModel.cardNameState.value
    val cardNumberState = viewModel.cardNumberState.value
    val cardHolderNameState = viewModel.cardHolderNameState.value
    val expMonthState = viewModel.expMonthState.value
    val expYearState = viewModel.expYearState.value
    val secCodeState = viewModel.secCodeState.value

    Scaffold(modifier, containerColor = Color.White) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            when (uiState.value) {
                UiState.AddAccountState -> AccountSection(
                    modifier = Modifier,
                    accountNameState = accountNameState,
                    onAccountNameChanged = viewModel::onAccountNameChanged,
                    onUserNameChanged = viewModel::onUserNameChanged,
                    onPasswordChanged = viewModel::onPasswordChanged,
                    onUrlChanged = viewModel::onUrlChanged,
                    onNotesChanged = viewModel::onNotesChanged,
                    userNameState = userNameState,
                    passwordState = passwordState,
                    urlState = urlState,
                    notesState = notesState,
                    favoriteState = favouriteState,
                    onFavoriteChanged = viewModel::onFavouriteChanged,
                    onSave = viewModel::onAccountSubmit
                )

                UiState.AddCardState -> CardSection(
                    modifier = Modifier,
                    cardNameState = cardNameState,
                    cardNumberState = cardNumberState,
                    cardHolderNameState = cardHolderNameState,
                    expiryMonthState = expMonthState,
                    expiryYearState = expYearState,
                    securityCodeState = secCodeState,
                    onCardNameChanged = viewModel::onCardNameChanged,
                    onCardNumberChanged = viewModel::onCardNumberChanged,
                    onCardHolderNameChanged = viewModel::onCardHolderNameChanged,
                    onExpiryMonthChanged = viewModel::onExpMonthChanged,
                    onExpiryYearChanged = viewModel::onExpYearChanged,
                    onSecurityCodeChanged = viewModel::onSecCodeChanged,
                    onNotesChanged = viewModel::onNotesChanged,
                    favoriteState = favouriteState,
                    onFavoriteChanged = viewModel::onFavouriteChanged,
                    notesState = notesState,
                    onSave = viewModel::onCardSubmit
                )

                else -> {
                    Text("Else")
                }
            }
        }
    }
}

@Composable
fun AccountSection(
    modifier: Modifier = Modifier,
    accountNameState: FormFieldState,
    userNameState: FormFieldState,
    passwordState: FormFieldState,
    urlState: FormFieldState,
    notesState: FormFieldState,
    favoriteState: Boolean,
    onAccountNameChanged: (String) -> Unit,
    onUserNameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onUrlChanged: (String) -> Unit,
    onNotesChanged: (String) -> Unit,
    onFavoriteChanged: (Boolean) -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StandardInput(
            labelText = stringResource(R.string.label_account_name),
            hintText = stringResource(R.string.hint_account_name),
            textCapitalization = KeyboardCapitalization.Words,
            state = accountNameState,
            onValueChange = onAccountNameChanged
        )
        StandardInput(
            labelText = stringResource(R.string.label_username),
            hintText = stringResource(R.string.hint_username),
            state = userNameState,
            keyboardType = KeyboardType.Email,
            onValueChange = onUserNameChanged
        )
        StandardInput(
            labelText = stringResource(R.string.label_password),
            hintText = stringResource(R.string.hint_password),
            state = passwordState,
            keyboardType = KeyboardType.Password,
            onValueChange = onPasswordChanged
        )
        StandardInput(
            labelText = stringResource(R.string.label_account_url),
            hintText = stringResource(R.string.hint_account_url),
            state = urlState,
            keyboardType = KeyboardType.Uri,
            onValueChange = onUrlChanged
        )
        StandardInput(
            labelText = stringResource(R.string.label_account_notes),
            hintText = stringResource(R.string.hint_account_notes),
            textCapitalization = KeyboardCapitalization.Sentences,
            keyboardImeAction = ImeAction.Done,
            state = notesState,
            onValueChange = onNotesChanged
        )
        FavoriteToggle(isChecked = favoriteState, onCheckChanged = onFavoriteChanged)
        PrimaryButton(text = stringResource(R.string.save_account), onClick = onSave)
    }
    //favrt
}

@Composable
fun CardSection(
    modifier: Modifier = Modifier,
    cardNameState: FormFieldState,
    cardNumberState: FormFieldState,
    cardHolderNameState: FormFieldState,
    expiryMonthState: FormFieldState,
    expiryYearState: FormFieldState,
    securityCodeState: FormFieldState,
    favoriteState: Boolean,
    onCardNameChanged: (String) -> Unit,
    onCardNumberChanged: (String) -> Unit,
    onCardHolderNameChanged: (String) -> Unit,
    onExpiryMonthChanged: (String) -> Unit,
    onExpiryYearChanged: (String) -> Unit,
    onSecurityCodeChanged: (String) -> Unit,
    onNotesChanged: (String) -> Unit,
    onFavoriteChanged: (Boolean) -> Unit,
    notesState: FormFieldState, onSave: () -> Unit
) {

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StandardInput(
            labelText = stringResource(R.string.label_card_name),
            hintText = stringResource(R.string.hint_card_name),
            state = cardNameState,
            textCapitalization = KeyboardCapitalization.Words,
            onValueChange = onCardNameChanged
        )
        StandardInput(
            labelText = stringResource(R.string.label_card_number),
            hintText = stringResource(R.string.hint_card_number),
            state = cardNumberState,
            keyboardType = KeyboardType.Number,
            onValueChange = onCardNumberChanged
        )
        StandardInput(
            labelText = stringResource(R.string.label_card_holder_name),
            hintText = stringResource(R.string.hint_card_holder_name),
            textCapitalization = KeyboardCapitalization.Words,
            state = cardHolderNameState,
            onValueChange = onCardHolderNameChanged
        )
        StandardInput(
            labelText = stringResource(R.string.label_expiry_month),
            hintText = stringResource(R.string.hint_expiry_month),
            state = expiryMonthState,
            keyboardType = KeyboardType.Number,
            onValueChange = onExpiryMonthChanged
        )
        StandardInput(
            labelText = stringResource(R.string.label_expiry_year),
            hintText = stringResource(R.string.hint_expiry_year),
            state = expiryYearState,
            keyboardType = KeyboardType.Number,
            onValueChange = onExpiryYearChanged
        )
        StandardInput(
            labelText = stringResource(R.string.label_security_code),
            hintText = stringResource(R.string.hint_security_code),
            state = securityCodeState,
            keyboardType = KeyboardType.NumberPassword,
            onValueChange = onSecurityCodeChanged
        )
        StandardInput(
            labelText = stringResource(R.string.label_account_notes),
            hintText = stringResource(R.string.hint_account_notes),
            state = notesState,
            textCapitalization = KeyboardCapitalization.Sentences,
            keyboardImeAction = ImeAction.Done,
            onValueChange = onNotesChanged
        )
        FavoriteToggle(isChecked = favoriteState, onCheckChanged = onFavoriteChanged)
        PrimaryButton(text = stringResource(R.string.save_account), onClick = onSave)
    }
}

@Composable
fun StandardInput(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 0.dp, vertical = 8.dp),
    labelText: String,
    hintText: String,
    state: FormFieldState,
    maxLines: Int = 1,
    enabled: Boolean = true,
    isPasswordInput: Boolean = false,
    textCapitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    keyboardImeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardAction: KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit,
) {
    TextField(
        modifier = modifier,
        value = state.value,
        keyboardActions = keyboardAction,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            capitalization = textCapitalization,
            imeAction = keyboardImeAction
        ),
        maxLines = maxLines,
        onValueChange = onValueChange,
        isError = state.hasError,
        label = { Text(labelText) },
        enabled = enabled,
        placeholder = { Text(hintText) },
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

@Composable
fun PrimaryButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Button(modifier = modifier, onClick = onClick) {
        Text(text)
    }
}

@Composable
fun FavoriteToggle(modifier: Modifier = Modifier, isChecked: Boolean, onCheckChanged: (Boolean) -> Unit) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(modifier = modifier, checked = isChecked, onCheckedChange = onCheckChanged)
        Text("Favorite")
    }
}

@Preview
@Composable
private fun StandardInputPreview() {
    Box(
        modifier = Modifier
            .padding(all = 16.dp)
            .background(color = Color.White)
    ) {
        StandardInput(
            modifier = Modifier,
            labelText = "Username",
            hintText = "Enter Username",
            state = FormFieldState(value = "@pnmhatre"),
            isPasswordInput = false
        ) {

        }

    }
}

@Preview
@Composable
private fun PrimaryButtonPreview() {
    PrimaryButton(text = "Save Account") { }
}

/*
@Preview
@Composable
private fun AccountSectionPreview() {
    val state = rememberSaveable { mutableStateOf("") } to ErrorState.NoErrors
    AccountSection(
        modifier = Modifier.background(Color.White),
        accountNameState = state,
        userNameState = state,
        passwordState = state,
        urlState = state, notesState = state
    )
}*/
