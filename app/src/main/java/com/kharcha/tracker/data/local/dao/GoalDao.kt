package com.kharcha.tracker.data.local.dao

import androidx.room.*
import com.kharcha.tracker.data.local.entity.GoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals ORDER BY isCompleted ASC, id DESC")
    fun getAll(): Flow<List<GoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(g: GoalEntity): Long

    @Update suspend fun update(g: GoalEntity)
    @Delete suspend fun delete(g: GoalEntity)
}
