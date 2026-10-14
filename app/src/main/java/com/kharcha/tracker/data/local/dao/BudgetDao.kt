package com.kharcha.tracker.data.local.dao

import androidx.room.*
import com.kharcha.tracker.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgets WHERE year = :year AND month = :month")
    fun getForMonth(year: Int, month: Int): Flow<List<BudgetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(b: BudgetEntity): Long

    @Update suspend fun update(b: BudgetEntity)
    @Delete suspend fun delete(b: BudgetEntity)

    @Query("UPDATE budgets SET spent = :spent WHERE id = :id")
    suspend fun updateSpent(id: Long, spent: Double)
}
