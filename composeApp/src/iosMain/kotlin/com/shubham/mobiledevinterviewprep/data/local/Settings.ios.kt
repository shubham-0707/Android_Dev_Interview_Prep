package com.shubham.mobiledevinterviewprep.data.local

import platform.Foundation.NSUserDefaults

/**
 * iOS implementation of Settings using NSUserDefaults.
 * 
 * NSUserDefaults provides persistent storage that survives
 * app restarts, similar to SharedPreferences on Android.
 */
actual class Settings(private val userDefaults: NSUserDefaults) {
    
    actual fun getString(key: String, defaultValue: String): String {
        return userDefaults.stringForKey(key) ?: defaultValue
    }
    
    actual fun putString(key: String, value: String) {
        userDefaults.setObject(value, forKey = key)
        userDefaults.synchronize()
    }
    
    actual fun getStringSet(key: String): Set<String> {
        val array = userDefaults.arrayForKey(key) ?: return emptySet()
        return array.filterIsInstance<String>().toSet()
    }
    
    actual fun putStringSet(key: String, values: Set<String>) {
        userDefaults.setObject(values.toList(), forKey = key)
        userDefaults.synchronize()
    }
    
    actual fun contains(key: String): Boolean {
        return userDefaults.objectForKey(key) != null
    }
    
    actual fun remove(key: String) {
        userDefaults.removeObjectForKey(key)
        userDefaults.synchronize()
    }
    
    actual fun clear() {
        val dictionary = userDefaults.dictionaryRepresentation()
        for (key in dictionary.keys) {
            if (key is String) {
                userDefaults.removeObjectForKey(key)
            }
        }
        userDefaults.synchronize()
    }
}

/**
 * Factory object for creating Settings instances on iOS.
 * Uses standard NSUserDefaults.
 */
actual object SettingsFactory {
    private var settings: Settings? = null
    
    actual fun create(): Settings {
        if (settings == null) {
            settings = Settings(NSUserDefaults.standardUserDefaults)
        }
        return settings!!
    }
}
