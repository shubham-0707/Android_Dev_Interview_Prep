package com.shubham.mobiledevinterviewprep.di

import com.shubham.mobiledevinterviewprep.data.local.SettingsFactory
import com.shubham.mobiledevinterviewprep.data.repository.AuthRepositoryImpl
import com.shubham.mobiledevinterviewprep.data.repository.BookmarkRepositoryImpl
import com.shubham.mobiledevinterviewprep.data.repository.ProgressRepositoryImpl
import com.shubham.mobiledevinterviewprep.data.repository.QuestionRepositoryImpl
import com.shubham.mobiledevinterviewprep.domain.repository.AuthRepository
import com.shubham.mobiledevinterviewprep.domain.repository.BookmarkRepository
import com.shubham.mobiledevinterviewprep.domain.repository.ProgressRepository
import com.shubham.mobiledevinterviewprep.domain.repository.QuestionRepository
import com.shubham.mobiledevinterviewprep.domain.usecase.GetQuestionsUseCase
import com.shubham.mobiledevinterviewprep.domain.usecase.GetTopicUseCase
import com.shubham.mobiledevinterviewprep.domain.usecase.GetTopicsUseCase
import com.shubham.mobiledevinterviewprep.domain.usecase.ManageAuthUseCase
import com.shubham.mobiledevinterviewprep.domain.usecase.ManageBookmarksUseCase
import com.shubham.mobiledevinterviewprep.domain.usecase.ManageProgressUseCase
import com.shubham.mobiledevinterviewprep.domain.usecase.SearchQuestionsUseCase
import com.shubham.mobiledevinterviewprep.presentation.viewmodel.LoginViewModel
import com.shubham.mobiledevinterviewprep.presentation.viewmodel.SplashViewModel
import com.shubham.mobiledevinterviewprep.presentation.viewmodel.UserViewModel
import com.shubham.mobiledevinterviewprep.presentation.viewmodel.BookmarkViewModel
import com.shubham.mobiledevinterviewprep.presentation.viewmodel.FlashcardViewModel
import com.shubham.mobiledevinterviewprep.presentation.viewmodel.HomeViewModel
import com.shubham.mobiledevinterviewprep.presentation.viewmodel.SearchViewModel
import com.shubham.mobiledevinterviewprep.presentation.viewmodel.TopicViewModel

/**
 * Simple service locator for dependency injection.
 * 
 * Architecture Decision: Using a simple service locator pattern
 * instead of a full DI framework (Koin, Hilt) for simplicity in
 * this KMP project. This can be easily replaced with Koin for
 * more complex scenarios.
 * 
 * Singletons are used for repositories to share state (bookmarks)
 * across the app. Settings is also singleton for consistent storage.
 */
object AppModule {

    // ============================================================================
    // Settings - Platform-specific persistent storage (singleton)
    // ============================================================================
    
    private val settings by lazy {
        SettingsFactory.create()
    }

    // ============================================================================
    // Repositories - Singleton instances
    // ============================================================================
    
    private val questionRepository: QuestionRepository by lazy {
        QuestionRepositoryImpl()
    }

    private val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(settings)
    }

    private val bookmarkRepository: BookmarkRepository by lazy {
        BookmarkRepositoryImpl(settings)
    }

    private val progressRepository: ProgressRepository by lazy {
        ProgressRepositoryImpl(settings)
    }

    // ============================================================================
    // Use Cases - Factory pattern (new instance each time)
    // ============================================================================
    
    private fun provideGetTopicsUseCase(): GetTopicsUseCase {
        return GetTopicsUseCase(questionRepository)
    }

    private fun provideGetTopicUseCase(): GetTopicUseCase {
        return GetTopicUseCase(questionRepository)
    }

    private fun provideGetQuestionsUseCase(): GetQuestionsUseCase {
        return GetQuestionsUseCase(questionRepository)
    }

    private fun provideSearchQuestionsUseCase(): SearchQuestionsUseCase {
        return SearchQuestionsUseCase(questionRepository)
    }

    private fun provideManageBookmarksUseCase(): ManageBookmarksUseCase {
        return ManageBookmarksUseCase(bookmarkRepository, questionRepository)
    }

    private fun provideManageAuthUseCase(): ManageAuthUseCase {
        return ManageAuthUseCase(authRepository)
    }

    private fun provideManageProgressUseCase(): ManageProgressUseCase {
        return ManageProgressUseCase(progressRepository, questionRepository)
    }

    // ============================================================================
    // ViewModels - Factory methods for creating new instances
    // ============================================================================
    
    fun provideHomeViewModel(): HomeViewModel {
        return HomeViewModel(
            getTopicsUseCase = provideGetTopicsUseCase(),
            manageBookmarksUseCase = provideManageBookmarksUseCase(),
            manageProgressUseCase = provideManageProgressUseCase()
        )
    }

    fun provideTopicViewModel(): TopicViewModel {
        return TopicViewModel(
            getTopicUseCase = provideGetTopicUseCase(),
            getQuestionsUseCase = provideGetQuestionsUseCase(),
            manageBookmarksUseCase = provideManageBookmarksUseCase(),
            manageProgressUseCase = provideManageProgressUseCase()
        )
    }

    fun provideFlashcardViewModel(): FlashcardViewModel {
        return FlashcardViewModel(
            getTopicUseCase = provideGetTopicUseCase(),
            getQuestionsUseCase = provideGetQuestionsUseCase(),
            manageBookmarksUseCase = provideManageBookmarksUseCase(),
            manageProgressUseCase = provideManageProgressUseCase()
        )
    }

    fun provideSplashViewModel(): SplashViewModel {
        return SplashViewModel(
            manageAuthUseCase = provideManageAuthUseCase()
        )
    }

    fun provideLoginViewModel(): LoginViewModel {
        return LoginViewModel(
            manageAuthUseCase = provideManageAuthUseCase()
        )
    }

    fun provideUserViewModel(): UserViewModel {
        return UserViewModel(
            manageAuthUseCase = provideManageAuthUseCase()
        )
    }

    fun provideBookmarkViewModel(): BookmarkViewModel {
        return BookmarkViewModel(
            manageBookmarksUseCase = provideManageBookmarksUseCase()
        )
    }

    fun provideSearchViewModel(): SearchViewModel {
        return SearchViewModel(
            searchQuestionsUseCase = provideSearchQuestionsUseCase(),
            manageBookmarksUseCase = provideManageBookmarksUseCase()
        )
    }
}
