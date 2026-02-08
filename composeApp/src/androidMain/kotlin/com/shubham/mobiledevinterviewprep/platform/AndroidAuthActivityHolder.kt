package com.shubham.mobiledevinterviewprep.platform

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.shubham.mobiledevinterviewprep.R
import kotlin.coroutines.Continuation

/**
 * Holds Android Activity and Google Sign-In launcher.
 */
object AndroidAuthActivityHolder {
    var activity: ComponentActivity? = null
    var googleSignInClient: GoogleSignInClient? = null
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var pendingContinuation: Continuation<com.shubham.mobiledevinterviewprep.domain.model.UserProfile>? = null

    fun register(activity: ComponentActivity) {
        this.activity = activity

        val webClientId = activity.getString(R.string.default_web_client_id)
        require(webClientId.isNotBlank()) {
            "Missing default_web_client_id. Re-download google-services.json after adding SHA-1."
        }

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, options)

        launcher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) {
                pendingContinuation?.resumeWith(Result.failure(IllegalStateException("Google sign-in canceled")))
                pendingContinuation = null
                return@registerForActivityResult
            }
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken
                if (idToken.isNullOrBlank()) {
                    pendingContinuation?.resumeWith(Result.failure(IllegalStateException("Missing ID token")))
                    pendingContinuation = null
                    return@registerForActivityResult
                }
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                FirebaseAuth.getInstance()
                    .signInWithCredential(credential)
                    .addOnSuccessListener { authResult ->
                        val user = authResult.user
                        val profile = user?.let { userToProfile(it) }
                        if (profile != null) {
                            pendingContinuation?.resumeWith(Result.success(profile))
                        } else {
                            pendingContinuation?.resumeWith(Result.failure(IllegalStateException("User is null")))
                        }
                        pendingContinuation = null
                    }
                    .addOnFailureListener { e ->
                        pendingContinuation?.resumeWith(Result.failure(e))
                        pendingContinuation = null
                    }
            } catch (e: Exception) {
                pendingContinuation?.resumeWith(Result.failure(e))
                pendingContinuation = null
            }
        }
    }

    fun launchGoogleSignIn(continuation: Continuation<com.shubham.mobiledevinterviewprep.domain.model.UserProfile>) {
        pendingContinuation = continuation
        val intent = googleSignInClient?.signInIntent
            ?: throw IllegalStateException("GoogleSignInClient not initialized")
        launcher?.launch(intent) ?: throw IllegalStateException("Google Sign-In launcher not initialized")
    }
}
