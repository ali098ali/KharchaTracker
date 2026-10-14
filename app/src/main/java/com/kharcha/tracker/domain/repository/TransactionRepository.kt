package com.kharcha.tracker.domain.repository

import com.kharcha.tracker.domain.model.*
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getTransactionsByMonth(year: Int, month: Int): Flow<List<Transaction>>
    fun searchTransactions(query: String): Flow<List<Transaction>>
    fun getFiltered(type: TransactionType?, category: ExpenseCategory?, from: Date?, to: Date?): Flow<List<Transaction>>
    suspend fun getById(id: Long): Transaction?
    suspend fun add(transaction: Transaction): Long
    suspend fun update(transaction: Transaction)
    suspend fun delete(transaction: Transaction)
    suspend fun getMonthlyTotal(year: Int, month: Int, type: TransactionType): Double
    suspend fun getCategoryTotals(year: Int, month: Int, type: TransactionType = TransactionType.EXPENSE): List<CategoryTotal>
    suspend fun getDailyTotals(year: Int, month: Int): List<DailyTotal>
    suspend fun getDashboardSummary(year: Int, month: Int): DashboardSummary
}

interface BudgetRepository {
    fun getBudgets(year: Int, month: Int): Flow<List<Budget>>
    suspend fun addBudget(budget: Budget): Long
    suspend fun updateBudget(budget: Budget)
    suspend fun deleteBudget(budget: Budget)
    suspend fun refreshSpent(year: Int, month: Int)
}

interface GoalRepository {
    fun getGoals(): Flow<List<Goal>>
    suspend fun addGoal(goal: Goal): Long
    suspend fun updateGoal(goal: Goal)
    suspend fun deleteGoal(goal: Goal)
}

interface RecurringRepository {
    fun getActive(): Flow<List<RecurringExpense>>
    suspend fun add(recurring: RecurringExpense): Long
    suspend fun update(recurring: RecurringExpense)
    suspend fun delete(recurring: RecurringExpense)
}

interface AccountRepository {
    fun getAccounts(): Flow<List<Account>>
    suspend fun addAccount(account: Account): Long
    suspend fun updateAccount(account: Account)
}
