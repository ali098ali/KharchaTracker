package com.kharcha.tracker.data.repository

import com.kharcha.tracker.data.local.dao.AccountDao
import com.kharcha.tracker.data.mapper.Mappers.toDomain
import com.kharcha.tracker.data.mapper.Mappers.toEntity
import com.kharcha.tracker.domain.model.Account
import com.kharcha.tracker.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(private val dao: AccountDao) : AccountRepository {
    override fun getAccounts(): Flow<List<Account>> = dao.getAll().map { it.map { e -> e.toDomain() } }
    override suspend fun addAccount(account: Account) = dao.insert(account.toEntity())
    override suspend fun updateAccount(account: Account) = dao.update(account.toEntity())
}
