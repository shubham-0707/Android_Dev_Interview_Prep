package com.shubham.mobiledevinterviewprep

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.shubham.mobiledevinterviewprep.di.AppModule
import com.shubham.mobiledevinterviewprep.presentation.navigation.Screen
import com.shubham.mobiledevinterviewprep.presentation.screen.BookmarksScreen
import com.shubham.mobiledevinterviewprep.presentation.screen.FlashcardScreen
import com.shubham.mobiledevinterviewprep.presentation.screen.HomeScreen
import com.shubham.mobiledevinterviewprep.presentation.screen.SearchScreen
import com.shubham.mobiledevinterviewprep.presentation.screen.TopicScreen
import com.shubham.mobiledevinterviewprep.presentation.theme.InterviewPrepTheme

/**
 * Main App composable with navigation setup.
 * 
 * Architecture Decision: Using a single NavHost for type-safe navigation.
 * ViewModels are created via AppModule (service locator pattern) for
 * simplicity in KMP. In production, consider Koin for more robust DI.
 * 
 * Navigation flow:
 * Home -> Topic -> Flashcard
 * Home -> Search
 * Home -> Bookmarks -> Flashcard (bookmarks mode)
 */
@Composable
fun App() {
    InterviewPrepTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
        ) {
            val navController = rememberNavController()

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route ?: "home"

            AnimatedContent(
                targetState = currentRoute,
                transitionSpec = {
                    val slideSpec = tween<IntOffset>(durationMillis = 280)
                    val fadeInSpec = tween<Float>(durationMillis = 220)
                    val fadeOutSpec = tween<Float>(durationMillis = 180)
                    (slideInHorizontally(animationSpec = slideSpec, initialOffsetX = { it / 6 }) + fadeIn(
                        animationSpec = fadeInSpec
                    )) togetherWith (slideOutHorizontally(animationSpec = slideSpec, targetOffsetX = { -it / 6 }) + fadeOut(
                        animationSpec = fadeOutSpec
                    ))
                },
                label = "screen_transition"
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home
                ) {
                    // Home Screen
                    composable<Screen.Home> {
                        val viewModel = remember { AppModule.provideHomeViewModel() }
                        HomeScreen(
                            viewModel = viewModel,
                            onTopicClick = { topicId ->
                                navController.navigate(Screen.Topic(topicId))
                            },
                            onSearchClick = {
                                navController.navigate(Screen.Search)
                            },
                            onBookmarksClick = {
                                navController.navigate(Screen.Bookmarks)
                            }
                        )
                    }
                    
                    // Topic Screen
                    composable<Screen.Topic> { backStackEntry ->
                        val route = backStackEntry.toRoute<Screen.Topic>()
                        val viewModel = remember { AppModule.provideTopicViewModel() }
                        TopicScreen(
                            topicId = route.topicId,
                            viewModel = viewModel,
                            onBackClick = { navController.popBackStack() },
                            onQuestionClick = { index ->
                                navController.navigate(
                                    Screen.Flashcard(
                                        topicId = route.topicId,
                                        startIndex = index,
                                        bookmarksOnly = false
                                    )
                                )
                            },
                            onStartFlashcards = {
                                navController.navigate(
                                    Screen.Flashcard(
                                        topicId = route.topicId,
                                        startIndex = 0,
                                        bookmarksOnly = false
                                    )
                                )
                            }
                        )
                    }
                    
                    // Flashcard Screen
                    composable<Screen.Flashcard> { backStackEntry ->
                        val route = backStackEntry.toRoute<Screen.Flashcard>()
                        val viewModel = remember { AppModule.provideFlashcardViewModel() }
                        FlashcardScreen(
                            topicId = route.topicId,
                            startIndex = route.startIndex,
                            bookmarksOnly = route.bookmarksOnly,
                            questionId = route.questionId,
                            viewModel = viewModel,
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                    
                    // Bookmarks Screen
                    composable<Screen.Bookmarks> {
                        val viewModel = remember { AppModule.provideBookmarkViewModel() }
                        BookmarksScreen(
                            viewModel = viewModel,
                            onBackClick = { navController.popBackStack() },
                            onQuestionClick = { index ->
                                navController.navigate(
                                    Screen.Flashcard(
                                        topicId = null,
                                        startIndex = index,
                                        bookmarksOnly = true
                                    )
                                )
                            },
                            onStartFlashcards = {
                                navController.navigate(
                                    Screen.Flashcard(
                                        topicId = null,
                                        startIndex = 0,
                                        bookmarksOnly = true
                                    )
                                )
                            }
                        )
                    }
                    
                    // Search Screen
                    composable<Screen.Search> {
                        val viewModel = remember { AppModule.provideSearchViewModel() }
                        SearchScreen(
                            viewModel = viewModel,
                            onBackClick = { navController.popBackStack() },
                            onQuestionClick = { question ->
                                // Navigate to flashcard showing this specific question
                                navController.navigate(
                                    Screen.Flashcard(
                                        topicId = question.topicId,
                                        startIndex = 0,
                                        bookmarksOnly = false,
                                        questionId = question.id // Navigate directly to the searched question
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}