package com.kharcha.tracker.domain.model

data class Budget(
    val id: Long = 0,
    val category: ExpenseCategory? = null, // null = overall monthly budget
    val amount: Double,
    val year: Int,
    val month: Int, // 0-11
    val spent: Double = 0.0
) {
    val remaining: Double get() = (amount - spent).coerceAtLeast(0.0)
    val progress: Float get() = if (amount > 0) (spent / amount).toFloat().coerceIn(0f, 1.5f) else 0f
    val isOver: Boolean get() = spent > amount
}
