package com.shubham.mobiledevinterviewprep.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.shubham.mobiledevinterviewprep.domain.model.Question
import com.shubham.mobiledevinterviewprep.presentation.component.ArrowBackIcon
import com.shubham.mobiledevinterviewprep.presentation.component.CloseIcon
import com.shubham.mobiledevinterviewprep.presentation.component.EmptyScreen
import com.shubham.mobiledevinterviewprep.presentation.component.ErrorScreen
import com.shubham.mobiledevinterviewprep.presentation.component.LoadingScreen
import com.shubham.mobiledevinterviewprep.presentation.component.QuestionCard
import com.shubham.mobiledevinterviewprep.presentation.component.SearchIcon
import com.shubham.mobiledevinterviewprep.presentation.viewmodel.SearchUiState
import com.shubham.mobiledevinterviewprep.presentation.viewmodel.SearchViewModel

/**
 * Search Screen for finding questions across all topics.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onBackClick: () -> Unit,
    onQuestionClick: (Question) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Auto-focus search field when screen opens
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.06f),
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
                        Text(
                            text = "Search",
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
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
                    )
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Search field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .focusRequester(focusRequester),
                    placeholder = {
                        Text(text = "Search questions, answers, or tags...")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = SearchIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.clearSearch() }) {
                                Icon(
                                    imageVector = CloseIcon,
                                    contentDescription = "Clear search",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                        }
                    )
                )

                // Search results
                when (val state = uiState) {
                    is SearchUiState.Idle -> {
                        EmptyScreen(
                            title = "Search Questions",
                            message = "Type to search across all topics"
                        )
                    }
                    is SearchUiState.Loading -> {
                        LoadingScreen(message = "Searching...")
                    }
                    is SearchUiState.Error -> {
                        ErrorScreen(message = state.message)
                    }
                    is SearchUiState.Success -> {
                        if (state.isEmpty) {
                            EmptyScreen(
                                title = "No Results",
                                message = "No questions found for \"${state.query}\""
                            )
                        } else {
                            SearchResults(
                                query = state.query,
                                results = state.results,
                                bookmarkedIds = state.bookmarkedIds,
                                onQuestionClick = onQuestionClick,
                                onBookmarkClick = { viewModel.toggleBookmark(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResults(
    query: String,
    results: List<Question>,
    bookmarkedIds: Set<String>,
    onQuestionClick: (Question) -> Unit,
    onBookmarkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Results count header
        item(key = "results_header") {
            Text(
                text = "${results.size} results for \"$query\"",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Question cards
        itemsIndexed(
            items = results,
            key = { _, question -> question.id }
        ) { _, question ->
            QuestionCard(
                question = question,
                isBookmarked = question.id in bookmarkedIds,
                onClick = { onQuestionClick(question) },
                onBookmarkClick = { onBookmarkClick(question.id) }
            )
        }
    }
}
