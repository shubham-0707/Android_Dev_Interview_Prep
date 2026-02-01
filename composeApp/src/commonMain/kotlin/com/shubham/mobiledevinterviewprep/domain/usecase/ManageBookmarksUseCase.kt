package com.shubham.mobiledevinterviewprep.domain.usecase

import com.shubham.mobiledevinterviewprep.domain.model.Question
import com.shubham.mobiledevinterviewprep.domain.repository.BookmarkRepository
import com.shubham.mobiledevinterviewprep.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

/**
 * Use case for managing bookmarks.
 * Combines bookmark repository with question repository
 * to provide complete bookmarked question data.
 */
class ManageBookmarksUseCase(
    private val bookmarkRepository: BookmarkRepository,
    private val questionRepository: QuestionRepository
) {
    /**
     * Returns all bookmarked question IDs.
     */
    fun getBookmarkedIds(): Flow<Set<String>> {
        return bookmarkRepository.getBookmarkedQuestionIds()
    }

    /**
     * Returns all bookmarked questions with full data.
     */
    fun getBookmarkedQuestions(): Flow<List<Question>> {
        return bookmarkRepository.getBookmarkedQuestionIds()
            .flatMapLatest { ids ->
                questionRepository.getQuestionsByIds(ids.toList())
            }
    }

    /**
     * Checks if a question is bookmarked.
     */
    fun isBookmarked(questionId: String): Flow<Boolean> {
        return bookmarkRepository.isBookmarked(questionId)
    }

    /**
     * Toggles bookmark state for a question.
     * @return true if now bookmarked, false if unbookmarked
     */
    suspend fun toggleBookmark(questionId: String): Boolean {
        return bookmarkRepository.toggleBookmark(questionId)
    }

    /**
     * Adds a bookmark.
     */
    suspend fun addBookmark(questionId: String) {
        bookmarkRepository.addBookmark(questionId)
    }

    /**
     * Removes a bookmark.
     */
    suspend fun removeBookmark(questionId: String) {
        bookmarkRepository.removeBookmark(questionId)
    }

    /**
     * Returns the count of bookmarked questions.
     */
    fun getBookmarkCount(): Flow<Int> {
        return bookmarkRepository.getBookmarkCount()
    }
}
