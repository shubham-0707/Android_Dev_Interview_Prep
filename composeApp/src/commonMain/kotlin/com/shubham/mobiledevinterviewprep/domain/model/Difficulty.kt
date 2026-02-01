package com.shubham.mobiledevinterviewprep.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the difficulty level of a question.
 * Used for filtering and displaying appropriate visual indicators.
 */
@Serializable
enum class Difficulty {
    EASY,
    MEDIUM,
    HARD;

    /**
     * Returns a human-readable display name for the difficulty level.
     */
    fun displayName(): String = when (this) {
        EASY -> "Easy"
        MEDIUM -> "Medium"
        HARD -> "Hard"
    }
}
