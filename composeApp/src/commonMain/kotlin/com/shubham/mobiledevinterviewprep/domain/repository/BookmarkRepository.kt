package com.shubham.mobiledevinterviewprep.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing bookmarks.
 * 
 * Architecture Decision: Separating bookmark persistence from questions
 * allows independent storage strategies. Bookmarks can use simple
 * key-value storage while questions use structured data.
 */
interface BookmarkRepository {
    
    /**
     * Returns a flow of all bookmarked question IDs.
     * Flow enables reactive UI updates when bookmarks change.
     */
    fun getBookmarkedQuestionIds(): Flow<Set<String>>
    
    /**
     * Checks if a specific question is bookmarked.
     * @param questionId The ID of the question to check
     */
    fun isBookmarked(questionId: String): Flow<Boolean>
    
    /**
     * Adds a bookmark for the given question.
     * @param questionId The ID of the question to bookmark
     */
    suspend fun addBookmark(questionId: String)
    
    /**
     * Removes a bookmark for the given question.
     * @param questionId The ID of the question to unbookmark
     */
    suspend fun removeBookmark(questionId: String)
    
    /**
     * Toggles the bookmark state for the given question.
     * @param questionId The ID of the question to toggle
     * @return true if bookmarked after toggle, false otherwise
     */
    suspend fun toggleBookmark(questionId: String): Boolean
    
    /**
     * Returns the count of bookmarked questions.
     */
    fun getBookmarkCount(): Flow<Int>
}
