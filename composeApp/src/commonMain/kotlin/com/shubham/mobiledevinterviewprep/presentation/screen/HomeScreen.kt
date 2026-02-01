package com.shubham.mobiledevinterviewprep.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shubham.mobiledevinterviewprep.domain.model.Topic
import com.shubham.mobiledevinterviewprep.domain.model.TopicCategory
import com.shubham.mobiledevinterviewprep.presentation.component.BookmarkFilledIcon
import com.shubham.mobiledevinterviewprep.presentation.component.BookmarkOutlineIcon
import com.shubham.mobiledevinterviewprep.presentation.component.ErrorScreen
import com.shubham.mobiledevinterviewprep.presentation.component.LoadingScreen
import com.shubham.mobiledevinterviewprep.presentation.component.SearchIcon
import com.shubham.mobiledevinterviewprep.presentation.component.TopicCard
import com.shubham.mobiledevinterviewprep.presentation.viewmodel.HomeUiState
import com.shubham.mobiledevinterviewprep.presentation.viewmodel.HomeViewModel

/**
 * Home Screen showing topics grouped by category.
 * 
 * Architecture Decision: Screen composables receive ViewModel as parameter
 * for easier testing. Navigation callbacks are passed as lambdas for
 * loose coupling with navigation implementation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onTopicClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onBookmarksClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Interview Prep",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Master your next interview",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = SearchIcon,
                            contentDescription = "Search questions",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    
                    val bookmarkCount = (uiState as? HomeUiState.Success)?.bookmarkCount ?: 0
                    IconButton(onClick = onBookmarksClick) {
                        if (bookmarkCount > 0) {
                            BadgedBox(
                                badge = {
                                    Badge {
                                        Text(bookmarkCount.toString())
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = BookmarkFilledIcon,
                                    contentDescription = "Bookmarks ($bookmarkCount)",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        } else {
                            Icon(
                                imageVector = BookmarkOutlineIcon,
                                contentDescription = "Bookmarks",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        when (val state = uiState) {
            is HomeUiState.Loading -> {
                LoadingScreen(message = "Loading topics...")
            }
            is HomeUiState.Error -> {
                ErrorScreen(message = state.message)
            }
            is HomeUiState.Success -> {
                HomeContent(
                    topicsByCategory = state.topicsByCategory,
                    onTopicClick = onTopicClick,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun HomeContent(
    topicsByCategory: Map<TopicCategory, List<Topic>>,
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Iterate through categories in defined order
        TopicCategory.entries.forEach { category ->
            val topics = topicsByCategory[category] ?: emptyList()
            if (topics.isNotEmpty()) {
                item(key = "header_${category.name}") {
                    CategoryHeader(category = category)
                }
                
                items(
                    items = topics,
                    key = { it.id }
                ) { topic ->
                    TopicCard(
                        topic = topic,
                        onClick = { onTopicClick(topic.id) }
                    )
                }
                
                item(key = "spacer_${category.name}") {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun CategoryHeader(
    category: TopicCategory,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = category.icon(),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = category.displayName(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
