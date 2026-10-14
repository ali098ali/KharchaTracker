package com.kharcha.tracker.data.repository

import com.kharcha.tracker.data.local.dao.RecurringDao
import com.kharcha.tracker.data.mapper.Mappers.toDomain
import com.kharcha.tracker.data.mapper.Mappers.toEntity
import com.kharcha.tracker.domain.model.RecurringExpense
import com.kharcha.tracker.domain.repository.RecurringRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecurringRepositoryImpl @Inject constructor(private val dao: RecurringDao) : RecurringRepository {
    override fun getActive(): Flow<List<RecurringExpense>> =
        dao.getActive().map { it.map { e -> e.toDomain() } }
    override suspend fun add(recurring: RecurringExpense) = dao.insert(recurring.toEntity())
    override suspend fun update(recurring: RecurringExpense) = dao.update(recurring.toEntity())
    override suspend fun delete(recurring: RecurringExpense) = dao.delete(recurring.toEntity())
}
