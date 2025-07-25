package cypher.hushlet.features.dashboard.domain.models

import cypher.hushlet.core.domain.models.AccountListItemDto

sealed class AccountsListItem {
    data class SectionHeader(val title: String) : AccountsListItem()
    data class AccountData(
        val data: AccountListItemDto
    ) : AccountsListItem()
}