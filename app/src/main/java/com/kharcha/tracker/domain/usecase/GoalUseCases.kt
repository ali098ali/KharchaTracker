package com.kharcha.tracker.domain.usecase

import com.kharcha.tracker.domain.model.Goal
import com.kharcha.tracker.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGoalsUseCase @Inject constructor(private val repo: GoalRepository) {
    operator fun invoke(): Flow<List<Goal>> = repo.getGoals()
}

class AddGoalUseCase @Inject constructor(private val repo: GoalRepository) {
    suspend operator fun invoke(goal: Goal): Long {
        require(goal.targetAmount > 0) { "Target must be > 0" }
        return repo.addGoal(goal)
    }
}

class UpdateGoalUseCase @Inject constructor(private val repo: GoalRepository) {
    suspend operator fun invoke(goal: Goal) = repo.updateGoal(goal)
}

class DeleteGoalUseCase @Inject constructor(private val repo: GoalRepository) {
    suspend operator fun invoke(goal: Goal) = repo.deleteGoal(goal)
}
