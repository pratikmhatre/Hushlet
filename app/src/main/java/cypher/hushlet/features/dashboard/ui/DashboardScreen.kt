package cypher.hushlet.features.dashboard.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cypher.hushlet.core.domain.models.AccountListItemDto
import cypher.hushlet.core.domain.models.CardListItemDto
import cypher.hushlet.features.dashboard.domain.models.DashboardContent
import cypher.hushlet.features.dashboard.ui.events.DashboardUiState

@Composable
fun DashboardScreen(modifier: Modifier = Modifier, viewModel: DashboardViewModel = hiltViewModel()) {
    val uiState = viewModel.dashboardUiState.collectAsStateWithLifecycle()

    Scaffold { innerPadding ->
        if (uiState.value is DashboardUiState.DashboardDataState) {
            Column(modifier = Modifier.padding(innerPadding)) {
                CardList(content = (uiState.value as DashboardUiState.DashboardDataState).dashboardData.cardSectionContent)
                AccountList(content = (uiState.value as DashboardUiState.DashboardDataState).dashboardData.accountSectionContent)
            }
        }
    }
}

@Composable
fun CardList(modifier: Modifier = Modifier, content: DashboardContent<CardListItemDto>) {
    if ((content !is DashboardContent.FavoriteContent)) return
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(content.data) {
            CardItem(cardName = it.cardName, cardHolderName = it.cardHolderName) { }
        }
    }
}

@Composable
fun AccountList(modifier: Modifier = Modifier, content: DashboardContent<AccountListItemDto>) {
    if ((content !is DashboardContent.FavoriteContent)) return
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items((content).data) {
            AccountItem(accountName = it.accountName, userName = it.url ?: "username") { }
        }
    }
}


@Composable
fun AccountItem(modifier: Modifier = Modifier, accountName: String, userName: String, onAccountSelected: () -> Unit) {
    Text(text = "$accountName - $userName", modifier = modifier.clickable { onAccountSelected() })
}

@Composable
fun CardItem(modifier: Modifier = Modifier, cardName: String, cardHolderName: String, onCardSelected: () -> Unit) {
    Text(text = "$cardName - $cardHolderName", modifier = modifier.clickable { onCardSelected() })
}