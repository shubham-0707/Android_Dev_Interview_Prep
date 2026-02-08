package com.shubham.mobiledevinterviewprep.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shubham.mobiledevinterviewprep.domain.model.Question
import com.shubham.mobiledevinterviewprep.presentation.component.ArrowBackIcon
import com.shubham.mobiledevinterviewprep.presentation.component.BookmarkFilledIcon
import com.shubham.mobiledevinterviewprep.presentation.component.BookmarkOutlineIcon
import com.shubham.mobiledevinterviewprep.presentation.component.DifficultyBadge
import com.shubham.mobiledevinterviewprep.presentation.component.EmptyScreen
import com.shubham.mobiledevinterviewprep.presentation.component.ErrorScreen
import com.shubham.mobiledevinterviewprep.presentation.component.LoadingScreen
import com.shubham.mobiledevinterviewprep.presentation.viewmodel.FlashcardUiState
import com.shubham.mobiledevinterviewprep.presentation.viewmodel.FlashcardViewModel
import kotlin.math.absoluteValue

/**
 * Flashcard Screen with swipe-based navigation.
 * 
 * Architecture Decision: Using gesture detection for swipe navigation
 * provides a natural, touch-friendly experience. The card flip animation
 * gives visual feedback when revealing answers.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreen(
    topicId: String?,
    startIndex: Int,
    bookmarksOnly: Boolean,
    questionId: String?,
    viewModel: FlashcardViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Load flashcards based on mode
    LaunchedEffect(topicId, bookmarksOnly, startIndex, questionId) {
        when {
            bookmarksOnly -> {
                viewModel.loadBookmarkedFlashcards(startIndex)
            }
            topicId != null && questionId != null -> {
                // Navigate to specific question (from search results)
                viewModel.loadFlashcardsForQuestion(topicId, questionId)
            }
            topicId != null -> {
                viewModel.loadFlashcards(topicId, startIndex)
            }
        }
    }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.06f),
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.background
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        when (val state = uiState) {
                            is FlashcardUiState.Success -> {
                                Column {
                                    Text(
                                        text = if (bookmarksOnly) "Bookmarked" else state.topic?.name ?: "Flashcards",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = state.progressText,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            else -> {
                                Text(text = "Flashcards")
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = ArrowBackIcon,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        val state = uiState
                        if (state is FlashcardUiState.Success) {
                            IconButton(onClick = { viewModel.toggleCurrentBookmark() }) {
                                Icon(
                                    imageVector = if (state.isCurrentBookmarked) {
                                        BookmarkFilledIcon
                                    } else {
                                        BookmarkOutlineIcon
                                    },
                                    contentDescription = "Toggle bookmark",
                                    tint = if (state.isCurrentBookmarked) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
                    )
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            when (val state = uiState) {
                is FlashcardUiState.Loading -> {
                    LoadingScreen(message = "Loading flashcards...")
                }
                is FlashcardUiState.Error -> {
                    ErrorScreen(message = state.message)
                }
                is FlashcardUiState.Empty -> {
                    EmptyScreen(
                        title = "No Questions",
                        message = if (bookmarksOnly) {
                            "You haven't bookmarked any questions yet."
                        } else {
                            "No questions available for this topic."
                        }
                    )
                }
                is FlashcardUiState.Success -> {
                    FlashcardContent(
                        state = state,
                        onSwipeLeft = { viewModel.nextCard() },
                        onSwipeRight = { viewModel.previousCard() },
                        onCardClick = { viewModel.toggleAnswer() },
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}

@Composable
private fun FlashcardContent(
    state: FlashcardUiState.Success,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var dragOffset by remember { mutableFloatStateOf(0f) }
    val swipeThreshold = 100f

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Progress indicator
        LinearProgressIndicator(
            progress = { state.progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Flashcard with swipe gesture
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .pointerInput(state.currentIndex) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (dragOffset < -swipeThreshold && state.hasNext) {
                                onSwipeLeft()
                            } else if (dragOffset > swipeThreshold && state.hasPrevious) {
                                onSwipeRight()
                            }
                            dragOffset = 0f
                        },
                        onDragCancel = {
                            dragOffset = 0f
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            dragOffset += dragAmount
                        }
                    )
                }
        ) {
            state.currentQuestion?.let { question ->
                FlashcardCard(
                    question = question,
                    isAnswerRevealed = state.isAnswerRevealed,
                    onClick = onCardClick,
                    dragOffset = dragOffset,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation hint
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (state.hasPrevious) {
                Text(
                    text = "← Swipe right for previous",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (state.hasPrevious && state.hasNext) {
                Spacer(modifier = Modifier.width(16.dp))
            }
            if (state.hasNext) {
                Text(
                    text = "Swipe left for next →",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tap hint
        Text(
            text = if (state.isAnswerRevealed) "Tap card to hide answer" else "Tap card to reveal answer",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FlashcardCard(
    question: Question,
    isAnswerRevealed: Boolean,
    onClick: () -> Unit,
    dragOffset: Float,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = dragOffset / 20f,
        animationSpec = tween(durationMillis = 50),
        label = "rotation"
    )

    Card(
        modifier = modifier
            .graphicsLayer {
                rotationZ = rotation.coerceIn(-15f, 15f)
                translationX = dragOffset
            }
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.28f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header with difficulty
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DifficultyBadge(difficulty = question.difficulty)
                
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isAnswerRevealed) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.primaryContainer
                    }
                ) {
                    Text(
                        text = if (isAnswerRevealed) "Answer" else "Question",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isAnswerRevealed) {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        } else {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Question text (always visible)
            Text(
                text = question.questionText,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Answer (animated visibility)
            AnimatedVisibility(
                visible = isAnswerRevealed,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                Column {
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = question.answerText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.5
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Tags at bottom
            if (question.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    question.tags.take(4).forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = tag,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
