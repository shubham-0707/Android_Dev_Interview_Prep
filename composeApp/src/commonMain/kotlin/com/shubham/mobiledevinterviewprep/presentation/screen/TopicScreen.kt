package com.shubham.mobiledevinterviewprep.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shubham.mobiledevinterviewprep.domain.model.Question
import com.shubham.mobiledevinterviewprep.presentation.component.ArrowBackIcon
import com.shubham.mobiledevinterviewprep.presentation.component.ErrorScreen
import com.shubham.mobiledevinterviewprep.presentation.component.LoadingScreen
import com.shubham.mobiledevinterviewprep.presentation.component.PlayIcon
import com.shubham.mobiledevinterviewprep.presentation.component.QuestionCard
import com.shubham.mobiledevinterviewprep.presentation.viewmodel.TopicUiState
import com.shubham.mobiledevinterviewprep.presentation.viewmodel.TopicViewModel

/**
 * Topic Screen showing list of questions for a specific topic.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicScreen(
    topicId: String,
    viewModel: TopicViewModel,
    onBackClick: () -> Unit,
    onQuestionClick: (Int) -> Unit,
    onStartFlashcards: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Load topic when screen is first displayed
    LaunchedEffect(topicId) {
        viewModel.loadTopic(topicId)
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
                            is TopicUiState.Success -> {
                                Text(
                                    text = state.topic.name,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            else -> {
                                Text(text = "Topic")
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
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
                    )
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            when (val state = uiState) {
                is TopicUiState.Loading -> {
                    LoadingScreen(message = "Loading questions...")
                }
                is TopicUiState.Error -> {
                    ErrorScreen(message = state.message)
                }
                is TopicUiState.Success -> {
                    TopicContent(
                        questions = state.questions,
                        bookmarkedIds = state.bookmarkedIds,
                    coveredCount = state.coveredCount,
                        onQuestionClick = onQuestionClick,
                        onBookmarkClick = { viewModel.toggleBookmark(it) },
                        onStartFlashcards = onStartFlashcards,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}

@Composable
private fun TopicContent(
    questions: List<Question>,
    bookmarkedIds: Set<String>,
    coveredCount: Int,
    onQuestionClick: (Int) -> Unit,
    onBookmarkClick: (String) -> Unit,
    onStartFlashcards: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Start Flashcard mode button
        item(key = "start_flashcards") {
            StartFlashcardsButton(
                questionCount = questions.size,
                onClick = onStartFlashcards
            )
        }

        // Questions count header
        item(key = "questions_header") {
            Text(
                text = "${questions.size} Questions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Progress summary
        item(key = "progress_summary") {
            val progress = if (questions.isEmpty()) 0f else coveredCount.toFloat() / questions.size
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "$coveredCount / ${questions.size} covered",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }

        // Question cards
        itemsIndexed(
            items = questions,
            key = { _, question -> question.id }
        ) { index, question ->
            QuestionCard(
                question = question,
                isBookmarked = question.id in bookmarkedIds,
                onClick = { onQuestionClick(index) },
                onBookmarkClick = { onBookmarkClick(question.id) }
            )
        }
    }
}

@Composable
private fun StartFlashcardsButton(
    questionCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = PlayIcon,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Start Flashcards ($questionCount)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
