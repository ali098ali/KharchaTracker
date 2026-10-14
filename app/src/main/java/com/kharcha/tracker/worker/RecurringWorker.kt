package com.kharcha.tracker.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kharcha.tracker.domain.model.Transaction
import com.kharcha.tracker.domain.model.TransactionType
import com.kharcha.tracker.domain.repository.RecurringRepository
import com.kharcha.tracker.domain.repository.TransactionRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.Calendar
import java.util.Date

/**
 * Phase 5: Fully working recurring expense generator.
 * Schedule with PeriodicWorkRequest (daily) from Application.
 */
@HiltWorker
class RecurringWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val recurringRepo: RecurringRepository,
    private val txRepo: TransactionRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val today = Calendar.getInstance()
            val dayOfMonth = today.get(Calendar.DAY_OF_MONTH)
            val monthStart = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val active = recurringRepo.getActive().first()
            for (item in active) {
                if (item.dayOfMonth != dayOfMonth) continue
                if (item.lastGenerated >= monthStart) continue

                txRepo.add(
                    Transaction(
                        amount = item.amount,
                        type = TransactionType.EXPENSE,
                        category = item.category,
                        note = item.note.ifBlank { "Recurring: ${item.category.displayName}" },
                        date = Date(),
                        paymentMethod = item.paymentMethod,
                        isRecurring = true,
                        recurringId = item.id
                    )
                )
                recurringRepo.update(item.copy(lastGenerated = System.currentTimeMillis()))
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
