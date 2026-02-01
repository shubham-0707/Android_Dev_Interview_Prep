package com.shubham.mobiledevinterviewprep.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a bookmarked question.
 * Stored separately to allow persistence of bookmark state.
 * 
 * @property questionId ID of the bookmarked question
 * @property createdAt Timestamp when the bookmark was created (epoch millis)
 */
@Serializable
data class Bookmark(
    val questionId: String,
    val createdAt: Long = 0L
)
