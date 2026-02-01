package com.shubham.mobiledevinterviewprep.data.repository

import com.shubham.mobiledevinterviewprep.data.datasource.SampleData
import com.shubham.mobiledevinterviewprep.domain.model.Question
import com.shubham.mobiledevinterviewprep.domain.model.Topic
import com.shubham.mobiledevinterviewprep.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * In-memory implementation of QuestionRepository.
 * 
 * Architecture Decision: Using in-memory storage with SampleData for
 * offline-first design. This implementation can be easily replaced with
 * SQLDelight or Room for persistent local storage in production.
 * 
 * Flow is used even for static data to maintain API consistency and
 * allow future implementations to provide real-time updates.
 */
class QuestionRepositoryImpl : QuestionRepository {

    override fun getTopics(): Flow<List<Topic>> = flow {
        emit(SampleData.getTopicsWithCounts())
    }

    override fun getQuestionsByTopic(topicId: String): Flow<List<Question>> = flow {
        emit(SampleData.getQuestionsForTopic(topicId))
    }

    override fun getQuestionById(questionId: String): Flow<Question?> = flow {
        emit(SampleData.questions.find { it.id == questionId })
    }

    override fun getQuestionsByIds(questionIds: List<String>): Flow<List<Question>> = flow {
        val questionIdSet = questionIds.toSet()
        emit(SampleData.questions.filter { it.id in questionIdSet })
    }

    override fun searchQuestions(query: String): Flow<List<Question>> = flow {
        emit(SampleData.searchQuestions(query))
    }

    override fun getTopicById(topicId: String): Flow<Topic?> = flow {
        emit(SampleData.getTopicsWithCounts().find { it.id == topicId })
    }
}
