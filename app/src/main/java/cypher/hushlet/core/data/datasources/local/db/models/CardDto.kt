package cypher.hushlet.core.data.datasources.local.db.models

data class CardDto(
    val id: Long,
    val cardNumber: String,
    val cardName: String,
    val cardType: String?,
    val cardHolderName: String,
    val isFavourite: Boolean,
    val updatedAt: Long
)