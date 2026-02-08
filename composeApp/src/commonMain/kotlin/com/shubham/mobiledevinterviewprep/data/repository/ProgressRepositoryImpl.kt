package com.shubham.mobiledevinterviewprep.data.repository

import com.shubham.mobiledevinterviewprep.data.local.Settings
import com.shubham.mobiledevinterviewprep.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Persistent progress repository using platform-specific Settings.
 * Tracks covered question IDs across app sessions.
 */
class ProgressRepositoryImpl(
    private val settings: Settings
) : ProgressRepository {

    companion object {
        private const val COVERED_KEY = "covered_questions"
        private const val CELEBRATED_KEY = "celebrated_topics"
    }

    private val coveredIds = MutableStateFlow(loadCoveredFromStorage())
    private val celebratedTopicIds = MutableStateFlow(loadCelebratedFromStorage())

    private fun loadCoveredFromStorage(): Set<String> {
        return settings.getStringSet(COVERED_KEY)
    }

    private fun saveCoveredToStorage(ids: Set<String>) {
        settings.putStringSet(COVERED_KEY, ids)
    }

    private fun loadCelebratedFromStorage(): Set<String> {
        return settings.getStringSet(CELEBRATED_KEY)
    }

    private fun saveCelebratedToStorage(ids: Set<String>) {
        settings.putStringSet(CELEBRATED_KEY, ids)
    }

    override fun getCoveredQuestionIds(): Flow<Set<String>> = coveredIds

    override fun getCelebratedTopicIds(): Flow<Set<String>> = celebratedTopicIds

    override suspend fun markCovered(questionId: String) {
        coveredIds.update { current ->
            val updated = current + questionId
            saveCoveredToStorage(updated)
            updated
        }
    }

    override suspend fun removeCovered(questionId: String) {
        coveredIds.update { current ->
            val updated = current - questionId
            saveCoveredToStorage(updated)
            updated
        }
    }

    override suspend fun clearProgress() {
        coveredIds.update {
            saveCoveredToStorage(emptySet())
            emptySet()
        }
        celebratedTopicIds.update {
            saveCelebratedToStorage(emptySet())
            emptySet()
        }
    }

    override suspend fun setProgress(coveredIds: Set<String>, celebratedTopicIds: Set<String>) {
        this.coveredIds.update {
            saveCoveredToStorage(coveredIds)
            coveredIds
        }
        this.celebratedTopicIds.update {
            saveCelebratedToStorage(celebratedTopicIds)
            celebratedTopicIds
        }
    }

    override suspend fun markTopicCelebrated(topicId: String) {
        celebratedTopicIds.update { current ->
            val updated = current + topicId
            saveCelebratedToStorage(updated)
            updated
        }
    }
}
