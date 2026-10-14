package com.kharcha.tracker.data.local.dao

import androidx.room.*
import com.kharcha.tracker.data.local.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts ORDER BY isDefault DESC, name ASC")
    fun getAll(): Flow<List<AccountEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(a: AccountEntity): Long

    @Update suspend fun update(a: AccountEntity)
}
