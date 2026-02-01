package com.shubham.mobiledevinterviewprep.data.local

/**
 * Platform-agnostic settings interface for key-value storage.
 * 
 * Architecture Decision: Using expect/actual pattern allows us to
 * leverage platform-specific storage mechanisms (SharedPreferences
 * on Android, NSUserDefaults on iOS) while keeping the interface
 * consistent in shared code.
 */
expect class Settings {
    /**
     * Retrieves a string value for the given key.
     * @param key The key to look up
     * @param defaultValue The value to return if the key doesn't exist
     * @return The stored string value or defaultValue if not found
     */
    fun getString(key: String, defaultValue: String): String
    
    /**
     * Stores a string value for the given key.
     * @param key The key to store under
     * @param value The string value to store
     */
    fun putString(key: String, value: String)
    
    /**
     * Retrieves a set of strings for the given key.
     * @param key The key to look up
     * @return The stored set of strings or empty set if not found
     */
    fun getStringSet(key: String): Set<String>
    
    /**
     * Stores a set of strings for the given key.
     * @param key The key to store under
     * @param values The set of strings to store
     */
    fun putStringSet(key: String, values: Set<String>)
    
    /**
     * Checks if a key exists in storage.
     * @param key The key to check
     * @return True if the key exists
     */
    fun contains(key: String): Boolean
    
    /**
     * Removes a value for the given key.
     * @param key The key to remove
     */
    fun remove(key: String)
    
    /**
     * Clears all stored values.
     */
    fun clear()
}

/**
 * Object to provide platform-specific Settings instance.
 * Each platform provides its own implementation.
 */
expect object SettingsFactory {
    fun create(): Settings
}
