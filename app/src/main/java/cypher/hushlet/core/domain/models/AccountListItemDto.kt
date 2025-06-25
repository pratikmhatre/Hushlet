package cypher.hushlet.core.domain.models

data class AccountListItemDto(
    val id: Long,
    val accountName: String,
    val url: String?,
    val isFavourite: Boolean,
    val updatedAt: Long
)