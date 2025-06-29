package cypher.hushlet.core.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import cypher.hushlet.core.data.datasources.local.db.HushletDb
import cypher.hushlet.core.data.datasources.local.db.accounts.AccountsDao
import cypher.hushlet.core.data.datasources.local.db.cards.CardsDao
import cypher.hushlet.core.data.repositories.AccountsRepositoryImpl
import cypher.hushlet.core.data.repositories.CardsRepositoryImpl
import cypher.hushlet.core.domain.repositories.AccountsRepository
import cypher.hushlet.core.domain.repositories.CardsRepository
import cypher.hushlet.core.utils.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideCardsRepository(repositoryImpl: CardsRepositoryImpl): CardsRepository = repositoryImpl

    @Singleton
    @Provides
    fun provideAccountsRepository(repositoryImpl: AccountsRepositoryImpl): AccountsRepository = repositoryImpl

    @Singleton
    @Provides
    fun provideHushletDb(@ApplicationContext context: Context): HushletDb = Room.databaseBuilder(
        context, HushletDb::class.java,
        AppConstants.HUSHLET_DB_NAME
    ).build()

    @Singleton
    @Provides
    fun provideAccountsDao(database : HushletDb): AccountsDao {
        return database.getAccountsDao()
    }
    @Singleton
    @Provides
    fun provideCardsDao(database : HushletDb): CardsDao {
        return database.getCardsDao()
    }
}