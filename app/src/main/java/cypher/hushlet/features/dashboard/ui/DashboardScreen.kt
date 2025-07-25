package cypher.hushlet.features.dashboard.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cypher.hushlet.R
import cypher.hushlet.core.domain.models.AccountListItemDto
import cypher.hushlet.core.domain.models.CardListItemDto
import cypher.hushlet.core.theme.HushletTheme
import cypher.hushlet.core.theme.spacing
import cypher.hushlet.features.dashboard.domain.models.AccountsListItem
import cypher.hushlet.features.dashboard.domain.models.DashboardContent
import cypher.hushlet.features.dashboard.ui.events.DashboardUiState

@Composable
fun DashboardScreen(modifier: Modifier = Modifier, viewModel: DashboardViewModel = hiltViewModel()) {
    val uiState = viewModel.dashboardUiState.collectAsStateWithLifecycle()

    Scaffold(modifier = modifier, containerColor = MaterialTheme.colorScheme.primary, bottomBar = {
        SearchBar()
    }) { innerPadding ->
        ScaffoldBody(modifier = Modifier.padding(innerPadding), uiState = uiState.value)
    }
}

@Composable
private fun ScaffoldBody(modifier: Modifier, uiState: DashboardUiState) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 70.dp)
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(
                        bottomEnd = MaterialTheme.spacing.large,
                        bottomStart = MaterialTheme.spacing.large
                    )
                )
                .align(Alignment.TopCenter)
                .clip(
                    RoundedCornerShape(
                        bottomEnd = MaterialTheme.spacing.large,
                        bottomStart = MaterialTheme.spacing.large
                    )
                )
        ) {
            if (uiState is DashboardUiState.AccountDataState) {
                AccountList(
                    modifier = Modifier,
                    accountsList = uiState.accountList
                )
            } else {
                Text("Empty State")
            }
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
fun AccountList(modifier: Modifier = Modifier, accountsList: List<AccountsListItem>) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        items((accountsList)) { item ->
            if (item is AccountsListItem.SectionHeader) {
                SectionHeader(title = item.title)
            } else {
                AccountItem(accountData = (item as AccountsListItem.AccountData).data, onAccountSelected = {})
            }
        }
    }
}

@Composable
fun AccountItem(
    modifier: Modifier = Modifier,
    accountData: AccountListItemDto,
    onAccountSelected: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.spacing.medium, horizontal = MaterialTheme.spacing.large),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
                .size(50.dp), contentAlignment = Alignment.Center
        ) {
            if (false) {
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
            copyPassword(accountData.accountName)
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

private fun copyPassword(password: String) {

}

