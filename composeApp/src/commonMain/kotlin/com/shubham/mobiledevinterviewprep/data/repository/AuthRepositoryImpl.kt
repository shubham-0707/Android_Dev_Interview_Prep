package com.shubham.mobiledevinterviewprep.data.repository

import com.shubham.mobiledevinterviewprep.domain.model.UserProfile
import com.shubham.mobiledevinterviewprep.domain.repository.AuthRepository
import com.shubham.mobiledevinterviewprep.platform.PlatformAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking

/**
 * Auth repository backed by Firebase Auth via platform implementations.
 */
class AuthRepositoryImpl : AuthRepository {

    private val userState = MutableStateFlow<UserProfile?>(null)

    init {
        runBlocking {
            userState.value = PlatformAuth.currentUser()
        }
    }

    override fun currentUser(): Flow<UserProfile?> = userState

    override suspend fun signInWithGoogle(): UserProfile {
        val user = PlatformAuth.signInWithGoogle()
        userState.update { user }
        return user
    }

    override suspend fun startPhoneVerification(phoneNumber: String): String {
        return PlatformAuth.startPhoneVerification(phoneNumber)
    }

    override suspend fun verifyPhoneCode(verificationId: String, code: String): UserProfile {
        val user = PlatformAuth.verifyPhoneCode(verificationId, code)
        userState.update { user }
        return user
    }

    override suspend fun updateProfilePhoto(bytes: ByteArray): UserProfile? {
        val user = PlatformAuth.updateProfilePhoto(bytes)
        if (user != null) {
            userState.update { user }
        }
        return user
    }

    override suspend fun signOut() {
        PlatformAuth.signOut()
        userState.update { null }
    }
}
