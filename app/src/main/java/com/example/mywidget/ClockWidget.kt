package com.example.mywidget

import android.content.Context
import android.icu.util.Calendar
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
import java.sql.Time
import java.text.DateFormat

object ClockWidget : GlanceAppWidget() {
    val clock = stringPreferencesKey("clock")
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        updateAppWidgetState(context = context, glanceId = id) { prefs ->
            val calendar = Calendar.getInstance().time
            val currentTime = DateFormat.getTimeInstance().format(calendar)
            prefs[clock] = currentTime
            ClockWidget.update(context,id)
        }
        provideContent {
            val realTime = currentState(key = clock)
            Column(
                modifier = GlanceModifier.fillMaxSize().background(Color.Gray),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = realTime.toString())

            }
        }
    }
}

class SimpleClockWidget : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = ClockWidget
}