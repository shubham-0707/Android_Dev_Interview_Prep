package com.shubham.mobiledevinterviewprep.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubham.mobiledevinterviewprep.domain.model.Question
import com.shubham.mobiledevinterviewprep.domain.model.Topic
import com.shubham.mobiledevinterviewprep.domain.usecase.GetQuestionsUseCase
import com.shubham.mobiledevinterviewprep.domain.usecase.GetTopicUseCase
import com.shubham.mobiledevinterviewprep.domain.usecase.ManageBookmarksUseCase
import com.shubham.mobiledevinterviewprep.domain.usecase.ManageProgressUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * UI State for Topic Screen showing list of questions.
 */
sealed class TopicUiState {
    data object Loading : TopicUiState()
    data class Success(
        val topic: Topic,
        val questions: List<Question>,
        val bookmarkedIds: Set<String>,
        val coveredCount: Int
    ) : TopicUiState()
    data class Error(val message: String) : TopicUiState()
}

/**
 * ViewModel for the Topic Screen.
 * Shows all questions for a specific topic.
 */
class TopicViewModel(
    private val getTopicUseCase: GetTopicUseCase,
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val manageBookmarksUseCase: ManageBookmarksUseCase,
    private val manageProgressUseCase: ManageProgressUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TopicUiState>(TopicUiState.Loading)
    val uiState: StateFlow<TopicUiState> = _uiState.asStateFlow()

    private var currentTopicId: String? = null

    /**
     * Loads questions for the given topic.
     */
    fun loadTopic(topicId: String) {
        currentTopicId = topicId
        _uiState.value = TopicUiState.Loading

        viewModelScope.launch {
            combine(
                getTopicUseCase(topicId),
                getQuestionsUseCase(topicId),
                manageBookmarksUseCase.getBookmarkedIds(),
                manageProgressUseCase.getCoveredIds()
            ) { topic, questions, bookmarkedIds, coveredIds ->
                if (topic != null) {
                    val coveredCount = questions.count { it.id in coveredIds }
                    TopicUiState.Success(
                        topic = topic,
                        questions = questions,
                        bookmarkedIds = bookmarkedIds,
                        coveredCount = coveredCount
                    )
                } else {
                    TopicUiState.Error("Topic not found")
                }
            }
            .catch { e ->
                _uiState.value = TopicUiState.Error(e.message ?: "Unknown error occurred")
            }
            .collect { state ->
                _uiState.value = state
            }
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
     * Refreshes the current topic data.
     */
    fun refresh() {
        currentTopicId?.let { loadTopic(it) }
    }
}
