package com.jermaine.dailyfocus.device

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.jermaine.dailyfocus.R
import com.jermaine.dailyfocus.feature.usecase.ArchiveAllUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ArchiveWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val archiveAllUseCase: ArchiveAllUseCase,
) : CoroutineWorker(
    context,
    workerParameters,
) {
    override suspend fun doWork(): Result {
        return try {
            setForeground(getForegroundInfo())
            archiveAllUseCase.execute()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID,
            createNotification(),
        )
    }

    private fun createNotification(): Notification {
        val channel = NotificationChannel(
            CHANNEL_ID,
            applicationContext.getString(R.string.title_archiver_notification_channel),
            NotificationManager.IMPORTANCE_DEFAULT,
        )

        val notificationsManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationsManager.createNotificationChannel(channel)

        return Notification.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(
                applicationContext.getString(R.string.title_archive_todos_notification),
            )
            .build()
    }

    companion object {
        const val NOTIFICATION_ID = 1000
        const val CHANNEL_ID = "Archiver"
        const val WORKER_NAME = "Archiver"
    }
}
