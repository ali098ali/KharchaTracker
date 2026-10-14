package com.kharcha.tracker.data.local.dao

import androidx.room.*
import com.kharcha.tracker.data.local.entity.RecurringEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecurringDao {
    @Query("SELECT * FROM recurring WHERE isActive = 1")
    fun getActive(): Flow<List<RecurringEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(r: RecurringEntity): Long

    @Update suspend fun update(r: RecurringEntity)
    @Delete suspend fun delete(r: RecurringEntity)
}
