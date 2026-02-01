package com.shubham.mobiledevinterviewprep.presentation.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes using Kotlin Serialization.
 * 
 * Architecture Decision: Using sealed class with @Serializable
 * enables type-safe navigation with compile-time checking.
 * This approach works with Navigation Compose 2.8+.
 */
sealed interface Screen {
    
    /**
     * Home screen showing list of topics grouped by category.
     */
    @Serializable
    data object Home : Screen
    
    /**
     * Topic screen showing list of questions for a specific topic.
     * @property topicId The ID of the topic to display
     */
    @Serializable
    data class Topic(val topicId: String) : Screen
    
    /**
     * Flashcard screen for studying questions.
     * @property topicId The ID of the topic (null for bookmarks mode)
     * @property startIndex Index to start at (default 0)
     * @property bookmarksOnly True to show only bookmarked questions
     * @property questionId Optional specific question ID to navigate to (for search results)
     */
    @Serializable
    data class Flashcard(
        val topicId: String? = null,
        val startIndex: Int = 0,
        val bookmarksOnly: Boolean = false,
        val questionId: String? = null
    ) : Screen
    
    /**
     * Bookmarks screen showing all saved questions.
     */
    @Serializable
    data object Bookmarks : Screen
    
    /**
     * Search screen for finding questions across all topics.
     */
    @Serializable
    data object Search : Screen
}
