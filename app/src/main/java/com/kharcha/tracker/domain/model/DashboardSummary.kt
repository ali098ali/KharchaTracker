package com.kharcha.tracker.domain.model

data class DashboardSummary(
    val monthlyExpense: Double = 0.0,
    val monthlyIncome: Double = 0.0,
    val balance: Double = 0.0,
    val savings: Double = 0.0,
    val budgetRemaining: Double = 0.0,
    val topCategories: List<CategoryTotal> = emptyList()
)
