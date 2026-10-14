package com.kharcha.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recurring")
data class RecurringEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val category: String,
    val note: String,
    val dayOfMonth: Int,
    val paymentMethod: String,
    val isActive: Boolean,
    val lastGenerated: Long
)
