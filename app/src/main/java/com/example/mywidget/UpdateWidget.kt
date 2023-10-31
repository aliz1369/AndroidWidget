package com.example.mywidget

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll

suspend fun updateWidget(time: String, context: Context) {

    GlanceAppWidgetManager(context).getGlanceIds(ClockWidget::class.java).forEach { glanceId ->
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[stringPreferencesKey("update_time")] = time
        }
    }
    ClockWidget().updateAll(context)
}