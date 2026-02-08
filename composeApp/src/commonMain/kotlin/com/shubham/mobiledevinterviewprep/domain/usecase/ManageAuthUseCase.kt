package com.shubham.mobiledevinterviewprep.domain.usecase

import com.shubham.mobiledevinterviewprep.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for authentication state handling.
 */
class ManageAuthUseCase(
    private val authRepository: AuthRepository
) {
    fun isLoggedIn(): Flow<Boolean> = authRepository.isLoggedIn()
    suspend fun login() = authRepository.setLoggedIn(true)
    suspend fun logout() = authRepository.logout()
}
