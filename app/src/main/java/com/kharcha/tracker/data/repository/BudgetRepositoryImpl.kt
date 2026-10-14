package com.kharcha.tracker.data.repository

import com.kharcha.tracker.data.local.dao.BudgetDao
import com.kharcha.tracker.data.local.dao.TransactionDao
import com.kharcha.tracker.data.mapper.Mappers.toDomain
import com.kharcha.tracker.data.mapper.Mappers.toEntity
import com.kharcha.tracker.domain.model.Budget
import com.kharcha.tracker.domain.model.ExpenseCategory
import com.kharcha.tracker.domain.repository.BudgetRepository
import com.kharcha.tracker.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao,
    private val txDao: TransactionDao
) : BudgetRepository {

    override fun getBudgets(year: Int, month: Int): Flow<List<Budget>> =
        budgetDao.getForMonth(year, month).map { it.map { e -> e.toDomain() } }

    override suspend fun addBudget(budget: Budget) = budgetDao.insert(budget.toEntity())
    override suspend fun updateBudget(budget: Budget) = budgetDao.update(budget.toEntity())
    override suspend fun deleteBudget(budget: Budget) = budgetDao.delete(budget.toEntity())

    override suspend fun refreshSpent(year: Int, month: Int) {
        val (s, e) = DateUtils.monthRange(year, month)
        val budgets = budgetDao.getForMonth(year, month).first()
        for (b in budgets) {
            val spent = if (b.category == null) {
                txDao.sumByType("EXPENSE", s, e)
            } else {
                txDao.categorySums("EXPENSE", s, e)
                    .find { it.category == b.category }?.total ?: 0.0
            }
            budgetDao.updateSpent(b.id, spent)
        }
    }
}
