package com.shubham.mobiledevinterviewprep.domain.model

/**
 * Authenticated user profile.
 */
data class UserProfile(
    val uid: String,
    val displayName: String?,
    val email: String?,
    val phoneNumber: String?,
    val photoUrl: String?
)
