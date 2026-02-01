package com.shubham.mobiledevinterviewprep.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a specific topic within a category.
 * For example, "Arrays" is a topic under the DSA category.
 * 
 * @property id Unique identifier for the topic
 * @property name Display name of the topic
 * @property category The parent category this topic belongs to
 * @property description Brief description of what this topic covers
 * @property questionCount Number of questions available in this topic
 */
@Serializable
data class Topic(
    val id: String,
    val name: String,
    val category: TopicCategory,
    val description: String,
    val questionCount: Int = 0
)
