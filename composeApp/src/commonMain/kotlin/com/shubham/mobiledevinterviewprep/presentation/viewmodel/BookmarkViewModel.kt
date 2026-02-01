package com.shubham.mobiledevinterviewprep.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubham.mobiledevinterviewprep.domain.model.Question
import com.shubham.mobiledevinterviewprep.domain.usecase.ManageBookmarksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * UI State for the Bookmarks Screen.
 */
sealed class BookmarkUiState {
    data object Loading : BookmarkUiState()
    data class Success(val questions: List<Question>) : BookmarkUiState() {
        val isEmpty: Boolean get() = questions.isEmpty()
    }
    data class Error(val message: String) : BookmarkUiState()
}

/**
 * ViewModel for the Bookmarks Screen.
 * Shows all bookmarked questions.
 */
class BookmarkViewModel(
    private val manageBookmarksUseCase: ManageBookmarksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<BookmarkUiState>(BookmarkUiState.Loading)
    val uiState: StateFlow<BookmarkUiState> = _uiState.asStateFlow()

    init {
        loadBookmarks()
    }

    /**
     * Loads all bookmarked questions.
     */
    private fun loadBookmarks() {
        viewModelScope.launch {
            manageBookmarksUseCase.getBookmarkedQuestions()
                .catch { e ->
                    _uiState.value = BookmarkUiState.Error(e.message ?: "Unknown error occurred")
                }
                .collect { questions ->
                    _uiState.value = BookmarkUiState.Success(questions)
                }
        }
    }

    /**
     * Removes a bookmark.
     */
    fun removeBookmark(questionId: String) {
        viewModelScope.launch {
            manageBookmarksUseCase.removeBookmark(questionId)
        }
    }

    /**
     * Toggles bookmark state for a question.
     */
    fun toggleBookmark(questionId: String) {
        viewModelScope.launch {
            manageBookmarksUseCase.toggleBookmark(questionId)
        }
    }

    /**
     * Refreshes bookmarks.
     */
    fun refresh() {
        _uiState.value = BookmarkUiState.Loading
        loadBookmarks()
    }
}
