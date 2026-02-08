package com.shubham.mobiledevinterviewprep.platform

/**
 * Progress snapshot for cloud sync.
 */
data class ProgressSnapshot(
    val coveredIds: Set<String>,
    val celebratedTopicIds: Set<String>
)

/**
 * Platform cloud sync for progress using Firebase Firestore.
 */
expect object PlatformProgressSync {
    suspend fun fetchProgress(userId: String): ProgressSnapshot?
    suspend fun saveProgress(userId: String, coveredIds: Set<String>, celebratedTopicIds: Set<String>)
}
