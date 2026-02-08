package com.shubham.mobiledevinterviewprep.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for tracking study progress.
 * Stores covered question IDs for offline-first progress tracking.
 */
interface ProgressRepository {

    /**
     * Returns a flow of covered question IDs.
     */
    fun getCoveredQuestionIds(): Flow<Set<String>>

    /**
     * Returns topic IDs that already showed completion celebration.
     */
    fun getCelebratedTopicIds(): Flow<Set<String>>

    /**
     * Marks a question as covered.
     */
    suspend fun markCovered(questionId: String)

    /**
     * Removes a question from covered list.
     */
    suspend fun removeCovered(questionId: String)

    /**
     * Clears all progress.
     */
    suspend fun clearProgress()

    /**
     * Sets progress from remote sync.
     */
    suspend fun setProgress(coveredIds: Set<String>, celebratedTopicIds: Set<String>)

    /**
     * Marks a topic as already celebrated.
     */
    suspend fun markTopicCelebrated(topicId: String)
}
