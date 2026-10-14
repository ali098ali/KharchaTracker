package com.kharcha.tracker.data.local.dao

import androidx.room.*
import com.kharcha.tracker.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY dateMillis DESC, createdAt DESC")
    fun getAll(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE dateMillis >= :start AND dateMillis < :end ORDER BY dateMillis DESC")
    fun getBetween(start: Long, end: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE note LIKE '%' || :q || '%' OR category LIKE '%' || :q || '%' ORDER BY dateMillis DESC")
    fun search(q: String): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions 
        WHERE (:type IS NULL OR type = :type)
          AND (:category IS NULL OR category = :category)
          AND (:from IS NULL OR dateMillis >= :from)
          AND (:to IS NULL OR dateMillis < :to)
        ORDER BY dateMillis DESC
    """)
    fun filtered(type: String?, category: String?, from: Long?, to: Long?): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getById(id: Long): TransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(e: TransactionEntity): Long

    @Update suspend fun update(e: TransactionEntity)
    @Delete suspend fun delete(e: TransactionEntity)

    @Query("SELECT COALESCE(SUM(amount),0) FROM transactions WHERE type = :type AND dateMillis >= :start AND dateMillis < :end")
    suspend fun sumByType(type: String, start: Long, end: Long): Double

    @Query("""
        SELECT category, SUM(amount) as total FROM transactions 
        WHERE type = :type AND dateMillis >= :start AND dateMillis < :end
        GROUP BY category ORDER BY total DESC
    """)
    suspend fun categorySums(type: String, start: Long, end: Long): List<CatSum>

    @Query("""
        SELECT CAST(strftime('%d', dateMillis/1000, 'unixepoch') AS INTEGER) as day, SUM(amount) as total
        FROM transactions WHERE type = 'EXPENSE' AND dateMillis >= :start AND dateMillis < :end
        GROUP BY day ORDER BY day
    """)
    suspend fun dailySums(start: Long, end: Long): List<DaySum>
}

data class CatSum(val category: String, val total: Double)
data class DaySum(val day: Int, val total: Double)
