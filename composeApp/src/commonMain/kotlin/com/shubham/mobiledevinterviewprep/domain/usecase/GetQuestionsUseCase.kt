package com.shubham.mobiledevinterviewprep.domain.usecase

import com.shubham.mobiledevinterviewprep.domain.model.Difficulty
import com.shubham.mobiledevinterviewprep.domain.model.Question
import com.shubham.mobiledevinterviewprep.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Use case for retrieving questions with various filtering options.
 */
class GetQuestionsUseCase(
    private val questionRepository: QuestionRepository
) {
    /**
     * Returns all questions for a specific topic.
     */
    operator fun invoke(topicId: String): Flow<List<Question>> {
        return questionRepository.getQuestionsByTopic(topicId)
    }

    /**
     * Returns questions filtered by difficulty.
     */
    fun byDifficulty(topicId: String, difficulty: Difficulty): Flow<List<Question>> {
        return questionRepository.getQuestionsByTopic(topicId).map { questions ->
            questions.filter { it.difficulty == difficulty }
        }
    }

    /**
     * Returns a single question by ID.
     */
    fun byId(questionId: String): Flow<Question?> {
        return questionRepository.getQuestionById(questionId)
    }

    /**
     * Returns questions for the given IDs.
     */
    fun byIds(questionIds: List<String>): Flow<List<Question>> {
        return questionRepository.getQuestionsByIds(questionIds)
    }
}
