package com.shubham.mobiledevinterviewprep.data.local

import android.content.Context
import android.content.SharedPreferences

/**
 * Android implementation of Settings using SharedPreferences.
 * 
 * Uses a singleton pattern to ensure the same SharedPreferences
 * instance is used throughout the app lifecycle.
 */
actual class Settings(private val prefs: SharedPreferences) {
    
    actual fun getString(key: String, defaultValue: String): String {
        return prefs.getString(key, defaultValue) ?: defaultValue
    }
    
    actual fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
    
    actual fun getStringSet(key: String): Set<String> {
        return prefs.getStringSet(key, emptySet()) ?: emptySet()
    }
    
    actual fun putStringSet(key: String, values: Set<String>) {
        prefs.edit().putStringSet(key, values).apply()
    }
    
    actual fun contains(key: String): Boolean {
        return prefs.contains(key)
    }
    
    actual fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }
    
    actual fun clear() {
        prefs.edit().clear().apply()
    }
}

/**
 * Factory object for creating Settings instances on Android.
 * 
 * Note: Android requires a Context to access SharedPreferences.
 * The initialize() method must be called from Application.onCreate()
 * or MainActivity.onCreate() before using Settings.
 */
actual object SettingsFactory {
    private var appContext: Context? = null
    private var settings: Settings? = null
    
    private const val PREFS_NAME = "interview_prep_settings"
    
    /**
     * Initializes the SettingsFactory with an application context.
     * Must be called before create().
     * @param context The application context
     */
    fun initialize(context: Context) {
        appContext = context.applicationContext
    }
    
    actual fun create(): Settings {
        if (settings == null) {
            val context = appContext 
                ?: throw IllegalStateException(
                    "SettingsFactory not initialized. Call SettingsFactory.initialize(context) first."
                )
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            settings = Settings(prefs)
        }
        return settings!!
    }
}
