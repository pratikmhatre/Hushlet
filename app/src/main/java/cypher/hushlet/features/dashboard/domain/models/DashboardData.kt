package cypher.hushlet.features.dashboard.domain.models

import cypher.hushlet.core.domain.models.AccountListItemDto
import cypher.hushlet.core.domain.models.CardListItemDto

data class DashboardData(
    val isFavoriteCardsEmpty: Boolean, val cardsList: List<CardListItemDto>,
    val isFavoriteAccountsEmpty: Boolean, val accountsList: List<AccountListItemDto>
)