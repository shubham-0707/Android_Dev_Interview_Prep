package com.shubham.mobiledevinterviewprep.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for authentication state.
 */
interface AuthRepository {
    fun isLoggedIn(): Flow<Boolean>
    suspend fun setLoggedIn(isLoggedIn: Boolean)
    suspend fun logout()
}
