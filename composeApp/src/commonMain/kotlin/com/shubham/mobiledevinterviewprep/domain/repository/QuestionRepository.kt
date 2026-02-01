package com.shubham.mobiledevinterviewprep.domain.repository

import com.shubham.mobiledevinterviewprep.domain.model.Question
import com.shubham.mobiledevinterviewprep.domain.model.Topic
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for accessing questions and topics.
 * 
 * Architecture Decision: Using interface to allow different implementations
 * (in-memory, local database, remote API) without changing the domain layer.
 * This follows the Dependency Inversion Principle from SOLID.
 */
interface QuestionRepository {
    
    /**
     * Returns all topics with their question counts.
     * Flow is used for reactive updates if data changes.
     */
    fun getTopics(): Flow<List<Topic>>
    
    /**
     * Returns questions for a specific topic.
     * @param topicId The ID of the topic to get questions for
     */
    fun getQuestionsByTopic(topicId: String): Flow<List<Question>>
    
    /**
     * Returns a single question by its ID.
     * @param questionId The ID of the question
     */
    fun getQuestionById(questionId: String): Flow<Question?>
    
    /**
     * Returns questions matching the given IDs.
     * Used for displaying bookmarked questions.
     * @param questionIds List of question IDs to retrieve
     */
    fun getQuestionsByIds(questionIds: List<String>): Flow<List<Question>>
    
    /**
     * Searches questions by query string.
     * Searches in question text, answer text, and tags.
     * @param query The search query
     */
    fun searchQuestions(query: String): Flow<List<Question>>
    
    /**
     * Returns topic information by its ID.
     * @param topicId The ID of the topic
     */
    fun getTopicById(topicId: String): Flow<Topic?>
}
