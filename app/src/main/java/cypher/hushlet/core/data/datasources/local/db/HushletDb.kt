package cypher.hushlet.features.dashboard.data.datasources.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cypher.hushlet.features.dashboard.data.datasources.local.db.cards.CardsDao
import cypher.hushlet.features.dashboard.data.datasources.local.db.cards.CardsTable
import cypher.hushlet.features.dashboard.data.datasources.local.db.credentials.CredentialsDao
import cypher.hushlet.features.dashboard.data.datasources.local.db.credentials.CredentialsTable
import cypher.hushlet.core.utils.AppConstants

@Database(
    entities = [CardsTable::class, CredentialsTable::class],
    version = AppConstants.HUSHLET_DB_VERSION,
    exportSchema = false
)
abstract class HushletDb : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: HushletDb? = null

        fun getDatabase(context: Context): HushletDb {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    HushletDb::class.java,
                    name = AppConstants.HUSHLET_DB_NAME,
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
    abstract fun getCardsDao(): CardsDao
    abstract fun getCredentialsDao(): CredentialsDao
}