package cypher.hushlet.core.domain.models

data class CardListItemDto(
    val id: Long,
    val cardNumber: String,
    val cardName: String,
    val cardType: String?,
    val cardHolderName: String,
    val isFavourite: Boolean,
    val isArchived: Boolean,
    val updatedAt: Long
)