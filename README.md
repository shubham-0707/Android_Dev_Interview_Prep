# Interview Preparation App

A production-ready **Kotlin Multiplatform** interview preparation application built with **Compose Multiplatform** for Android and iOS.

## Features

- **Topic-based Learning**: Questions organized by categories (DSA, Android, Kotlin)
- **Flashcard Mode**: Swipe-based card navigation with tap-to-reveal answers
- **Bookmark System**: Save important questions for later review
- **Search**: Find questions across all topics
- **100% Offline**: No internet required, all data stored locally

## Screenshots

| Home | Topic | Flashcard |
|------|-------|-----------|
| Topic categories | Question list | Swipe cards |

## Architecture

### Clean Architecture with MVVM

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │   Screens   │  │  ViewModels │  │     Components      │ │
│  │  (Compose)  │←─│ (StateFlow) │←─│  (Reusable UI)      │ │
│  └─────────────┘  └─────────────┘  └─────────────────────┘ │
└────────────────────────────┬────────────────────────────────┘
                             │
┌────────────────────────────┴────────────────────────────────┐
│                      Domain Layer                            │
│  ┌─────────────┐  ┌─────────────────────────────────────┐  │
│  │   Models    │  │             Use Cases               │  │
│  │ (Entities)  │  │  (Business Logic Encapsulation)     │  │
│  └─────────────┘  └─────────────────────────────────────┘  │
└────────────────────────────┬────────────────────────────────┘
                             │
┌────────────────────────────┴────────────────────────────────┐
│                       Data Layer                             │
│  ┌─────────────────────┐  ┌─────────────────────────────┐  │
│  │   Repositories      │  │        Data Sources         │  │
│  │  (Implementations)  │←─│   (In-Memory/SampleData)    │  │
│  └─────────────────────┘  └─────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### Project Structure

```
composeApp/src/commonMain/kotlin/com/shubham/mobiledevinterviewprep/
├── di/                          # Dependency Injection
│   └── AppModule.kt             # Service Locator
├── domain/                      # Domain Layer
│   ├── model/                   # Data Models
│   │   ├── Question.kt
│   │   ├── Topic.kt
│   │   ├── TopicCategory.kt
│   │   ├── Difficulty.kt
│   │   └── Bookmark.kt
│   ├── repository/              # Repository Interfaces
│   │   ├── QuestionRepository.kt
│   │   └── BookmarkRepository.kt
│   └── usecase/                 # Use Cases
│       ├── GetTopicsUseCase.kt
│       ├── GetQuestionsUseCase.kt
│       ├── SearchQuestionsUseCase.kt
│       └── ManageBookmarksUseCase.kt
├── data/                        # Data Layer
│   ├── datasource/
│   │   └── SampleData.kt        # 55 Interview Questions
│   └── repository/
│       ├── QuestionRepositoryImpl.kt
│       └── BookmarkRepositoryImpl.kt
└── presentation/                # Presentation Layer
    ├── navigation/
    │   └── Screen.kt            # Type-safe Navigation Routes
    ├── theme/
    │   └── AppTheme.kt          # Material 3 Theme
    ├── component/               # Reusable UI Components
    │   ├── QuestionCard.kt
    │   ├── TopicCard.kt
    │   ├── LoadingIndicator.kt
    │   └── Icons.kt
    ├── viewmodel/               # ViewModels with StateFlow
    │   ├── HomeViewModel.kt
    │   ├── TopicViewModel.kt
    │   ├── FlashcardViewModel.kt
    │   ├── BookmarkViewModel.kt
    │   └── SearchViewModel.kt
    └── screen/                  # Screen Composables
        ├── HomeScreen.kt
        ├── TopicScreen.kt
        ├── FlashcardScreen.kt
        ├── BookmarksScreen.kt
        └── SearchScreen.kt
```

## Tech Stack

| Technology | Purpose |
|------------|---------|
| Kotlin Multiplatform | Shared business logic |
| Compose Multiplatform | Cross-platform UI |
| Kotlin Coroutines | Asynchronous programming |
| Kotlin Flow/StateFlow | Reactive state management |
| Navigation Compose | Type-safe navigation |
| Kotlinx Serialization | JSON serialization |
| Material 3 | Modern design system |

## Topics & Questions

### Data Structures & Algorithms (22 questions)
- **Arrays**: Time complexity, two-pointer technique, Kadane's algorithm
- **Strings**: Palindrome, KMP, anagrams, LCS
- **Trees**: Traversals, height, LCA, serialization
- **Graphs**: Representations, cycle detection, Dijkstra, topological sort
- **Dynamic Programming**: Knapsack, LIS, coin change, edit distance

### Android Development (20 questions)
- **Activity**: Lifecycle, launch modes, data passing
- **ViewModel**: SharedFlow, ViewModelScope, SavedStateHandle
- **Jetpack Compose**: Recomposition, remember, side effects, state hoisting
- **Lifecycle**: LifecycleOwner, process death, repeatOnLifecycle

### Kotlin Language (15 questions)
- **Coroutines**: Dispatchers, launch vs async, structured concurrency
- **Flow**: StateFlow vs SharedFlow, exception handling, operators
- **Sealed Classes**: UI state modeling, exhaustive when
- **Data Classes**: copy(), componentN(), limitations

## Building & Running

### Prerequisites
- Android Studio Ladybug or later
- Xcode 15+ (for iOS)
- JDK 11+

### Android
```bash
./gradlew :composeApp:assembleDebug
```

### iOS
Open `iosApp/iosApp.xcodeproj` in Xcode and run.

## Architecture Decisions

### 1. Shared ViewModels in KMP
ViewModels live in the shared module using `androidx.lifecycle.viewmodel` for KMP. This enables:
- Single source of truth for business logic
- Consistent behavior across platforms
- Easier testing

### 2. StateFlow for UI State
Using `StateFlow` instead of `LiveData` because:
- Part of Kotlin Coroutines (works in KMP)
- Type-safe, null-safe
- Built-in operators for transformation

### 3. Service Locator Pattern
Using a simple `AppModule` object instead of Koin/Hilt for:
- Simplicity in KMP context
- No additional dependencies
- Easy to replace with DI framework later

### 4. In-Memory Data Storage
Using in-memory storage for offline-first design:
- Fast access, no I/O latency
- Can be easily replaced with SQLDelight for persistence
- Bookmarks use `StateFlow` for reactive updates

### 5. Type-Safe Navigation
Using Kotlin Serialization with Navigation Compose:
- Compile-time route checking
- Type-safe arguments
- Clean navigation API

## Future Improvements

- [ ] Add SQLDelight for persistent storage
- [ ] Implement spaced repetition algorithm
- [ ] Add progress tracking and statistics
- [ ] Support custom question sets
- [ ] Dark/Light theme toggle
- [ ] Question difficulty filtering
- [ ] Export/Import bookmarks

## License

MIT License - See LICENSE file for details.
