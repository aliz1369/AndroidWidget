package com.example.mywidget

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text

class ClockWidget : GlanceAppWidget() {

    override val stateDefinition = CustomGlanceStateDefinition
    override suspend fun provideGlance(context: Context, id: GlanceId) {

        provideContent {
            val state = currentState<Preferences>()
            val time = state[stringPreferencesKey("update_time")] ?: ""
            Column(
                modifier = GlanceModifier.fillMaxSize().background(Color.Gray),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = time)
            }
        }
    }


}

class SimpleClockWidget : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = ClockWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        ClockWorkerTask.enqueue(context)
    }
    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        ClockWorkerTask.cancel(context)
    }
}
