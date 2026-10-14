package com.kharcha.tracker.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kharcha.tracker.data.local.converter.Converters
import com.kharcha.tracker.data.local.dao.*
import com.kharcha.tracker.data.local.entity.*

@Database(
    entities = [
        TransactionEntity::class,
        BudgetEntity::class,
        GoalEntity::class,
        AccountEntity::class,
        RecurringEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun goalDao(): GoalDao
    abstract fun accountDao(): AccountDao
    abstract fun recurringDao(): RecurringDao
}
