package cypher.hushlet.core.data.datasources.local.db.cards

import androidx.room.Entity
import androidx.room.PrimaryKey
import cypher.hushlet.core.utils.AppConstants

@Entity(tableName = AppConstants.CARD_TABLE)
data class CardsTable(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    var cardNumber: String,
    var expiryMonth: String?,
    var expiryYear: String?,
    var securityCode: Int?,
    var cardName: String,
    var cardType: String?,
    var cardHolderName: String?,
    var notes: String = "",
    var isFavourite: Boolean = false,
    var isArchived: Boolean = false,
    val createdAt: Long,
    var updatedAt: Long
)