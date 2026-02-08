package com.shubham.mobiledevinterviewprep.domain.usecase

import com.shubham.mobiledevinterviewprep.domain.repository.ProgressRepository
import com.shubham.mobiledevinterviewprep.domain.repository.QuestionRepository
import com.shubham.mobiledevinterviewprep.platform.PlatformProgressSync
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

/**
 * Use case for tracking study progress and coverage.
 */
class ManageProgressUseCase(
    private val progressRepository: ProgressRepository,
    private val questionRepository: QuestionRepository,
    private val manageAuthUseCase: ManageAuthUseCase
) {
    /**
     * Returns covered question IDs.
     */
    fun getCoveredIds(): Flow<Set<String>> = progressRepository.getCoveredQuestionIds()

    /**
     * Marks a question as covered.
     */
    suspend fun markCovered(questionId: String) {
        progressRepository.markCovered(questionId)
        syncToRemoteIfLoggedIn()
    }

    /**
     * Clears progress for all questions.
     */
    suspend fun clearProgress() {
        progressRepository.clearProgress()
        syncToRemoteIfLoggedIn()
    }

    /**
     * Checks if a topic just completed and marks it celebrated once.
     * Returns true if a celebration should be shown.
     */
    suspend fun checkAndCelebrateIfCompleted(topicId: String): Boolean {
        val questions = questionRepository.getQuestionsByTopic(topicId).first()
        if (questions.isEmpty()) return false
        val coveredIds = progressRepository.getCoveredQuestionIds().first()
        val isComplete = questions.all { it.id in coveredIds }
        if (!isComplete) return false
        val celebrated = progressRepository.getCelebratedTopicIds().first()
        if (topicId in celebrated) return false
        progressRepository.markTopicCelebrated(topicId)
        syncToRemoteIfLoggedIn()
        return true
    }

    /**
     * Syncs progress from cloud on login.
     */
    suspend fun syncFromRemoteIfLoggedIn() {
        val user = manageAuthUseCase.currentUser().first() ?: return
        try {
            val snapshot = PlatformProgressSync.fetchProgress(user.uid) ?: return
            progressRepository.setProgress(snapshot.coveredIds, snapshot.celebratedTopicIds)
        } catch (_: Exception) {
            // Ignore offline failures; keep local progress.
        }
    }

    private suspend fun syncToRemoteIfLoggedIn() {
        val user = manageAuthUseCase.currentUser().first() ?: return
        val covered = progressRepository.getCoveredQuestionIds().first()
        val celebrated = progressRepository.getCelebratedTopicIds().first()
        try {
            PlatformProgressSync.saveProgress(user.uid, covered, celebrated)
        } catch (_: Exception) {
            // Ignore offline failures; will sync next time.
        }
    }

    /**
     * Returns covered count for each topic ID.
     */
    fun getCoveredCountByTopic(): Flow<Map<String, Int>> {
        return combine(
            questionRepository.getTopics(),
            progressRepository.getCoveredQuestionIds(),
            questionRepository.getAllQuestions()
        ) { topics, coveredIds, allQuestions ->
            val coveredByTopic = topics.associate { it.id to 0 }.toMutableMap()
            allQuestions.forEach { question ->
                if (question.id in coveredIds) {
                    coveredByTopic[question.topicId] =
                        (coveredByTopic[question.topicId] ?: 0) + 1
                }
            }
            coveredByTopic
        }
    }
}
