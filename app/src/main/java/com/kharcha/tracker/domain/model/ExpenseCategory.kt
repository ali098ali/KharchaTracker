package com.kharcha.tracker.domain.model

enum class ExpenseCategory(val displayName: String, val colorKey: String) {
    FOOD("Food", "cat_food"),
    TRANSPORT("Transport", "cat_transport"),
    SHOPPING("Shopping", "cat_shopping"),
    BILLS("Bills", "cat_bills"),
    ENTERTAINMENT("Entertainment", "cat_entertainment"),
    HEALTH("Health", "cat_health"),
    EDUCATION("Education", "cat_education"),
    SALARY("Salary", "cat_salary"),
    FREELANCE("Freelance", "cat_freelance"),
    INVESTMENT("Investment", "cat_investment"),
    OTHER("Other", "cat_other");

    companion object {
        fun fromName(name: String): ExpenseCategory =
            entries.find { it.name.equals(name, true) } ?: OTHER

        fun expenseCategories() = listOf(FOOD, TRANSPORT, SHOPPING, BILLS, ENTERTAINMENT, HEALTH, EDUCATION, OTHER)
        fun incomeCategories() = listOf(SALARY, FREELANCE, INVESTMENT, OTHER)
    }
}
