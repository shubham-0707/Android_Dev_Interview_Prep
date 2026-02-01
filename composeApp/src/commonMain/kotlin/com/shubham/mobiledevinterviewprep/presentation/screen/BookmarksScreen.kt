package com.shubham.mobiledevinterviewprep.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shubham.mobiledevinterviewprep.domain.model.Question
import com.shubham.mobiledevinterviewprep.presentation.component.ArrowBackIcon
import com.shubham.mobiledevinterviewprep.presentation.component.EmptyScreen
import com.shubham.mobiledevinterviewprep.presentation.component.ErrorScreen
import com.shubham.mobiledevinterviewprep.presentation.component.LoadingScreen
import com.shubham.mobiledevinterviewprep.presentation.component.PlayIcon
import com.shubham.mobiledevinterviewprep.presentation.component.QuestionCard
import com.shubham.mobiledevinterviewprep.presentation.viewmodel.BookmarkUiState
import com.shubham.mobiledevinterviewprep.presentation.viewmodel.BookmarkViewModel

/**
 * Bookmarks Screen showing all saved questions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    viewModel: BookmarkViewModel,
    onBackClick: () -> Unit,
    onQuestionClick: (Int) -> Unit,
    onStartFlashcards: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Bookmarks",
                        fontWeight = FontWeight.Bold
                    )
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
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        when (val state = uiState) {
            is BookmarkUiState.Loading -> {
                LoadingScreen(message = "Loading bookmarks...")
            }
            is BookmarkUiState.Error -> {
                ErrorScreen(message = state.message)
            }
            is BookmarkUiState.Success -> {
                if (state.isEmpty) {
                    EmptyScreen(
                        title = "No Bookmarks",
                        message = "Questions you bookmark will appear here."
                    )
                } else {
                    BookmarksContent(
                        questions = state.questions,
                        onQuestionClick = onQuestionClick,
                        onBookmarkClick = { viewModel.removeBookmark(it) },
                        onStartFlashcards = onStartFlashcards,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}

@Composable
private fun BookmarksContent(
    questions: List<Question>,
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
            Button(
                onClick = onStartFlashcards,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                contentPadding = PaddingValues(16.dp)
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
                        text = "Study Bookmarked (${questions.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Bookmarks count header
        item(key = "bookmarks_header") {
            Text(
                text = "${questions.size} Bookmarked Questions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Question cards (all are bookmarked)
        itemsIndexed(
            items = questions,
            key = { _, question -> question.id }
        ) { index, question ->
            QuestionCard(
                question = question,
                isBookmarked = true, // All questions here are bookmarked
                onClick = { onQuestionClick(index) },
                onBookmarkClick = { onBookmarkClick(question.id) }
            )
        }
    }
}
