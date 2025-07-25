package cypher.hushlet.features.dashboard.ui

import android.content.ClipData
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cypher.hushlet.R
import cypher.hushlet.core.domain.models.AccountListItemDto
import cypher.hushlet.core.theme.spacing
import cypher.hushlet.features.add_credentials.ui.AddEditCredentialScreen
import cypher.hushlet.features.dashboard.domain.models.AccountsListItem
import cypher.hushlet.features.dashboard.ui.events.DetailsBottomSheetState
import cypher.hushlet.features.dashboard.ui.events.DashboardUiState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(modifier: Modifier = Modifier, viewModel: DashboardViewModel = hiltViewModel()) {
    val uiState = viewModel.dashboardUiState.collectAsStateWithLifecycle()
    val bottomSheetState = viewModel.detailsBottomSheetState.collectAsStateWithLifecycle(null)
    val accountBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(onAddButtonClicked = { viewModel.onAddAccountClicked() }) },
        containerColor = MaterialTheme.colorScheme.primary,
        bottomBar = {
            SearchBar()
        }) { innerPadding ->
        ScaffoldBody(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState.value,
            onAccountSelected = { viewModel.onAccountSelected(it) })

        LaunchedEffect(bottomSheetState.value) {
            if (bottomSheetState.value is DetailsBottomSheetState.EditAccountDetailsBottomSheetState || bottomSheetState.value is DetailsBottomSheetState.AddAccountDetailsBottomSheetState) {
                coroutineScope.launch { if (bottomSheetState.value!!.shouldShow) accountBottomSheetState.show() else accountBottomSheetState.hide() }
            }
        }

        if (accountBottomSheetState.isVisible) {
            ModalBottomSheet(onDismissRequest = {
                coroutineScope.launch {
                    accountBottomSheetState.hide()
                }
            }) {

                when (bottomSheetState.value!!) {
                    is DetailsBottomSheetState.AddAccountDetailsBottomSheetState -> AddEditCredentialScreen(
                        accountId = null,
                        dismissDialog = { coroutineScope.launch { accountBottomSheetState.hide() } }
                    )

                    else -> AddEditCredentialScreen(
                        accountId = (bottomSheetState.value as DetailsBottomSheetState.EditAccountDetailsBottomSheetState).accountDto?.id,
                        dismissDialog = { coroutineScope.launch { accountBottomSheetState.hide() } }
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(modifier: Modifier = Modifier, onAddButtonClicked: () -> Unit) {
    TopAppBar(
        title = { Text("Hushlet", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
        modifier = modifier.fillMaxWidth(),
        navigationIcon = {
            Icon(
                modifier = Modifier.padding(start = MaterialTheme.spacing.large),
                imageVector = Icons.Default.Menu,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }, actions = {
            IconButton(onClick = onAddButtonClicked) {
                Icon(
                    modifier = Modifier.padding(end = MaterialTheme.spacing.large),
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        })
}

@Preview
@Composable
private fun TopAppBarPreview() {
    TopAppBar(onAddButtonClicked = {})
}

@Composable
private fun ScaffoldBody(modifier: Modifier, uiState: DashboardUiState, onAccountSelected: (Long) -> Unit) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(
                    bottomEnd = MaterialTheme.spacing.large,
                    bottomStart = MaterialTheme.spacing.large
                )
            )
            .clip(
                RoundedCornerShape(
                    bottomEnd = MaterialTheme.spacing.large,
                    bottomStart = MaterialTheme.spacing.large
                )
            ),
    ) {
        if (uiState is DashboardUiState.AccountDataState) {
            AccountList(
                modifier = Modifier,
                accountsList = uiState.accountList, onAccountSelected = onAccountSelected
            )
        } else {
            Text("Empty State")
        }


    }
}


@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(horizontal = MaterialTheme.spacing.large), contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.Search,
                contentDescription = "Search Icon",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextField(
                modifier = Modifier,
                value = "",
                onValueChange = {},
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        stringResource(R.string.search_placeholder),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
            )

        }
    }
}


@Composable
fun AccountList(
    modifier: Modifier = Modifier,
    accountsList: List<AccountsListItem>,
    onAccountSelected: (Long) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        items((accountsList)) { item ->

            if (item is AccountsListItem.SectionHeader) {
                SectionHeader(title = item.title)
            } else {
                val accData = (item as AccountsListItem.AccountData).data
                AccountItem(accountData = accData, onAccountSelected = onAccountSelected)
            }
        }
    }
}

@Composable
fun AccountItem(
    modifier: Modifier = Modifier,
    accountData: AccountListItemDto,
    onAccountSelected: (Long) -> Unit
) {
    val clipboardManager = LocalClipboard.current
    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onAccountSelected(accountData.id)
            }
            .padding(vertical = MaterialTheme.spacing.medium, horizontal = MaterialTheme.spacing.large),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
                .size(50.dp), contentAlignment = Alignment.Center
        ) {
            if (true) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(R.drawable.ic_drive),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = ""
                )
            } else {
                Text(
                    text = accountData.accountName.first().toString().uppercase(),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.spacing.large)
                .align(Alignment.CenterVertically)
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = accountData.accountName,
                modifier = Modifier,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp, fontWeight = FontWeight.Bold
            )
            Text(
                text = accountData.url ?: "placeholder@place.com",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Normal
            )
        }
        IconButton(onClick = {
            copyPassword(password = accountData.accountName, context = context, clipboardManager = clipboardManager)
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_copy),
                tint = MaterialTheme.colorScheme.inverseOnSurface,
                contentDescription = ""
            )
        }
    }
}


@Composable
fun SectionHeader(modifier: Modifier = Modifier, title: String) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.xLarge)
            .padding(top = MaterialTheme.spacing.xLarge, bottom = MaterialTheme.spacing.small),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(title, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }
}

private fun copyPassword(clipboardManager: Clipboard, context: Context?, password: String) {
    clipboardManager.nativeClipboard.setPrimaryClip(ClipData.newPlainText("password", password))
    context?.run { Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show() }
}

