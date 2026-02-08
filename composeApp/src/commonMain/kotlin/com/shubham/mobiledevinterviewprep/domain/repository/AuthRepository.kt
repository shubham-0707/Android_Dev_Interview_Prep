package com.shubham.mobiledevinterviewprep.domain.repository

import com.shubham.mobiledevinterviewprep.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for authentication state.
 */
interface AuthRepository {
    fun currentUser(): Flow<UserProfile?>
    suspend fun signInWithGoogle(): UserProfile
    suspend fun startPhoneVerification(phoneNumber: String): String
    suspend fun verifyPhoneCode(verificationId: String, code: String): UserProfile
    suspend fun updateProfilePhoto(bytes: ByteArray): UserProfile?
    suspend fun signOut()
}
