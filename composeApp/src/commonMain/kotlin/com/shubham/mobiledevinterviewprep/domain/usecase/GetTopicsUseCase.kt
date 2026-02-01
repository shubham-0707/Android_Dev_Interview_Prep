package com.shubham.mobiledevinterviewprep.domain.usecase

import com.shubham.mobiledevinterviewprep.domain.model.Topic
import com.shubham.mobiledevinterviewprep.domain.model.TopicCategory
import com.shubham.mobiledevinterviewprep.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Use case for retrieving topics, optionally grouped by category.
 * 
 * Architecture Decision: Use cases encapsulate business logic and
 * provide a clean API for ViewModels. This allows reuse across
 * different ViewModels and easier testing.
 */
class GetTopicsUseCase(
    private val questionRepository: QuestionRepository
) {
    /**
     * Returns all topics.
     */
    operator fun invoke(): Flow<List<Topic>> = questionRepository.getTopics()

    /**
     * Returns topics grouped by category.
     */
    fun groupedByCategory(): Flow<Map<TopicCategory, List<Topic>>> {
        return questionRepository.getTopics().map { topics ->
            topics.groupBy { it.category }
        }
    }

    /**
     * Returns topics for a specific category.
     */
    fun byCategory(category: TopicCategory): Flow<List<Topic>> {
        return questionRepository.getTopics().map { topics ->
            topics.filter { it.category == category }
        }
    }
}
