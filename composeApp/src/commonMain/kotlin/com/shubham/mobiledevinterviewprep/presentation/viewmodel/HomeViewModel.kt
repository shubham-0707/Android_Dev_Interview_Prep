package com.shubham.mobiledevinterviewprep.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubham.mobiledevinterviewprep.domain.model.Topic
import com.shubham.mobiledevinterviewprep.domain.model.TopicCategory
import com.shubham.mobiledevinterviewprep.domain.usecase.GetTopicsUseCase
import com.shubham.mobiledevinterviewprep.domain.usecase.ManageBookmarksUseCase
import com.shubham.mobiledevinterviewprep.domain.usecase.ManageProgressUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * UI State for the Home Screen.
 * Using sealed class allows exhaustive handling in UI.
 */
sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(
        val topicsByCategory: Map<TopicCategory, List<Topic>>,
        val bookmarkCount: Int,
        val coveredCountByTopic: Map<String, Int>
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

/**
 * ViewModel for the Home Screen.
 * 
 * Architecture Decision: ViewModel is placed in shared KMP module,
 * making it available on all platforms. Uses StateFlow for
 * unidirectional data flow with Compose.
 */
class HomeViewModel(
    private val getTopicsUseCase: GetTopicsUseCase,
    private val manageBookmarksUseCase: ManageBookmarksUseCase,
    private val manageProgressUseCase: ManageProgressUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadTopics()
    }

    /**
     * Loads topics grouped by category along with bookmark count.
     */
    private fun loadTopics() {
        viewModelScope.launch {
            combine(
                getTopicsUseCase.groupedByCategory(),
                manageBookmarksUseCase.getBookmarkCount(),
                manageProgressUseCase.getCoveredCountByTopic()
            ) { topics, bookmarkCount, coveredCountByTopic ->
                HomeUiState.Success(
                    topicsByCategory = topics,
                    bookmarkCount = bookmarkCount,
                    coveredCountByTopic = coveredCountByTopic
                )
            }
            .catch { e ->
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error occurred")
            }
            .collect { state ->
                _uiState.value = state
            }
        }
    }

    /**
     * Refreshes the topic list.
     */
    fun refresh() {
        _uiState.value = HomeUiState.Loading
        loadTopics()
    }
}
