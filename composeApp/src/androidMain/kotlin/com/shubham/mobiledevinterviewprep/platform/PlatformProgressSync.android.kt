package com.shubham.mobiledevinterviewprep.platform

import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

actual object PlatformProgressSync {
    private val firestore = FirebaseFirestore.getInstance()

    actual suspend fun fetchProgress(userId: String): ProgressSnapshot? = suspendCoroutine { cont ->
        firestore.collection("users").document(userId)
            .collection("meta").document("progress")
            .get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) {
                    cont.resume(null)
                } else {
                    val covered = (doc.get("coveredIds") as? List<*>)?.filterIsInstance<String>()?.toSet() ?: emptySet()
                    val celebrated = (doc.get("celebratedTopicIds") as? List<*>)?.filterIsInstance<String>()?.toSet() ?: emptySet()
                    cont.resume(ProgressSnapshot(covered, celebrated))
                }
            }
            .addOnFailureListener { e -> cont.resumeWithException(e) }
    }

    actual suspend fun saveProgress(
        userId: String,
        coveredIds: Set<String>,
        celebratedTopicIds: Set<String>
    ) = suspendCoroutine<Unit> { cont ->
        val data = mapOf(
            "coveredIds" to coveredIds.toList(),
            "celebratedTopicIds" to celebratedTopicIds.toList()
        )
        firestore.collection("users").document(userId)
            .collection("meta").document("progress")
            .set(data)
            .addOnSuccessListener { cont.resume(Unit) }
            .addOnFailureListener { e -> cont.resumeWithException(e) }
    }
}
