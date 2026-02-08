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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * UI State for the Flashcard Screen.
 */
sealed class FlashcardUiState {
    data object Loading : FlashcardUiState()
    data class Success(
        val topic: Topic?,
        val questions: List<Question>,
        val currentIndex: Int,
        val isAnswerRevealed: Boolean,
        val bookmarkedIds: Set<String>,
        val showCelebration: Boolean = false
    ) : FlashcardUiState() {
        val currentQuestion: Question? 
            get() = questions.getOrNull(currentIndex)
        
        val progress: Float 
            get() = if (questions.isEmpty()) 0f else (currentIndex + 1).toFloat() / questions.size
        
        val progressText: String 
            get() = "${currentIndex + 1} / ${questions.size}"
        
        val hasNext: Boolean 
            get() = currentIndex < questions.size - 1
        
        val hasPrevious: Boolean 
            get() = currentIndex > 0
        
        val isCurrentBookmarked: Boolean
            get() = currentQuestion?.id in bookmarkedIds
    }
    data class Error(val message: String) : FlashcardUiState()
    data object Empty : FlashcardUiState()
}

/**
 * ViewModel for the Flashcard Screen.
 * Manages swipe-based navigation and answer reveal state.
 * 
 * Architecture Decision: The ViewModel maintains the current index
 * and reveal state, allowing the UI to be stateless and react
 * purely to state changes.
 */
