package com.kharcha.tracker

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.kharcha.tracker.data.local.dao.AccountDao
import com.kharcha.tracker.data.local.entity.AccountEntity
import com.kharcha.tracker.worker.RecurringWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class KharchaApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory
    @Inject lateinit var accountDao: AccountDao

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        seedDefaultAccount()
        // Schedule after a short delay so WorkManager is ready with Hilt factory
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val request = PeriodicWorkRequestBuilder<RecurringWorker>(1, TimeUnit.DAYS).build()
                WorkManager.getInstance(this@KharchaApp).enqueueUniquePeriodicWork(
                    "recurring_expenses",
                    ExistingPeriodicWorkPolicy.KEEP,
                    request
                )
            } catch (_: Exception) {
                // Ignore if WorkManager not ready yet
            }
        }
    }

    private fun seedDefaultAccount() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val list = accountDao.getAll().first()
                if (list.isEmpty()) {
                    accountDao.insert(
                        AccountEntity(
                            name = "Cash",
                            type = "CASH",
                            balance = 0.0,
                            currency = "PKR",
                            isDefault = true
                        )
                    )
                }
            } catch (_: Exception) {
            }
        }
    }
}
