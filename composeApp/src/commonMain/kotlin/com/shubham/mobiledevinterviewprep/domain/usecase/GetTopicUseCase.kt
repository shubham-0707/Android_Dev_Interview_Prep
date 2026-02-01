package com.shubham.mobiledevinterviewprep.domain.usecase

import com.shubham.mobiledevinterviewprep.domain.model.Topic
import com.shubham.mobiledevinterviewprep.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for retrieving a single topic by ID.
 */
class GetTopicUseCase(
    private val questionRepository: QuestionRepository
) {
    /**
     * Returns topic information for the given ID.
     */
    operator fun invoke(topicId: String): Flow<Topic?> {
        return questionRepository.getTopicById(topicId)
    }
}