class FlashcardViewModel(
    private val getTopicUseCase: GetTopicUseCase,
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val manageBookmarksUseCase: ManageBookmarksUseCase,
    private val manageProgressUseCase: ManageProgressUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FlashcardUiState>(FlashcardUiState.Loading)
    val uiState: StateFlow<FlashcardUiState> = _uiState.asStateFlow()

    private var currentTopicId: String? = null
    private var startIndex: Int = 0

    /**
     * Loads flashcards for a topic, optionally starting at a specific index.
     */
    fun loadFlashcards(topicId: String, startAtIndex: Int = 0) {
        currentTopicId = topicId
        startIndex = startAtIndex
        _uiState.value = FlashcardUiState.Loading

        viewModelScope.launch {
            combine(
                getTopicUseCase(topicId),
                getQuestionsUseCase(topicId),
                manageBookmarksUseCase.getBookmarkedIds()
            ) { topic, questions, bookmarkedIds ->
                if (questions.isEmpty()) {
                    FlashcardUiState.Empty
                } else {
                    FlashcardUiState.Success(
                        topic = topic,
                        questions = questions,
                        currentIndex = startAtIndex.coerceIn(0, questions.size - 1),
                        isAnswerRevealed = false,
                        bookmarkedIds = bookmarkedIds,
                        showCelebration = false
                    )
                }
            }
            .catch { e ->
                _uiState.value = FlashcardUiState.Error(e.message ?: "Unknown error occurred")
            }
            .collect { state ->
                _uiState.value = state
            }
        }
    }

    /**
     * Loads flashcards for a topic and navigates to a specific question by its ID.
     * Useful for navigation from search results.
     * @param topicId The topic ID
     * @param questionId The question ID to navigate to
     */
    fun loadFlashcardsForQuestion(topicId: String, questionId: String) {
        currentTopicId = topicId
        _uiState.value = FlashcardUiState.Loading

        viewModelScope.launch {
            combine(
                getTopicUseCase(topicId),
                getQuestionsUseCase(topicId),
                manageBookmarksUseCase.getBookmarkedIds()
            ) { topic, questions, bookmarkedIds ->
                if (questions.isEmpty()) {
                    FlashcardUiState.Empty
                } else {
                    // Find the index of the target question
                    val targetIndex = questions.indexOfFirst { it.id == questionId }
                        .coerceAtLeast(0)
                    
                    FlashcardUiState.Success(
                        topic = topic,
                        questions = questions,
                        currentIndex = targetIndex,
                        isAnswerRevealed = false,
                        bookmarkedIds = bookmarkedIds,
                        showCelebration = false
                    )
                }
            }
            .catch { e ->
                _uiState.value = FlashcardUiState.Error(e.message ?: "Unknown error occurred")
            }
            .collect { state ->
                _uiState.value = state
            }
        }
    }

    /**
     * Loads flashcards for bookmarked questions only.
     * @param startAtIndex The index to start at (defaults to 0)
     */
    fun loadBookmarkedFlashcards(startAtIndex: Int = 0) {
        currentTopicId = null
        startIndex = startAtIndex
        _uiState.value = FlashcardUiState.Loading

        viewModelScope.launch {
            combine(
                manageBookmarksUseCase.getBookmarkedQuestions(),
                manageBookmarksUseCase.getBookmarkedIds()
            ) { questions, bookmarkedIds ->
                if (questions.isEmpty()) {
                    FlashcardUiState.Empty
                } else {
                    FlashcardUiState.Success(
                        topic = null,
                        questions = questions,
                        currentIndex = startAtIndex.coerceIn(0, questions.size - 1),
                        isAnswerRevealed = false,
                        bookmarkedIds = bookmarkedIds,
                        showCelebration = false
                    )
                }
            }
            .catch { e ->
                _uiState.value = FlashcardUiState.Error(e.message ?: "Unknown error occurred")
            }
            .collect { state ->
                _uiState.value = state
            }
        }
    }

    /**
     * Moves to the next flashcard.
     */
    fun nextCard() {
        updateState { state ->
            if (state.hasNext) {
                state.copy(
                    currentIndex = state.currentIndex + 1,
                    isAnswerRevealed = false
                )
            } else state
        }
    }

    /**
     * Moves to the previous flashcard.
     */
    fun previousCard() {
        updateState { state ->
            if (state.hasPrevious) {
                state.copy(
                    currentIndex = state.currentIndex - 1,
                    isAnswerRevealed = false
                )
            } else state
        }
    }

    /**
     * Goes to a specific card index.
     */
    fun goToCard(index: Int) {
        updateState { state ->
            state.copy(
                currentIndex = index.coerceIn(0, state.questions.size - 1),
                isAnswerRevealed = false
            )
        }
    }

    /**
     * Toggles the answer reveal state.
     */
    fun toggleAnswer() {
        val currentState = _uiState.value
        if (currentState is FlashcardUiState.Success) {
            val shouldReveal = !currentState.isAnswerRevealed
            if (shouldReveal) {
                currentState.currentQuestion?.let { question ->
                    viewModelScope.launch {
                        manageProgressUseCase.markCovered(question.id)
                        maybeCelebrateCompletion(currentState)
                    }
                }
            }
            updateState { state ->
                state.copy(isAnswerRevealed = shouldReveal)
            }
        }
    }

    /**
     * Shows the answer.
     */
    fun revealAnswer() {
        val currentState = _uiState.value
        if (currentState is FlashcardUiState.Success) {
            currentState.currentQuestion?.let { question ->
                viewModelScope.launch {
                    manageProgressUseCase.markCovered(question.id)
                    maybeCelebrateCompletion(currentState)
                }
            }
            updateState { state ->
                state.copy(isAnswerRevealed = true)
            }
        }
    }

    /**
     * Marks completion celebration when the last card is covered.
     */
    private suspend fun maybeCelebrateCompletion(state: FlashcardUiState.Success) {
        val topicId = state.topic?.id ?: return
        if (state.currentIndex != state.questions.lastIndex) return
        val shouldCelebrate = manageProgressUseCase.checkAndCelebrateIfCompleted(topicId)
        if (shouldCelebrate) {
            updateState { current ->
                current.copy(showCelebration = true)
            }
        }
    }

    /**
     * Resets celebration flag after showing animation.
     */
    fun dismissCelebration() {
        updateState { state ->
            if (state.showCelebration) state.copy(showCelebration = false) else state
        }
    }

    /**
     * Hides the answer.
     */
    fun hideAnswer() {
        updateState { state ->
            state.copy(isAnswerRevealed = false)
        }
    }

    /**
     * Toggles bookmark for current question.
     */
    fun toggleCurrentBookmark() {
        val currentState = _uiState.value
        if (currentState is FlashcardUiState.Success) {
            currentState.currentQuestion?.let { question ->
                viewModelScope.launch {
                    manageBookmarksUseCase.toggleBookmark(question.id)
                }
            }
        }
    }

    /**
     * Helper to update Success state.
     */
    private inline fun updateState(transform: (FlashcardUiState.Success) -> FlashcardUiState.Success) {
        val currentState = _uiState.value
        if (currentState is FlashcardUiState.Success) {
            _uiState.value = transform(currentState)
        }
    }
}
