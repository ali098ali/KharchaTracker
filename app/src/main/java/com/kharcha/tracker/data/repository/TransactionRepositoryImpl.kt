package com.kharcha.tracker.data.repository

import com.kharcha.tracker.data.local.dao.TransactionDao
import com.kharcha.tracker.data.mapper.Mappers.toDomain
import com.kharcha.tracker.data.mapper.Mappers.toEntity
import com.kharcha.tracker.domain.model.*
import com.kharcha.tracker.domain.repository.TransactionRepository
import com.kharcha.tracker.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionRepository {

    override fun getAllTransactions() = dao.getAll().map { it.map { e -> e.toDomain() } }

    override fun getTransactionsByMonth(year: Int, month: Int): Flow<List<Transaction>> {
        val (s, e) = DateUtils.monthRange(year, month)
        return dao.getBetween(s, e).map { it.map { x -> x.toDomain() } }
    }

    override fun searchTransactions(query: String) =
        dao.search(query).map { it.map { e -> e.toDomain() } }

    override fun getFiltered(type: TransactionType?, category: ExpenseCategory?, from: Date?, to: Date?) =
        dao.filtered(type?.name, category?.name, from?.time, to?.time)
            .map { it.map { e -> e.toDomain() } }

    override suspend fun getById(id: Long) = dao.getById(id)?.toDomain()
    override suspend fun add(t: Transaction) = dao.insert(t.toEntity())
    override suspend fun update(t: Transaction) = dao.update(t.toEntity())
    override suspend fun delete(t: Transaction) = dao.delete(t.toEntity())

    override suspend fun getMonthlyTotal(year: Int, month: Int, type: TransactionType): Double {
        val (s, e) = DateUtils.monthRange(year, month)
        return dao.sumByType(type.name, s, e)
    }

    override suspend fun getCategoryTotals(year: Int, month: Int, type: TransactionType): List<CategoryTotal> {
        val (s, e) = DateUtils.monthRange(year, month)
        return dao.categorySums(type.name, s, e).map {
            CategoryTotal(ExpenseCategory.fromName(it.category), it.total)
        }
    }

    override suspend fun getDailyTotals(year: Int, month: Int): List<DailyTotal> {
        val (s, e) = DateUtils.monthRange(year, month)
        return dao.dailySums(s, e).map { DailyTotal(it.day, it.total) }
    }

    override suspend fun getDashboardSummary(year: Int, month: Int): DashboardSummary {
        val exp = getMonthlyTotal(year, month, TransactionType.EXPENSE)
        val inc = getMonthlyTotal(year, month, TransactionType.INCOME)
        val cats = getCategoryTotals(year, month)
        return DashboardSummary(
            monthlyExpense = exp,
            monthlyIncome = inc,
            balance = inc - exp,
            savings = (inc - exp).coerceAtLeast(0.0),
            topCategories = cats.take(5)
        )
    }
}
