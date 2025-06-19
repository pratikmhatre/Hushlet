package cypher.hushlet.features.dashboard.domain.models

import cypher.hushlet.core.domain.models.AccountListItemDto
import cypher.hushlet.core.domain.models.CardListItemDto

data class DashboardData(
    val isCardsAvailable: Boolean,
    val isFavoriteCardsAvailable: Boolean,
    val cardsList: List<CardListItemDto>,
    val isAccountsAvailable: Boolean,
    val isFavoriteAccountsAvailable: Boolean,
    val accountsList: List<AccountListItemDto>
)