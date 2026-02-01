package com.shubham.mobiledevinterviewprep.domain.usecase

import com.shubham.mobiledevinterviewprep.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Use case for finding the index of a question within its topic's question list.
 * 
 * This is useful when navigating from search results to flashcards,
 * where we need to know the position of the selected question.
 */
class GetQuestionIndexUseCase(
    private val questionRepository: QuestionRepository
) {
    /**
     * Returns the index of a question within its topic's question list.
     * @param topicId The topic ID to search within
     * @param questionId The question ID to find
     * @return Flow emitting the index, or 0 if not found
     */
    operator fun invoke(topicId: String, questionId: String): Flow<Int> {
        return questionRepository.getQuestionsByTopic(topicId).map { questions ->
            val index = questions.indexOfFirst { it.id == questionId }
            if (index >= 0) index else 0
        }
    }
}
