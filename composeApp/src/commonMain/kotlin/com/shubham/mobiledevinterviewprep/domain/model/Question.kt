package com.shubham.mobiledevinterviewprep.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents an interview question with its answer.
 * This is the core data model for the flashcard system.
 * 
 * @property id Unique identifier for the question
 * @property topicId ID of the topic this question belongs to
 * @property questionText The question to be displayed
 * @property answerText The answer to be revealed
 * @property difficulty Difficulty level (Easy/Medium/Hard)
 * @property tags List of tags for categorization and search
 */
@Serializable
data class Question(
    val id: String,
    val topicId: String,
    val questionText: String,
    val answerText: String,
    val difficulty: Difficulty,
    val tags: List<String> = emptyList()
)
