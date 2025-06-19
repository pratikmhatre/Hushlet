package cypher.hushlet.core.domain.models

import cypher.hushlet.core.data.datasources.local.db.cards.CardEntity

data class CardDto(
    val id: Long,
    var cardNumber: String,
    var expiryMonth: String?,
    var expiryYear: String?,
    var securityCode: Int?,
    var cardName: String,
    var cardType: String?,
    var cardHolderName: String?,
    var notes: String,
    var isFavourite: Boolean,
    var isArchived: Boolean,
    val createdAt: Long,
    var updatedAt: Long
) {
    fun toEntity(): CardEntity {
        return CardEntity(
            id,
            cardNumber,
            expiryMonth,
            expiryYear,
            securityCode,
            cardName,
            cardType,
            cardHolderName,
            notes,
            isFavourite,
            isArchived,
            createdAt,
            updatedAt
        )
    }
}