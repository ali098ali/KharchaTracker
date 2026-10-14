package com.kharcha.tracker.domain.model

import java.util.Date

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType = TransactionType.EXPENSE,
    val category: ExpenseCategory = ExpenseCategory.OTHER,
    val note: String = "",
    val date: Date = Date(),
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val accountId: Long = 1L,
    val isRecurring: Boolean = false,
    val recurringId: Long? = null,
    val receiptUri: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
