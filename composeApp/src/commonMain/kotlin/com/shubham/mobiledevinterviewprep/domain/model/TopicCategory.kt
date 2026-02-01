package com.shubham.mobiledevinterviewprep.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the main category of interview topics.
 * Each category contains multiple sub-topics (Topic).
 */
@Serializable
enum class TopicCategory {
    DSA,
    ANDROID,
    KOTLIN;

    /**
     * Returns a human-readable display name for the category.
     */
    fun displayName(): String = when (this) {
        DSA -> "Data Structures & Algorithms"
        ANDROID -> "Android Development"
        KOTLIN -> "Kotlin Language"
    }

    /**
     * Returns an emoji icon for visual representation.
     */
    fun icon(): String = when (this) {
        DSA -> "🧮"
        ANDROID -> "📱"
        KOTLIN -> "💜"
    }
}
