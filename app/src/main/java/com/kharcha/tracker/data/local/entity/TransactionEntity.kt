package com.kharcha.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val type: String,
    val category: String,
    val note: String,
    val dateMillis: Long,
    val paymentMethod: String,
    val accountId: Long,
    val isRecurring: Boolean,
    val recurringId: Long?,
    val receiptUri: String?,
    val createdAt: Long
)
