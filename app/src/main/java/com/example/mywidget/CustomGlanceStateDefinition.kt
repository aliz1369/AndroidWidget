package com.example.mywidget

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.glance.state.GlanceStateDefinition
import java.io.File

object CustomGlanceStateDefinition : GlanceStateDefinition<Preferences> {
    override suspend fun getDataStore(context: Context, fileKey: String): DataStore<Preferences> {
        return context.dataStore
    }

    override fun getLocation(context: Context, fileKey: String): File {
        // Note: The Datastore Preference file resides is in the context.applicationContext.filesDir + "datastore/"
        return File(context.applicationContext.filesDir, "datastore/$fileName")
    }

    private const val fileName = "widget_store"
    private val Context.dataStore: DataStore<Preferences>
            by preferencesDataStore(name = fileName)
}