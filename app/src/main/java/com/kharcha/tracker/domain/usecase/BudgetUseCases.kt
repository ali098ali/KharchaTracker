package com.kharcha.tracker.domain.usecase

import com.kharcha.tracker.domain.model.Budget
import com.kharcha.tracker.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBudgetsUseCase @Inject constructor(private val repo: BudgetRepository) {
    operator fun invoke(year: Int, month: Int): Flow<List<Budget>> = repo.getBudgets(year, month)
}

class AddBudgetUseCase @Inject constructor(private val repo: BudgetRepository) {
    suspend operator fun invoke(budget: Budget): Long {
        require(budget.amount > 0) { "Budget amount must be > 0" }
        return repo.addBudget(budget)
    }
}

class UpdateBudgetUseCase @Inject constructor(private val repo: BudgetRepository) {
    suspend operator fun invoke(budget: Budget) = repo.updateBudget(budget)
}

class DeleteBudgetUseCase @Inject constructor(private val repo: BudgetRepository) {
    suspend operator fun invoke(budget: Budget) = repo.deleteBudget(budget)
}

class RefreshBudgetSpentUseCase @Inject constructor(private val repo: BudgetRepository) {
    suspend operator fun invoke(year: Int, month: Int) = repo.refreshSpent(year, month)
}
