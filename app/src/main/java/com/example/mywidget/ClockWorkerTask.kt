package com.example.mywidget

import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.Delay
import java.text.DateFormat
import java.time.Duration
import java.util.concurrent.Delayed
import java.util.concurrent.TimeUnit

class ClockWorkerTask(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    var count = 1

    companion object {
        private val uniqueWorkName = ClockWorkerTask::class.java.simpleName

        fun enqueue(context: Context, force: Boolean = false) {
            val manager = WorkManager.getInstance(context)
            val requestBuilder = PeriodicWorkRequestBuilder<ClockWorkerTask>(
                Duration.ofMinutes(15)
            ).build()
            var workPolicy = ExistingPeriodicWorkPolicy.KEEP
            if (force) {
                workPolicy = ExistingPeriodicWorkPolicy.UPDATE
            }
            manager.enqueueUniquePeriodicWork(
                uniqueWorkName,
                workPolicy,
                requestBuilder
            )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(uniqueWorkName)
        }
    }

    override suspend fun doWork(): Result {
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(ClockWidget::class.java)
        val calendar = Calendar.getInstance().time
        val currentTime = DateFormat.getTimeInstance().format(calendar)
        count += 1
        Log.i("timeeeer2", count.toString())
        return try {
            setWidgetData(glanceIds, currentTime)
            Result.success()
        } catch (e: Exception) {

            Result.retry()
        }
    }

    private suspend fun setWidgetData(glanceIds: List<GlanceId>, currentTime: String) {
        Log.i("timeeeer3", currentTime)
        glanceIds.forEach { glanceId ->
            updateAppWidgetState(
                context = context,
                glanceId = glanceId
            ) { prefs ->
                prefs[stringPreferencesKey("update_time")] = currentTime
            }
        }
        ClockWidget().updateAll(context)
    }
}