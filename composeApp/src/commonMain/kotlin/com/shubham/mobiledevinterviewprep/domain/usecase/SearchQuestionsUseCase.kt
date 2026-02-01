package com.shubham.mobiledevinterviewprep.domain.usecase

import com.shubham.mobiledevinterviewprep.domain.model.Question
import com.shubham.mobiledevinterviewprep.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for searching questions across all topics.
 * 
 * Searches in question text, answer text, and tags.
 */
class SearchQuestionsUseCase(
    private val questionRepository: QuestionRepository
) {
    /**
     * Searches questions by the given query.
     * Returns empty list if query is blank.
     */
    operator fun invoke(query: String): Flow<List<Question>> {
        return questionRepository.searchQuestions(query.trim())
    }
}
