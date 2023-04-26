package com.jermaine.dailyfocus.core

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.jermaine.dailyfocus.device.ArchiveWorker
import dagger.hilt.android.HiltAndroidApp
import java.time.Duration
import java.time.LocalTime
import javax.inject.Inject

@HiltAndroidApp
class DailyFocusApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        val archiveWorkRequest =
            PeriodicWorkRequestBuilder<ArchiveWorker>(Duration.ofDays(1))
                .setInitialDelay(
                    Duration.between(
                        LocalTime.of(0, 0),
                        LocalTime.now(),
                    ),
                )
                .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                ArchiveWorker.WORKER_NAME,
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                archiveWorkRequest,
            )
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
