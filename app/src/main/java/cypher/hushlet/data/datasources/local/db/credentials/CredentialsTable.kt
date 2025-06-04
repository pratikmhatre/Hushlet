package cypher.hushlet.data.datasources.local.db.credentials

import androidx.room.Entity
import androidx.room.PrimaryKey
import cypher.hushlet.utils.AppConstants

@Entity(tableName = AppConstants.CREDENTIALS_TABLE)
data class CredentialsTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val username: String,
    val password: String,
    val url: String,
    val notes: String,
    val isFavourite: Boolean = false,
    val isArchived: Boolean = false,
    val createdAt: Int,
    val updatedAt: String,
)