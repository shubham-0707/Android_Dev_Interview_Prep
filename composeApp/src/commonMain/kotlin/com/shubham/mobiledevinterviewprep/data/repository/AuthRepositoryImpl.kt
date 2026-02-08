package com.shubham.mobiledevinterviewprep.data.repository

import com.shubham.mobiledevinterviewprep.data.local.Settings
import com.shubham.mobiledevinterviewprep.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Persistent auth repository using platform-specific Settings.
 */
class AuthRepositoryImpl(
    private val settings: Settings
) : AuthRepository {

    companion object {
        private const val KEY_LOGGED_IN = "auth_logged_in"
    }

    private val loggedInState = MutableStateFlow(loadLoggedIn())

    private fun loadLoggedIn(): Boolean {
        return settings.getString(KEY_LOGGED_IN, "false").toBoolean()
    }

    private fun saveLoggedIn(isLoggedIn: Boolean) {
        settings.putString(KEY_LOGGED_IN, isLoggedIn.toString())
    }

    override fun isLoggedIn(): Flow<Boolean> = loggedInState

    override suspend fun setLoggedIn(isLoggedIn: Boolean) {
        loggedInState.update {
            saveLoggedIn(isLoggedIn)
            isLoggedIn
        }
    }

    override suspend fun logout() {
        setLoggedIn(false)
    }
}
