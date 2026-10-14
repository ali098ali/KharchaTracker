package com.kharcha.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String?, // null = overall
    val amount: Double,
    val year: Int,
    val month: Int,
    val spent: Double = 0.0
)
