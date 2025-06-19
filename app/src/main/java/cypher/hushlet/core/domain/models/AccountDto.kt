package cypher.hushlet.core.domain.models

import cypher.hushlet.core.data.datasources.local.db.accounts.AccountEntity

data class AccountDto(
    val id: Long,
    val title: String,
    val username: String?,
    val password: String,
    val url: String?,
    val notes: String?,
    val isFavourite: Boolean,
    val isArchived: Boolean,
    val createdAt: Long,
    val updatedAt: Long
) {
    fun toEntity(): AccountEntity {
        return AccountEntity(
            id,
            title,
            username,
            password,
            url,
            notes,
            isFavourite,
            isArchived,
            createdAt,
            updatedAt
        )
    }
}