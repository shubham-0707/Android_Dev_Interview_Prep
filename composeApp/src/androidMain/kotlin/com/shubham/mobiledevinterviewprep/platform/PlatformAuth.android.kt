package com.shubham.mobiledevinterviewprep.platform

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.FirebaseException
import com.google.firebase.storage.FirebaseStorage
import com.shubham.mobiledevinterviewprep.domain.model.UserProfile
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

actual object PlatformAuth {
    private val auth = FirebaseAuth.getInstance()

    actual suspend fun currentUser(): UserProfile? {
        return auth.currentUser?.let { userToProfile(it) }
    }

    actual suspend fun signInWithGoogle(): UserProfile = suspendCoroutine { cont ->
        AndroidAuthActivityHolder.launchGoogleSignIn(cont)
    }

    actual suspend fun startPhoneVerification(phoneNumber: String): String = suspendCoroutine { cont ->
        val activity = AndroidAuthActivityHolder.activity
            ?: return@suspendCoroutine cont.resumeWithException(IllegalStateException("Activity not set"))

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: com.google.firebase.auth.PhoneAuthCredential) {
                auth.signInWithCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                cont.resumeWithException(e)
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                cont.resume(verificationId)
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    actual suspend fun verifyPhoneCode(verificationId: String, code: String): UserProfile = suspendCoroutine { cont ->
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { result ->
                val user = result.user
                val profile = user?.let { userToProfile(it) }
                if (profile != null) {
                    cont.resume(profile)
                } else {
                    cont.resumeWithException(IllegalStateException("User is null"))
                }
            }
            .addOnFailureListener { e -> cont.resumeWithException(e) }
    }

    actual suspend fun updateProfilePhoto(bytes: ByteArray): UserProfile? = suspendCoroutine { cont ->
        val user = auth.currentUser ?: return@suspendCoroutine cont.resume(null)
        val storageRef = FirebaseStorage.getInstance()
            .reference.child("users/${user.uid}/profile.jpg")
        storageRef.putBytes(bytes)
            .addOnSuccessListener {
                storageRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        val updates = UserProfileChangeRequest.Builder()
                            .setPhotoUri(uri)
                            .build()
                        user.updateProfile(updates)
                            .addOnSuccessListener {
                                cont.resume(userToProfile(user))
                            }
                            .addOnFailureListener { e -> cont.resumeWithException(e) }
                    }
                    .addOnFailureListener { e -> cont.resumeWithException(e) }
            }
            .addOnFailureListener { e -> cont.resumeWithException(e) }
    }

    actual suspend fun signOut() {
        auth.signOut()
        AndroidAuthActivityHolder.googleSignInClient?.signOut()
    }
}

internal fun userToProfile(user: com.google.firebase.auth.FirebaseUser): UserProfile {
    return UserProfile(
        uid = user.uid,
        displayName = user.displayName,
        email = user.email,
        phoneNumber = user.phoneNumber,
        photoUrl = user.photoUrl?.toString()
    )
}
