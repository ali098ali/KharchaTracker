package com.kharcha.tracker.data.mapper

import com.kharcha.tracker.data.local.entity.*
import com.kharcha.tracker.domain.model.*
import java.util.Date

object Mappers {
    fun TransactionEntity.toDomain() = Transaction(
        id = id, amount = amount,
        type = TransactionType.valueOf(type),
        category = ExpenseCategory.fromName(category),
        note = note, date = Date(dateMillis),
        paymentMethod = PaymentMethod.fromName(paymentMethod),
        accountId = accountId, isRecurring = isRecurring,
        recurringId = recurringId, receiptUri = receiptUri, createdAt = createdAt
    )

    fun Transaction.toEntity() = TransactionEntity(
        id = id, amount = amount, type = type.name, category = category.name,
        note = note, dateMillis = date.time, paymentMethod = paymentMethod.name,
        accountId = accountId, isRecurring = isRecurring, recurringId = recurringId,
        receiptUri = receiptUri, createdAt = createdAt
    )

    fun BudgetEntity.toDomain() = Budget(
        id = id, category = category?.let { ExpenseCategory.fromName(it) },
        amount = amount, year = year, month = month, spent = spent
    )

    fun Budget.toEntity() = BudgetEntity(
        id = id, category = category?.name, amount = amount,
        year = year, month = month, spent = spent
    )

    fun GoalEntity.toDomain() = Goal(
        id = id, title = title, targetAmount = targetAmount,
        currentAmount = currentAmount,
        deadline = deadlineMillis?.let { Date(it) },
        isCompleted = isCompleted
    )

    fun Goal.toEntity() = GoalEntity(
        id = id, title = title, targetAmount = targetAmount,
        currentAmount = currentAmount,
        deadlineMillis = deadline?.time, isCompleted = isCompleted
    )

    fun AccountEntity.toDomain() = Account(
        id = id, name = name, type = PaymentMethod.fromName(type),
        balance = balance, currency = currency, isDefault = isDefault
    )

    fun Account.toEntity() = AccountEntity(
        id = id, name = name, type = type.name,
        balance = balance, currency = currency, isDefault = isDefault
    )

    fun RecurringEntity.toDomain() = RecurringExpense(
        id = id, amount = amount, category = ExpenseCategory.fromName(category),
        note = note, dayOfMonth = dayOfMonth,
        paymentMethod = PaymentMethod.fromName(paymentMethod),
        isActive = isActive, lastGenerated = lastGenerated
    )

    fun RecurringExpense.toEntity() = RecurringEntity(
        id = id, amount = amount, category = category.name, note = note,
        dayOfMonth = dayOfMonth, paymentMethod = paymentMethod.name,
        isActive = isActive, lastGenerated = lastGenerated
    )
}
