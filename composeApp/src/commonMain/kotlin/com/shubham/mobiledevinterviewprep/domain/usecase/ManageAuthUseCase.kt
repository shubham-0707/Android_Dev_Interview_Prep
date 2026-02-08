package com.shubham.mobiledevinterviewprep.domain.usecase

import com.shubham.mobiledevinterviewprep.domain.model.UserProfile
import com.shubham.mobiledevinterviewprep.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for authentication state handling.
 */
class ManageAuthUseCase(
    private val authRepository: AuthRepository
) {
    fun currentUser(): Flow<UserProfile?> = authRepository.currentUser()
    suspend fun signInWithGoogle(): UserProfile = authRepository.signInWithGoogle()
    suspend fun startPhoneVerification(phoneNumber: String): String =
        authRepository.startPhoneVerification(phoneNumber)
    suspend fun verifyPhoneCode(verificationId: String, code: String): UserProfile =
        authRepository.verifyPhoneCode(verificationId, code)
    suspend fun updateProfilePhoto(bytes: ByteArray): UserProfile? =
        authRepository.updateProfilePhoto(bytes)
    suspend fun signOut() = authRepository.signOut()
}
