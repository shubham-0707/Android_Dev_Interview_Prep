package com.shubham.mobiledevinterviewprep.platform

import com.shubham.mobiledevinterviewprep.domain.model.UserProfile
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import cocoapods.FirebaseAuth.Auth
import cocoapods.FirebaseAuth.GoogleAuthProvider
import cocoapods.FirebaseAuth.PhoneAuthProvider
import cocoapods.FirebaseStorage.FIRStorage
import cocoapods.GoogleSignIn.GIDSignIn
import kotlinx.coroutines.suspendCancellableCoroutine

actual object PlatformAuth {
    actual suspend fun currentUser(): UserProfile? {
        val user = Auth.auth().currentUser ?: return null
        return user.toProfile()
    }

    actual suspend fun signInWithGoogle(): UserProfile = kotlinx.coroutines.suspendCancellableCoroutine { cont ->
        val rootVC = rootViewController()
            ?: return@suspendCancellableCoroutine cont.resumeWithException(IllegalStateException("No root view controller"))
        GIDSignIn.sharedInstance.signInWithPresentingViewController(rootVC) { result, error ->
            if (error != null) {
                cont.resumeWithException(Exception(error.localizedDescription))
                return@signInWithPresentingViewController
            }
            val user = result?.user
            val idToken = user?.idToken?.tokenString
            val accessToken = user?.accessToken?.tokenString
            if (idToken == null || accessToken == null) {
                cont.resumeWithException(IllegalStateException("Missing Google tokens"))
                return@signInWithPresentingViewController
            }
            val credential = GoogleAuthProvider.credentialWithIDToken(idToken, accessToken)
            Auth.auth().signInWithCredential(credential) { authResult, authError ->
                if (authError != null) {
                    cont.resumeWithException(Exception(authError.localizedDescription))
                } else {
                    val profile = authResult?.user?.toProfile()
                    if (profile != null) {
                        cont.resume(profile)
                    } else {
                        cont.resumeWithException(IllegalStateException("User is null"))
                    }
                }
            }
        }
    }

    actual suspend fun startPhoneVerification(phoneNumber: String): String = kotlinx.coroutines.suspendCancellableCoroutine { cont ->
        PhoneAuthProvider.provider().verifyPhoneNumber(phoneNumber, null) { verificationId, error ->
            if (error != null) {
                cont.resumeWithException(Exception(error.localizedDescription))
            } else if (verificationId != null) {
                cont.resume(verificationId)
            } else {
                cont.resumeWithException(IllegalStateException("No verification ID"))
            }
        }
    }

    actual suspend fun verifyPhoneCode(verificationId: String, code: String): UserProfile =
        kotlinx.coroutines.suspendCancellableCoroutine { cont ->
            val credential = PhoneAuthProvider.provider().credentialWithVerificationID(verificationId, code)
            Auth.auth().signInWithCredential(credential) { authResult, error ->
                if (error != null) {
                    cont.resumeWithException(Exception(error.localizedDescription))
                } else {
                    val profile = authResult?.user?.toProfile()
                    if (profile != null) {
                        cont.resume(profile)
                    } else {
                        cont.resumeWithException(IllegalStateException("User is null"))
                    }
                }
            }
        }

    actual suspend fun updateProfilePhoto(bytes: ByteArray): UserProfile? = kotlinx.coroutines.suspendCancellableCoroutine { cont ->
        val user = Auth.auth().currentUser ?: return@suspendCancellableCoroutine cont.resume(null)
        val data = bytes.toNSData()
        val storageRef = FIRStorage.storage().reference().child("users/${user.uid}/profile.jpg")
        storageRef.putData(data, null) { _, error ->
            if (error != null) {
                cont.resumeWithException(Exception(error.localizedDescription))
                return@putData
            }
            storageRef.downloadURLWithCompletion { url, urlError ->
                if (urlError != null) {
                    cont.resumeWithException(Exception(urlError.localizedDescription))
                    return@downloadURLWithCompletion
                }
                val updates = user.profileChangeRequest()
                updates.photoURL = url
                updates.commitChangesWithCompletion { updateError ->
                    if (updateError != null) {
                        cont.resumeWithException(Exception(updateError.localizedDescription))
                    } else {
                        cont.resume(user.toProfile())
                    }
                }
            }
        }
    }

    actual suspend fun signOut() {
        Auth.auth().signOut(null)
        GIDSignIn.sharedInstance.signOut()
    }
}

private fun rootViewController(): UIViewController? {
    val window: UIWindow? = UIApplication.sharedApplication.windows.firstOrNull { it.isKeyWindow() }
    return window?.rootViewController
}

private fun cocoapods.FirebaseAuth.FIRUser.toProfile(): UserProfile {
    return UserProfile(
        uid = uid,
        displayName = displayName,
        email = email,
        phoneNumber = phoneNumber,
        photoUrl = photoURL?.absoluteString
    )
}

private fun ByteArray.toNSData(): NSData {
    return usePinned { pinned ->
        NSData.create(bytes = pinned.addressOf(0), length = size.toULong())
    }
}

