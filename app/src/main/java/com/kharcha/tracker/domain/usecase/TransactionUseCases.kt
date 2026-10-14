package com.kharcha.tracker.domain.usecase

import com.kharcha.tracker.domain.model.*
import com.kharcha.tracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(private val repo: TransactionRepository) {
    suspend operator fun invoke(t: Transaction): Long {
        require(t.amount > 0) { "Amount must be > 0" }
        return repo.add(t)
    }
}

class UpdateTransactionUseCase @Inject constructor(private val repo: TransactionRepository) {
    suspend operator fun invoke(t: Transaction) {
        require(t.amount > 0) { "Amount must be > 0" }
        repo.update(t)
    }
}

class DeleteTransactionUseCase @Inject constructor(private val repo: TransactionRepository) {
    suspend operator fun invoke(t: Transaction) = repo.delete(t)
}

class GetTransactionsByMonthUseCase @Inject constructor(private val repo: TransactionRepository) {
    operator fun invoke(year: Int, month: Int): Flow<List<Transaction>> =
        repo.getTransactionsByMonth(year, month)
}

class SearchTransactionsUseCase @Inject constructor(private val repo: TransactionRepository) {
    operator fun invoke(query: String): Flow<List<Transaction>> = repo.searchTransactions(query)
}

class GetFilteredTransactionsUseCase @Inject constructor(private val repo: TransactionRepository) {
    operator fun invoke(
        type: TransactionType? = null,
        category: ExpenseCategory? = null,
        from: Date? = null,
        to: Date? = null
    ): Flow<List<Transaction>> = repo.getFiltered(type, category, from, to)
}

class GetDashboardSummaryUseCase @Inject constructor(private val repo: TransactionRepository) {
    suspend operator fun invoke(year: Int, month: Int) = repo.getDashboardSummary(year, month)
}

class GetCategoryTotalsUseCase @Inject constructor(private val repo: TransactionRepository) {
    suspend operator fun invoke(year: Int, month: Int, type: TransactionType = TransactionType.EXPENSE) =
        repo.getCategoryTotals(year, month, type)
}

class GetDailyTotalsUseCase @Inject constructor(private val repo: TransactionRepository) {
    suspend operator fun invoke(year: Int, month: Int) = repo.getDailyTotals(year, month)
}

class GetMonthlyTotalUseCase @Inject constructor(private val repo: TransactionRepository) {
    suspend operator fun invoke(year: Int, month: Int, type: TransactionType) =
        repo.getMonthlyTotal(year, month, type)
}
