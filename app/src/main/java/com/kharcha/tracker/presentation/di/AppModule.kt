package com.kharcha.tracker.presentation.di

import android.content.Context
import androidx.room.Room
import com.kharcha.tracker.data.local.dao.*
import com.kharcha.tracker.data.local.database.AppDatabase
import com.kharcha.tracker.data.repository.*
import com.kharcha.tracker.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun provideDb(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "kharcha_v2.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideTxDao(db: AppDatabase) = db.transactionDao()
    @Provides fun provideBudgetDao(db: AppDatabase) = db.budgetDao()
    @Provides fun provideGoalDao(db: AppDatabase) = db.goalDao()
    @Provides fun provideAccountDao(db: AppDatabase) = db.accountDao()
    @Provides fun provideRecurringDao(db: AppDatabase) = db.recurringDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton abstract fun bindTx(impl: TransactionRepositoryImpl): TransactionRepository
    @Binds @Singleton abstract fun bindBudget(impl: BudgetRepositoryImpl): BudgetRepository
    @Binds @Singleton abstract fun bindGoal(impl: GoalRepositoryImpl): GoalRepository
    @Binds @Singleton abstract fun bindAccount(impl: AccountRepositoryImpl): AccountRepository
    @Binds @Singleton abstract fun bindRecurring(impl: RecurringRepositoryImpl): RecurringRepository
}
