package com.shubham.mobiledevinterviewprep.data.repository

import com.shubham.mobiledevinterviewprep.data.local.Settings
import com.shubham.mobiledevinterviewprep.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * Persistent implementation of BookmarkRepository using platform-specific Settings.
 * 
 * Architecture Decision: Using MutableStateFlow for reactive updates combined
 * with platform-specific Settings for persistence. This provides:
 * - Reactive UI updates when bookmarks change
 * - Persistence across app restarts
 * - Offline-first behavior (no network required)
 * 
 * The Settings abstraction allows platform-specific storage:
 * - Android: SharedPreferences
 * - iOS: NSUserDefaults
 */
class BookmarkRepositoryImpl(
    private val settings: Settings
) : BookmarkRepository {

    companion object {
        private const val BOOKMARKS_KEY = "bookmarked_questions"
    }

    /**
     * Reactive storage for bookmarked question IDs.
     * Initialized from persistent storage on creation.
     */
    private val bookmarks = MutableStateFlow(loadBookmarksFromStorage())

    /**
     * Loads bookmarks from persistent storage.
     */
    private fun loadBookmarksFromStorage(): Set<String> {
        return settings.getStringSet(BOOKMARKS_KEY)
    }

    /**
     * Saves bookmarks to persistent storage.
     */
    private fun saveBookmarksToStorage(bookmarkIds: Set<String>) {
        settings.putStringSet(BOOKMARKS_KEY, bookmarkIds)
    }

    override fun getBookmarkedQuestionIds(): Flow<Set<String>> = bookmarks

    override fun isBookmarked(questionId: String): Flow<Boolean> = bookmarks.map { 
        questionId in it 
    }

    override suspend fun addBookmark(questionId: String) {
        bookmarks.update { current -> 
            val updated = current + questionId
            saveBookmarksToStorage(updated)
            updated
        }
    }

    override suspend fun removeBookmark(questionId: String) {
        bookmarks.update { current -> 
            val updated = current - questionId
            saveBookmarksToStorage(updated)
            updated
        }
    }

    override suspend fun toggleBookmark(questionId: String): Boolean {
        var isNowBookmarked = false
        bookmarks.update { current ->
            if (questionId in current) {
                isNowBookmarked = false
                val updated = current - questionId
                saveBookmarksToStorage(updated)
                updated
            } else {
                isNowBookmarked = true
                val updated = current + questionId
                saveBookmarksToStorage(updated)
                updated
            }
        }
        return isNowBookmarked
    }

    override fun getBookmarkCount(): Flow<Int> = bookmarks.map { it.size }
}
