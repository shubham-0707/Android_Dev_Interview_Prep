package com.shubham.mobiledevinterviewprep.platform

import cocoapods.FirebaseFirestore.FIRFirestore
import kotlinx.cinterop.ObjCAction
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual object PlatformProgressSync {
    private val firestore = FIRFirestore.firestore()

    actual suspend fun fetchProgress(userId: String): ProgressSnapshot? = suspendCancellableCoroutine { cont ->
        firestore
            .collectionWithPath("users")
            .documentWithPath(userId)
            .collectionWithPath("meta")
            .documentWithPath("progress")
            .getDocumentWithCompletion { snapshot, error ->
                if (error != null) {
                    cont.resumeWithException(Exception(error.localizedDescription))
                    return@getDocumentWithCompletion
                }
                val data = snapshot?.data()
                if (data == null) {
                    cont.resume(null)
                    return@getDocumentWithCompletion
                }
                val covered = (data["coveredIds"] as? List<*>)?.filterIsInstance<String>()?.toSet() ?: emptySet()
                val celebrated = (data["celebratedTopicIds"] as? List<*>)?.filterIsInstance<String>()?.toSet() ?: emptySet()
                cont.resume(ProgressSnapshot(covered, celebrated))
            }
    }

    actual suspend fun saveProgress(
        userId: String,
        coveredIds: Set<String>,
        celebratedTopicIds: Set<String>
    ) = suspendCancellableCoroutine<Unit> { cont ->
        val data = mapOf(
            "coveredIds" to coveredIds.toList(),
            "celebratedTopicIds" to celebratedTopicIds.toList()
        )
        firestore
            .collectionWithPath("users")
            .documentWithPath(userId)
            .collectionWithPath("meta")
            .documentWithPath("progress")
            .setData(data) { error ->
                if (error != null) {
                    cont.resumeWithException(Exception(error.localizedDescription))
                } else {
                    cont.resume(Unit)
                }
            }
    }
}
