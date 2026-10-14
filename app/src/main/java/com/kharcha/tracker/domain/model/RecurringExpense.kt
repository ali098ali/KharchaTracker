package com.kharcha.tracker.domain.model

data class RecurringExpense(
    val id: Long = 0,
    val amount: Double,
    val category: ExpenseCategory,
    val note: String = "",
    val dayOfMonth: Int, // 1-28
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val isActive: Boolean = true,
    val lastGenerated: Long = 0L
)
