package com.shubham.mobiledevinterviewprep.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubham.mobiledevinterviewprep.domain.model.Question
import com.shubham.mobiledevinterviewprep.domain.usecase.ManageBookmarksUseCase
import com.shubham.mobiledevinterviewprep.domain.usecase.SearchQuestionsUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

/**
 * UI State for the Search Screen.
 */
sealed class SearchUiState {
    data object Idle : SearchUiState()
    data object Loading : SearchUiState()
    data class Success(
        val query: String,
        val results: List<Question>,
        val bookmarkedIds: Set<String>
    ) : SearchUiState() {
        val isEmpty: Boolean get() = results.isEmpty() && query.isNotBlank()
    }
    data class Error(val message: String) : SearchUiState()
}

/**
 * ViewModel for the Search Screen.
 * Implements debounced search for better UX.
 * 
 * Architecture Decision: Using debounce to prevent excessive searches
 * while the user is typing. This improves performance and provides
 * a smoother experience.
 */
class SearchViewModel(
    private val searchQuestionsUseCase: SearchQuestionsUseCase,
    private val manageBookmarksUseCase: ManageBookmarksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        setupSearch()
    }

    /**
     * Sets up the debounced search flow.
     */
    @OptIn(FlowPreview::class)
    private fun setupSearch() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300) // Wait 300ms after last keystroke
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        flowOf(SearchUiState.Idle)
                    } else {
                        _uiState.value = SearchUiState.Loading
                        combine(
                            searchQuestionsUseCase(query),
                            manageBookmarksUseCase.getBookmarkedIds()
                        ) { results, bookmarkedIds ->
                            SearchUiState.Success(
                                query = query,
                                results = results,
                                bookmarkedIds = bookmarkedIds
                            )
                        }
                    }
                }
                .catch { e ->
                    _uiState.value = SearchUiState.Error(e.message ?: "Search failed")
                }
                .collect { state ->
                    _uiState.value = state
                }
        }
    }

    /**
     * Updates the search query.
     */
    fun updateQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * Clears the search query and results.
     */
    fun clearSearch() {
        _searchQuery.value = ""
        _uiState.value = SearchUiState.Idle
    }

    /**
     * Toggles bookmark for a question.
     */
    fun toggleBookmark(questionId: String) {
        viewModelScope.launch {
            manageBookmarksUseCase.toggleBookmark(questionId)
        }
    }
}
