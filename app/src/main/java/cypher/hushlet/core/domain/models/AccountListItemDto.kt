package cypher.hushlet.core.domain.models

data class AccountListItemDto(
    val id: Long, val title: String,
    val url: String?, val isFavourite: Boolean
)