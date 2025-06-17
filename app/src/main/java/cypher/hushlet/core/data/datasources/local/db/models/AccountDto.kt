package cypher.hushlet.core.data.datasources.local.db.models

data class AccountDto(
    val id: Long, val title: String,
    val url: String?, val isFavourite: Boolean
)