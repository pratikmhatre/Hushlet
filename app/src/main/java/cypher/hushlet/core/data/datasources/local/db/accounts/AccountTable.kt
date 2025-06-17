package cypher.hushlet.core.data.datasources.local.db.accounts

import androidx.room.Entity
import androidx.room.PrimaryKey
import cypher.hushlet.core.utils.AppConstants

@Entity(tableName = AppConstants.ACCOUNT_TABLE)
data class AccountTable(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val username: String?,
    val password: String,
    val url: String?,
    val notes: String?,
    val isFavourite: Boolean = false,
    val isArchived: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long,
)