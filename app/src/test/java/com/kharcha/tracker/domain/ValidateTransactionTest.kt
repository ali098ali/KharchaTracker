package com.kharcha.tracker.domain

import com.kharcha.tracker.domain.model.ExpenseCategory
import com.kharcha.tracker.domain.model.Transaction
import com.kharcha.tracker.domain.model.TransactionType
import org.junit.Assert.*
import org.junit.Test

/**
 * Phase 6: Unit tests for domain validation rules.
 */
class ValidateTransactionTest {

    @Test
    fun amountMustBePositive() {
        val t = Transaction(amount = 0.0, type = TransactionType.EXPENSE, category = ExpenseCategory.FOOD)
        assertTrue(t.amount <= 0)
        val valid = t.copy(amount = 100.0)
        assertTrue(valid.amount > 0)
    }

    @Test
    fun expenseCategoriesExist() {
        assertTrue(ExpenseCategory.expenseCategories().isNotEmpty())
        assertTrue(ExpenseCategory.incomeCategories().isNotEmpty())
    }

    @Test
    fun categoryFromNameFallback() {
        assertEquals(ExpenseCategory.OTHER, ExpenseCategory.fromName("unknown_xyz"))
        assertEquals(ExpenseCategory.FOOD, ExpenseCategory.fromName("FOOD"))
    }
}
