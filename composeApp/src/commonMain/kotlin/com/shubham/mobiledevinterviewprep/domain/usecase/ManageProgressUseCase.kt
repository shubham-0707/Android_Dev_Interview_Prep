package com.shubham.mobiledevinterviewprep.domain.usecase

import com.shubham.mobiledevinterviewprep.domain.repository.ProgressRepository
import com.shubham.mobiledevinterviewprep.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

/**
 * Use case for tracking study progress and coverage.
 */
class ManageProgressUseCase(
    private val progressRepository: ProgressRepository,
    private val questionRepository: QuestionRepository
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
    }

    /**
     * Clears progress for all questions.
     */
    suspend fun clearProgress() {
        progressRepository.clearProgress()
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
        return true
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
