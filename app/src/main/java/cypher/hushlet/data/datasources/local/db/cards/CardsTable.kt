package cypher.hushlet.data.datasources.local.db.cards

import androidx.room.Entity
import androidx.room.PrimaryKey
import cypher.hushlet.utils.AppConstants

@Entity(tableName = AppConstants.CARDS_TABLE)
data class CardsTable(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val cardNumber: Int,
    val expiryMonth: Int,
    val expiryYear: Int,
    val securityCode: Int,
    val cardName: String,
    val cardType:String,
    val cardHolderName: String,
    val notes: String,
    val isFavourite: Boolean = false,
    val isArchived: Boolean = false,
    val createdAt: Int,
    val updatedAt: Int
)