package com.shubham.mobiledevinterviewprep.platform

import com.shubham.mobiledevinterviewprep.domain.model.UserProfile

/**
 * Platform auth bridge for Firebase Auth.
 */
expect object PlatformAuth {
    suspend fun currentUser(): UserProfile?
    suspend fun signInWithGoogle(): UserProfile
    suspend fun startPhoneVerification(phoneNumber: String): String
    suspend fun verifyPhoneCode(verificationId: String, code: String): UserProfile
    suspend fun updateProfilePhoto(bytes: ByteArray): UserProfile?
    suspend fun signOut()
}
