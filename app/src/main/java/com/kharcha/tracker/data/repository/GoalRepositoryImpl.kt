package com.kharcha.tracker.data.repository

import com.kharcha.tracker.data.local.dao.GoalDao
import com.kharcha.tracker.data.mapper.Mappers.toDomain
import com.kharcha.tracker.data.mapper.Mappers.toEntity
import com.kharcha.tracker.domain.model.Goal
import com.kharcha.tracker.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalRepositoryImpl @Inject constructor(private val dao: GoalDao) : GoalRepository {
    override fun getGoals(): Flow<List<Goal>> = dao.getAll().map { it.map { e -> e.toDomain() } }
    override suspend fun addGoal(goal: Goal) = dao.insert(goal.toEntity())
    override suspend fun updateGoal(goal: Goal) = dao.update(goal.toEntity())
    override suspend fun deleteGoal(goal: Goal) = dao.delete(goal.toEntity())
}
