package com.shubham.mobiledevinterviewprep.data.datasource

import com.shubham.mobiledevinterviewprep.domain.model.Difficulty
import com.shubham.mobiledevinterviewprep.domain.model.Question
import com.shubham.mobiledevinterviewprep.domain.model.Topic
import com.shubham.mobiledevinterviewprep.domain.model.TopicCategory

/**
 * Sample data provider for the Interview Preparation App.
 * Contains 50+ questions across DSA, Android, and Kotlin topics.
 * 
 * Architecture Decision: Using in-memory data for offline-first design.
 * This can be easily replaced with a local database (SQLDelight) for production.
 */
object SampleData {

    // ============================================================================
    // TOPICS
    // ============================================================================
    
    val topics: List<Topic> = listOf(
        // DSA Topics
        Topic(
            id = "dsa_arrays",
            name = "Arrays",
            category = TopicCategory.DSA,
            description = "Linear data structure with contiguous memory allocation"
        ),
        Topic(
            id = "dsa_strings",
            name = "Strings",
            category = TopicCategory.DSA,
            description = "Character sequences and string manipulation algorithms"
        ),
        Topic(
            id = "dsa_trees",
            name = "Trees",
            category = TopicCategory.DSA,
            description = "Hierarchical data structures including BST, AVL, and more"
        ),
        Topic(
            id = "dsa_graphs",
            name = "Graphs",
            category = TopicCategory.DSA,
            description = "Network structures with nodes and edges"
        ),
        Topic(
            id = "dsa_dp",
            name = "Dynamic Programming",
            category = TopicCategory.DSA,
            description = "Optimization technique using memoization and tabulation"
        ),
        
        // Android Topics
        Topic(
            id = "android_activity",
            name = "Activity",
            category = TopicCategory.ANDROID,
            description = "Entry point for user interaction in Android apps"
        ),
        Topic(
            id = "android_viewmodel",
            name = "ViewModel",
            category = TopicCategory.ANDROID,
            description = "Lifecycle-aware component for managing UI-related data"
        ),
        Topic(
            id = "android_compose",
            name = "Jetpack Compose",
            category = TopicCategory.ANDROID,
            description = "Modern declarative UI toolkit for Android"
        ),
        Topic(
            id = "android_lifecycle",
            name = "Lifecycle",
            category = TopicCategory.ANDROID,
            description = "Android component lifecycle management"
        ),
        
        // Kotlin Topics
        Topic(
            id = "kotlin_coroutines",
            name = "Coroutines",
            category = TopicCategory.KOTLIN,
            description = "Lightweight threads for asynchronous programming"
        ),
        Topic(
            id = "kotlin_flow",
            name = "Flow",
            category = TopicCategory.KOTLIN,
            description = "Cold asynchronous stream of values"
        ),
        Topic(
            id = "kotlin_sealed",
            name = "Sealed Classes",
            category = TopicCategory.KOTLIN,
            description = "Restricted class hierarchies for type safety"
        ),
        Topic(
            id = "kotlin_data",
            name = "Data Classes",
            category = TopicCategory.KOTLIN,
            description = "Classes designed to hold data with auto-generated functions"
        )
    )

    // ============================================================================
    // QUESTIONS - DSA: Arrays
    // ============================================================================
    
    private val arrayQuestions = listOf(
        Question(
            id = "arr_001",
            topicId = "dsa_arrays",
            questionText = "What is the time complexity of accessing an element in an array by index?",
            answerText = "O(1) - Constant time. Arrays provide direct memory access through index calculation: base_address + (index × element_size).",
            difficulty = Difficulty.EASY,
            tags = listOf("arrays", "time-complexity", "basics")
        ),
        Question(
            id = "arr_002",
            topicId = "dsa_arrays",
            questionText = "How do you find the second largest element in an array?",
            answerText = "Traverse once maintaining two variables: largest and secondLargest. For each element, update secondLargest if element > secondLargest but < largest, or update both if element > largest. Time: O(n), Space: O(1).",
            difficulty = Difficulty.EASY,
            tags = listOf("arrays", "traversal", "optimization")
        ),
        Question(
            id = "arr_003",
            topicId = "dsa_arrays",
            questionText = "Explain the two-pointer technique in arrays.",
            answerText = "Two-pointer technique uses two indices to traverse an array, typically from both ends or at different speeds. Common uses: finding pairs with target sum in sorted array, removing duplicates in-place, and the Dutch National Flag problem. Reduces O(n²) to O(n) in many cases.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("arrays", "two-pointers", "technique")
        ),
        Question(
            id = "arr_004",
            topicId = "dsa_arrays",
            questionText = "What is Kadane's Algorithm and when is it used?",
            answerText = "Kadane's Algorithm finds the maximum sum contiguous subarray in O(n) time. Maintain currentMax and globalMax. For each element: currentMax = max(element, currentMax + element), globalMax = max(globalMax, currentMax). Used in stock profit problems, max subarray variants.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("arrays", "dynamic-programming", "kadane")
        ),
        Question(
            id = "arr_005",
            topicId = "dsa_arrays",
            questionText = "How do you rotate an array by k positions efficiently?",
            answerText = "Reverse approach: 1) Reverse entire array, 2) Reverse first k elements, 3) Reverse remaining n-k elements. Time: O(n), Space: O(1). Example: [1,2,3,4,5], k=2 → [5,4,3,2,1] → [4,5,3,2,1] → [4,5,1,2,3]",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("arrays", "rotation", "in-place")
        ),
        Question(
            id = "arr_006",
            topicId = "dsa_arrays",
            questionText = "What is the difference between static and dynamic arrays?",
            answerText = "Static arrays have fixed size determined at compile time with stack allocation. Dynamic arrays (ArrayList, Vector) can resize at runtime, typically doubling capacity when full, with heap allocation. Dynamic arrays have amortized O(1) insertion but O(n) worst case when resizing.",
            difficulty = Difficulty.EASY,
            tags = listOf("arrays", "memory", "basics")
        )
    )

    // ============================================================================
    // QUESTIONS - DSA: Strings
    // ============================================================================
    
    private val stringQuestions = listOf(
        Question(
            id = "str_001",
            topicId = "dsa_strings",
            questionText = "How do you check if a string is a palindrome?",
            answerText = "Use two pointers from start and end, compare characters while ignoring non-alphanumeric. Move pointers inward until they meet. Time: O(n), Space: O(1). Alternatively, compare string with its reverse (uses O(n) space).",
            difficulty = Difficulty.EASY,
            tags = listOf("strings", "two-pointers", "palindrome")
        ),
        Question(
            id = "str_002",
            topicId = "dsa_strings",
            questionText = "Explain the KMP (Knuth-Morris-Pratt) pattern matching algorithm.",
            answerText = "KMP preprocesses the pattern to create a failure function (LPS array) showing longest proper prefix which is also suffix. During matching, on mismatch, uses LPS to skip comparisons. Time: O(n+m), Space: O(m). Avoids re-comparing matched characters.",
            difficulty = Difficulty.HARD,
            tags = listOf("strings", "pattern-matching", "kmp")
        ),
        Question(
            id = "str_003",
            topicId = "dsa_strings",
            questionText = "How do you find all anagrams of a pattern in a string?",
            answerText = "Sliding window with character frequency map. Create frequency map of pattern, slide window of pattern length over string, maintain window frequency. When frequencies match, record start index. Time: O(n), Space: O(1) since alphabet is constant.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("strings", "sliding-window", "anagram")
        ),
        Question(
            id = "str_004",
            topicId = "dsa_strings",
            questionText = "What is the longest common subsequence problem?",
            answerText = "Find longest sequence present in both strings (not necessarily contiguous). Use DP: if chars match, LCS[i][j] = 1 + LCS[i-1][j-1]; else LCS[i][j] = max(LCS[i-1][j], LCS[i][j-1]). Time: O(mn), Space: O(mn) or O(min(m,n)) optimized.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("strings", "dynamic-programming", "lcs")
        ),
        Question(
            id = "str_005",
            topicId = "dsa_strings",
            questionText = "How do you implement string compression?",
            answerText = "Count consecutive characters, write char + count if count > 1. Use StringBuilder for efficiency. Example: 'aabcccccaaa' → 'a2bc5a3'. Return original if compressed isn't shorter. Time: O(n), Space: O(n) for result.",
            difficulty = Difficulty.EASY,
            tags = listOf("strings", "compression", "basics")
        )
    )

    // ============================================================================
    // QUESTIONS - DSA: Trees
    // ============================================================================
    
    private val treeQuestions = listOf(
        Question(
            id = "tree_001",
            topicId = "dsa_trees",
            questionText = "What are the different types of binary tree traversals?",
            answerText = "1) Inorder (Left-Root-Right): gives sorted order in BST. 2) Preorder (Root-Left-Right): used for copying trees. 3) Postorder (Left-Right-Root): used for deletion. 4) Level order (BFS): uses queue. All DFS traversals: O(n) time, O(h) space for recursion.",
            difficulty = Difficulty.EASY,
            tags = listOf("trees", "traversal", "basics")
        ),
        Question(
            id = "tree_002",
            topicId = "dsa_trees",
            questionText = "How do you find the height of a binary tree?",
            answerText = "Recursively: height = 1 + max(height(left), height(right)), base case: null node returns -1 (or 0 for depth). Iteratively: use level order traversal, count levels. Time: O(n), Space: O(h) recursive or O(w) iterative where w is max width.",
            difficulty = Difficulty.EASY,
            tags = listOf("trees", "recursion", "height")
        ),
        Question(
            id = "tree_003",
            topicId = "dsa_trees",
            questionText = "What is a balanced binary tree and why does it matter?",
            answerText = "Balanced tree: height difference between left and right subtrees ≤ 1 for all nodes. Ensures O(log n) operations. Unbalanced trees can degrade to O(n) like linked lists. AVL and Red-Black trees are self-balancing BSTs.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("trees", "balanced", "optimization")
        ),
        Question(
            id = "tree_004",
            topicId = "dsa_trees",
            questionText = "How do you find the Lowest Common Ancestor (LCA) in a binary tree?",
            answerText = "Recursive approach: if root is null or matches either node, return root. Recursively find LCA in left and right subtrees. If both return non-null, root is LCA. If one is null, return the other. Time: O(n), Space: O(h).",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("trees", "lca", "recursion")
        ),
        Question(
            id = "tree_005",
            topicId = "dsa_trees",
            questionText = "Explain the difference between BFS and DFS in trees.",
            answerText = "BFS: Level-order, uses queue, explores neighbors first, good for shortest path, O(w) space. DFS: Depth-first using stack/recursion, explores branch completely, O(h) space. BFS better for wide trees, DFS for deep trees.",
            difficulty = Difficulty.EASY,
            tags = listOf("trees", "bfs", "dfs")
        ),
        Question(
            id = "tree_006",
            topicId = "dsa_trees",
            questionText = "How do you serialize and deserialize a binary tree?",
            answerText = "Preorder traversal with null markers. Serialize: visit root, record value (or 'null'), recurse left then right. Deserialize: read values, create node, recursively build left and right. Use queue or index pointer for deserialization. Time: O(n).",
            difficulty = Difficulty.HARD,
            tags = listOf("trees", "serialization", "design")
        )
    )

    // ============================================================================
    // QUESTIONS - DSA: Graphs
    // ============================================================================
    
    private val graphQuestions = listOf(
        Question(
            id = "graph_001",
            topicId = "dsa_graphs",
            questionText = "What are the different ways to represent a graph?",
            answerText = "1) Adjacency Matrix: O(V²) space, O(1) edge lookup, good for dense graphs. 2) Adjacency List: O(V+E) space, O(degree) lookup, good for sparse graphs. 3) Edge List: O(E) space, simple but O(E) lookup. Most real-world graphs are sparse, so adjacency list is common.",
            difficulty = Difficulty.EASY,
            tags = listOf("graphs", "representation", "basics")
        ),
        Question(
            id = "graph_002",
            topicId = "dsa_graphs",
            questionText = "How do you detect a cycle in a directed graph?",
            answerText = "DFS with three states: WHITE (unvisited), GRAY (in current path), BLACK (fully processed). Cycle exists if we visit a GRAY node. Alternatively, Kahn's algorithm: if topological sort doesn't include all vertices, cycle exists. Time: O(V+E).",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("graphs", "cycle-detection", "dfs")
        ),
        Question(
            id = "graph_003",
            topicId = "dsa_graphs",
            questionText = "Explain Dijkstra's algorithm.",
            answerText = "Single-source shortest path for non-negative weights. Use priority queue (min-heap). Initialize distances to infinity except source (0). Extract min, relax all edges. Time: O((V+E)log V) with binary heap. Doesn't work with negative edges.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("graphs", "dijkstra", "shortest-path")
        ),
        Question(
            id = "graph_004",
            topicId = "dsa_graphs",
            questionText = "What is topological sorting and when is it used?",
            answerText = "Linear ordering of vertices where for every edge u→v, u comes before v. Only for DAGs (Directed Acyclic Graphs). Uses: build systems, task scheduling, course prerequisites. Algorithms: Kahn's (BFS with in-degree) or DFS-based. Time: O(V+E).",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("graphs", "topological-sort", "dag")
        ),
        Question(
            id = "graph_005",
            topicId = "dsa_graphs",
            questionText = "How do you find connected components in an undirected graph?",
            answerText = "Method 1: DFS/BFS from each unvisited node, marking all reachable nodes as one component. Method 2: Union-Find - for each edge, union the vertices; count distinct sets. Time: O(V+E) for DFS/BFS, O(E⋅α(V)) for Union-Find with path compression.",
            difficulty = Difficulty.EASY,
            tags = listOf("graphs", "connected-components", "union-find")
        )
    )

    // ============================================================================
    // QUESTIONS - DSA: Dynamic Programming
    // ============================================================================
    
    private val dpQuestions = listOf(
        Question(
            id = "dp_001",
            topicId = "dsa_dp",
            questionText = "What is Dynamic Programming and when should you use it?",
            answerText = "DP solves problems by breaking into overlapping subproblems and storing results (memoization/tabulation). Use when: 1) Optimal substructure exists, 2) Overlapping subproblems. Approaches: Top-down (recursion + memo) or Bottom-up (iterative). Often reduces exponential to polynomial time.",
            difficulty = Difficulty.EASY,
            tags = listOf("dynamic-programming", "concepts", "basics")
        ),
        Question(
            id = "dp_002",
            topicId = "dsa_dp",
            questionText = "Explain the 0/1 Knapsack problem.",
            answerText = "Given weights and values of n items, maximize value for capacity W. DP[i][w] = max(DP[i-1][w], values[i] + DP[i-1][w-weights[i]]) if weight fits, else DP[i-1][w]. Time: O(nW), Space: O(nW) or O(W) optimized. Each item used at most once.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("dynamic-programming", "knapsack", "optimization")
        ),
        Question(
            id = "dp_003",
            topicId = "dsa_dp",
            questionText = "How do you solve the Longest Increasing Subsequence problem?",
            answerText = "DP approach: LIS[i] = 1 + max(LIS[j]) for all j < i where arr[j] < arr[i]. Time: O(n²). Optimized: Binary search on tails array maintaining smallest ending element for each length. Time: O(n log n).",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("dynamic-programming", "lis", "binary-search")
        ),
        Question(
            id = "dp_004",
            topicId = "dsa_dp",
            questionText = "What is the coin change problem?",
            answerText = "Find minimum coins to make amount (or number of ways). Min coins: DP[i] = min(DP[i], 1 + DP[i-coin]) for each coin. Number of ways: DP[i] += DP[i-coin]. Time: O(amount × coins), Space: O(amount). Classic unbounded knapsack variant.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("dynamic-programming", "coin-change", "unbounded-knapsack")
        ),
        Question(
            id = "dp_005",
            topicId = "dsa_dp",
            questionText = "Explain the difference between memoization and tabulation.",
            answerText = "Memoization (top-down): Recursive with cache, solves only needed subproblems, easier to implement but has recursion overhead. Tabulation (bottom-up): Iterative, fills table systematically, no stack overflow risk, often more space-efficient. Both achieve same time complexity.",
            difficulty = Difficulty.EASY,
            tags = listOf("dynamic-programming", "memoization", "tabulation")
        ),
        Question(
            id = "dp_006",
            topicId = "dsa_dp",
            questionText = "How do you solve the Edit Distance problem?",
            answerText = "Find minimum operations (insert, delete, replace) to convert string A to B. DP[i][j] = min(DP[i-1][j]+1, DP[i][j-1]+1, DP[i-1][j-1] + (A[i]≠B[j])). Time: O(mn), Space: O(mn) or O(min(m,n)). Used in spell checkers, DNA sequencing.",
            difficulty = Difficulty.HARD,
            tags = listOf("dynamic-programming", "edit-distance", "strings")
        )
    )

    // ============================================================================
    // QUESTIONS - Android: Activity
    // ============================================================================
    
    private val activityQuestions = listOf(
        Question(
            id = "act_001",
            topicId = "android_activity",
            questionText = "What is an Activity in Android?",
            answerText = "Activity is a single screen with a user interface, the entry point for user interaction. It manages UI lifecycle, handles user input, and coordinates with other components. Extends ComponentActivity or AppCompatActivity. Each activity is independent but can communicate via Intents.",
            difficulty = Difficulty.EASY,
            tags = listOf("activity", "basics", "android-components")
        ),
        Question(
            id = "act_002",
            topicId = "android_activity",
            questionText = "Explain the Activity lifecycle methods.",
            answerText = "onCreate(): Initialize activity, setContentView. onStart(): Visible but not interactive. onResume(): Interactive, foreground. onPause(): Partially visible, release resources. onStop(): Not visible. onDestroy(): Final cleanup. onRestart(): After onStop before onStart when returning.",
            difficulty = Difficulty.EASY,
            tags = listOf("activity", "lifecycle", "basics")
        ),
        Question(
            id = "act_003",
            topicId = "android_activity",
            questionText = "What happens when you rotate the screen?",
            answerText = "Configuration change triggers: onPause → onStop → onDestroy → onCreate → onStart → onResume. To preserve data: 1) ViewModel (survives config changes), 2) onSaveInstanceState/onRestoreInstanceState for small data, 3) android:configChanges in manifest (not recommended).",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("activity", "configuration-change", "rotation")
        ),
        Question(
            id = "act_004",
            topicId = "android_activity",
            questionText = "What are Activity launch modes?",
            answerText = "standard: New instance each time. singleTop: Reuse if on top (onNewIntent). singleTask: Single instance in task, clears above. singleInstance: Alone in task, separate task. singleInstancePerTask: Single instance per task (API 31+). Set via manifest or Intent flags.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("activity", "launch-modes", "tasks")
        ),
        Question(
            id = "act_005",
            topicId = "android_activity",
            questionText = "How do you pass data between Activities?",
            answerText = "1) Intent extras: putExtra/getExtra for primitives and Parcelable. 2) Bundle: Group of key-value pairs. 3) ViewModel with shared scope. 4) Singleton/Repository pattern. 5) ActivityResult API for returning data. Large objects should use Repository, not Intent extras.",
            difficulty = Difficulty.EASY,
            tags = listOf("activity", "intent", "data-passing")
        )
    )

    // ============================================================================
    // QUESTIONS - Android: ViewModel
    // ============================================================================
    
    private val viewModelQuestions = listOf(
        Question(
            id = "vm_001",
            topicId = "android_viewmodel",
            questionText = "What is ViewModel and why is it used?",
            answerText = "ViewModel stores and manages UI-related data in lifecycle-conscious way. Survives configuration changes (rotation). Separates UI logic from UI controllers. Part of Android Architecture Components. Cleared when owner is permanently destroyed (finish(), not rotation).",
            difficulty = Difficulty.EASY,
            tags = listOf("viewmodel", "architecture", "basics")
        ),
        Question(
            id = "vm_002",
            topicId = "android_viewmodel",
            questionText = "How do you share data between Fragments using ViewModel?",
            answerText = "Use activity-scoped ViewModel: by activityViewModels(). Both fragments observe same ViewModel instance, enabling communication. Use SharedFlow for events, StateFlow for state. Avoids tight coupling between fragments.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("viewmodel", "fragments", "sharing")
        ),
        Question(
            id = "vm_003",
            topicId = "android_viewmodel",
            questionText = "What is ViewModelScope and how does it work?",
            answerText = "viewModelScope is CoroutineScope tied to ViewModel lifecycle. Automatically canceled in onCleared(). Use for async operations: viewModelScope.launch { }. Dispatchers.Main by default. Prevents memory leaks from orphaned coroutines.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("viewmodel", "coroutines", "scope")
        ),
        Question(
            id = "vm_004",
            topicId = "android_viewmodel",
            questionText = "How do you inject dependencies into ViewModel?",
            answerText = "1) Hilt: @HiltViewModel with @Inject constructor. 2) ViewModelProvider.Factory: Custom factory creating ViewModel with dependencies. 3) Koin: viewModel { } DSL. 4) Manual: Pass via SavedStateHandle. Hilt is recommended for type-safe DI.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("viewmodel", "dependency-injection", "hilt")
        ),
        Question(
            id = "vm_005",
            topicId = "android_viewmodel",
            questionText = "What is SavedStateHandle in ViewModel?",
            answerText = "SavedStateHandle survives process death (unlike regular ViewModel). Stores key-value pairs like Bundle. Automatically restored by system. Use: val query: MutableStateFlow<String> = savedStateHandle.getStateFlow('query', ''). Essential for search queries, scroll positions.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("viewmodel", "saved-state", "process-death")
        )
    )

    // ============================================================================
    // QUESTIONS - Android: Jetpack Compose
    // ============================================================================
    
    private val composeQuestions = listOf(
        Question(
            id = "comp_001",
            topicId = "android_compose",
            questionText = "What is Jetpack Compose?",
            answerText = "Jetpack Compose is Android's modern declarative UI toolkit. Write UI as functions that transform state to UI. No XML, no findViewById. Features: less code, intuitive, accelerates development, powerful. Uses Kotlin compiler plugin for @Composable functions.",
            difficulty = Difficulty.EASY,
            tags = listOf("compose", "declarative-ui", "basics")
        ),
        Question(
            id = "comp_002",
            topicId = "android_compose",
            questionText = "Explain recomposition in Compose.",
            answerText = "Recomposition is Compose re-executing composables when state changes. Only affected composables recompose (smart recomposition). Triggered by State/MutableState changes. Optimize with: remember, key, derivedStateOf, stable types. Avoid side effects in composition.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("compose", "recomposition", "performance")
        ),
        Question(
            id = "comp_003",
            topicId = "android_compose",
            questionText = "What is remember and rememberSaveable?",
            answerText = "remember: Caches value across recompositions, lost on configuration change. rememberSaveable: Survives configuration changes and process death, uses Bundle. Use remember for expensive calculations, rememberSaveable for user input state.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("compose", "state", "remember")
        ),
        Question(
            id = "comp_004",
            topicId = "android_compose",
            questionText = "How do you handle side effects in Compose?",
            answerText = "LaunchedEffect: Coroutine scoped to composition, re-launches on key change. DisposableEffect: Cleanup on leave/key change. SideEffect: Run after every successful composition. rememberCoroutineScope: For event handlers. derivedStateOf: Derived state calculation.",
            difficulty = Difficulty.HARD,
            tags = listOf("compose", "side-effects", "coroutines")
        ),
        Question(
            id = "comp_005",
            topicId = "android_compose",
            questionText = "What is state hoisting in Compose?",
            answerText = "State hoisting moves state up to make composable stateless. Pattern: pass state down, events up. Composable receives state and onValueChange lambda. Benefits: Single source of truth, testable, reusable. Example: TextField(value = text, onValueChange = { text = it }).",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("compose", "state-hoisting", "patterns")
        ),
        Question(
            id = "comp_006",
            topicId = "android_compose",
            questionText = "How do you navigate between screens in Compose?",
            answerText = "Use Navigation Compose: NavHost with composable destinations. NavController handles navigation. Pass arguments via route: 'detail/{id}'. Type-safe navigation with @Serializable routes (Navigation 2.8+). Handle deep links and back stack.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("compose", "navigation", "navhost")
        )
    )

    // ============================================================================
    // QUESTIONS - Android: Lifecycle
    // ============================================================================
    
    private val lifecycleQuestions = listOf(
        Question(
            id = "lc_001",
            topicId = "android_lifecycle",
            questionText = "What is LifecycleOwner and LifecycleObserver?",
            answerText = "LifecycleOwner: Component with lifecycle (Activity, Fragment). Provides Lifecycle via getLifecycle(). LifecycleObserver: Observes lifecycle events. Use DefaultLifecycleObserver for ON_CREATE, ON_START, etc. callbacks. Enables lifecycle-aware components.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("lifecycle", "observer", "architecture")
        ),
        Question(
            id = "lc_002",
            topicId = "android_lifecycle",
            questionText = "What is the difference between onStop and onDestroy?",
            answerText = "onStop: Activity not visible but still exists. May return via onRestart. Release heavy resources (camera, sensors). onDestroy: Activity being destroyed (finish() or system kills). Final cleanup, release all resources. isFinishing() distinguishes user vs system destruction.",
            difficulty = Difficulty.EASY,
            tags = listOf("lifecycle", "activity", "basics")
        ),
        Question(
            id = "lc_003",
            topicId = "android_lifecycle",
            questionText = "How do you handle process death in Android?",
            answerText = "System kills background app to reclaim memory. onSaveInstanceState saves small data to Bundle (max ~1MB). ViewModel with SavedStateHandle persists across process death. Use rememberSaveable in Compose. Persistent storage (DataStore, Room) for larger data.",
            difficulty = Difficulty.HARD,
            tags = listOf("lifecycle", "process-death", "persistence")
        ),
        Question(
            id = "lc_004",
            topicId = "android_lifecycle",
            questionText = "What is lifecycleScope in Android?",
            answerText = "lifecycleScope is CoroutineScope tied to Lifecycle. Automatically cancels when lifecycle is destroyed. Use: lifecycleScope.launch { }. Variants: launchWhenStarted, launchWhenResumed (deprecated), use repeatOnLifecycle instead for collecting flows.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("lifecycle", "coroutines", "scope")
        ),
        Question(
            id = "lc_005",
            topicId = "android_lifecycle",
            questionText = "Explain repeatOnLifecycle and flowWithLifecycle.",
            answerText = "repeatOnLifecycle: Suspends block, launches when lifecycle reaches state, cancels when below. Safe for flow collection. flowWithLifecycle: Operator version, emits only when in state. Prevents updates to stopped UI, avoiding crashes and wasted resources.",
            difficulty = Difficulty.HARD,
            tags = listOf("lifecycle", "flow", "collection")
        )
    )

    // ============================================================================
    // QUESTIONS - Kotlin: Coroutines
    // ============================================================================
    
    private val coroutineQuestions = listOf(
        Question(
            id = "cor_001",
            topicId = "kotlin_coroutines",
            questionText = "What are Kotlin Coroutines?",
            answerText = "Coroutines are lightweight threads for asynchronous programming. Suspendable computations that don't block threads. Key concepts: suspend functions, CoroutineScope, Dispatchers, Job. Simplify async code vs callbacks. Structured concurrency ensures proper cleanup.",
            difficulty = Difficulty.EASY,
            tags = listOf("coroutines", "async", "basics")
        ),
        Question(
            id = "cor_002",
            topicId = "kotlin_coroutines",
            questionText = "What are the different Dispatchers in Coroutines?",
            answerText = "Dispatchers.Main: UI thread, UI updates. Dispatchers.IO: Optimized for I/O (network, disk), shared pool. Dispatchers.Default: CPU-intensive work, sized to CPU cores. Dispatchers.Unconfined: Starts in caller thread, resumes in suspending function's thread.",
            difficulty = Difficulty.EASY,
            tags = listOf("coroutines", "dispatchers", "threading")
        ),
        Question(
            id = "cor_003",
            topicId = "kotlin_coroutines",
            questionText = "Explain the difference between launch and async.",
            answerText = "launch: Fire-and-forget, returns Job, exceptions propagate. Use for side effects. async: Returns Deferred<T>, call await() for result. Exceptions thrown at await(). Use when you need return value. Both are coroutine builders.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("coroutines", "launch", "async")
        ),
        Question(
            id = "cor_004",
            topicId = "kotlin_coroutines",
            questionText = "What is structured concurrency?",
            answerText = "Structured concurrency: Coroutines are organized in hierarchy. Parent-child relationship via CoroutineScope. Parent waits for all children. Cancellation propagates down, failure propagates up. Prevents leaks. Enforced by scope requirement for launch/async.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("coroutines", "structured-concurrency", "scope")
        ),
        Question(
            id = "cor_005",
            topicId = "kotlin_coroutines",
            questionText = "How does coroutine cancellation work?",
            answerText = "Call job.cancel() or scope.cancel(). Cancellation is cooperative: check isActive or use cancellable functions (delay, yield, withContext). CancellationException is thrown. Use try-finally or invokeOnCompletion for cleanup. NonCancellable for must-complete operations.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("coroutines", "cancellation", "cooperative")
        ),
        Question(
            id = "cor_006",
            topicId = "kotlin_coroutines",
            questionText = "What is SupervisorJob and when do you use it?",
            answerText = "SupervisorJob: Child failure doesn't cancel parent or siblings (unlike regular Job). Use for independent parallel tasks where one failure shouldn't affect others. supervisorScope { } creates supervisor scope. Common in UI where one failing operation shouldn't crash everything.",
            difficulty = Difficulty.HARD,
            tags = listOf("coroutines", "supervisor", "error-handling")
        )
    )

    // ============================================================================
    // QUESTIONS - Kotlin: Flow
    // ============================================================================
    
    private val flowQuestions = listOf(
        Question(
            id = "flow_001",
            topicId = "kotlin_flow",
            questionText = "What is Kotlin Flow?",
            answerText = "Flow is a cold asynchronous stream of values. Emits multiple values sequentially. Cold: Only runs when collected. Built on coroutines. Operators: map, filter, transform, etc. Terminal operators: collect, first, toList. Replaces RxJava for most cases.",
            difficulty = Difficulty.EASY,
            tags = listOf("flow", "streams", "basics")
        ),
        Question(
            id = "flow_002",
            topicId = "kotlin_flow",
            questionText = "What is the difference between Flow, StateFlow, and SharedFlow?",
            answerText = "Flow: Cold, new execution per collector. StateFlow: Hot, holds current value, new collectors get latest immediately, requires initial value. SharedFlow: Hot, no initial value, configurable replay/buffer. Use StateFlow for UI state, SharedFlow for events.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("flow", "stateflow", "sharedflow")
        ),
        Question(
            id = "flow_003",
            topicId = "kotlin_flow",
            questionText = "How do you handle exceptions in Flow?",
            answerText = "catch { }: Catches upstream exceptions, can emit recovery values. onCompletion { cause -> }: Called on completion, receives exception if any. try-catch around collect: Catches all exceptions. retryWhen { cause, attempt -> }: Retry logic with backoff.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("flow", "exceptions", "error-handling")
        ),
        Question(
            id = "flow_004",
            topicId = "kotlin_flow",
            questionText = "Explain the difference between flatMapConcat, flatMapMerge, and flatMapLatest.",
            answerText = "flatMapConcat: Sequential, waits for inner flow to complete before next. flatMapMerge: Concurrent, collects all inner flows simultaneously. flatMapLatest: Cancels previous inner flow when new value arrives. Use Latest for search-as-you-type, Merge for parallel requests.",
            difficulty = Difficulty.HARD,
            tags = listOf("flow", "flatmap", "operators")
        ),
        Question(
            id = "flow_005",
            topicId = "kotlin_flow",
            questionText = "What is the difference between launchIn and collect?",
            answerText = "collect: Suspending function, blocks until flow completes. launchIn(scope): Returns Job, starts collection in scope, non-blocking. Use collect in coroutine, launchIn for fire-and-forget. onEach { } with launchIn for side effects.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("flow", "collection", "operators")
        )
    )

    // ============================================================================
    // QUESTIONS - Kotlin: Sealed Classes
    // ============================================================================
    
    private val sealedQuestions = listOf(
        Question(
            id = "seal_001",
            topicId = "kotlin_sealed",
            questionText = "What are sealed classes in Kotlin?",
            answerText = "Sealed classes restrict inheritance to known subclasses defined in same package (same file before Kotlin 1.5). Compiler knows all subclasses, enabling exhaustive when expressions. Perfect for representing restricted hierarchies like Result<T>, UI states, API responses.",
            difficulty = Difficulty.EASY,
            tags = listOf("sealed-classes", "inheritance", "basics")
        ),
        Question(
            id = "seal_002",
            topicId = "kotlin_sealed",
            questionText = "What is the difference between sealed class and sealed interface?",
            answerText = "Sealed class: Can have state (properties), single inheritance. Sealed interface: No state, multiple inheritance possible. Both restrict implementations to same package. Use interface when you don't need shared state or want multiple sealed type hierarchies.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("sealed-classes", "sealed-interface", "inheritance")
        ),
        Question(
            id = "seal_003",
            topicId = "kotlin_sealed",
            questionText = "How do you use sealed classes for UI state?",
            answerText = "Model UI states as sealed class: Loading, Success(data), Error(message). ViewModel exposes StateFlow<UiState>. UI uses when to handle all states. Compiler ensures exhaustive handling. Example: sealed class UiState { object Loading: UiState(); data class Success(val items: List<Item>): UiState(); data class Error(val msg: String): UiState() }",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("sealed-classes", "ui-state", "mvvm")
        ),
        Question(
            id = "seal_004",
            topicId = "kotlin_sealed",
            questionText = "What is the advantage of sealed classes over enum?",
            answerText = "Sealed classes: Each subclass can have different properties/methods, can be object or data class, supports inheritance. Enums: Fixed set of instances, all same type. Use sealed for heterogeneous cases (Success has data, Loading has none), enum for homogeneous (colors, days).",
            difficulty = Difficulty.EASY,
            tags = listOf("sealed-classes", "enum", "comparison")
        )
    )

    // ============================================================================
    // QUESTIONS - Kotlin: Data Classes
    // ============================================================================
    
    private val dataClassQuestions = listOf(
        Question(
            id = "data_001",
            topicId = "kotlin_data",
            questionText = "What is a data class in Kotlin?",
            answerText = "Data class is a class designed to hold data. Compiler auto-generates: equals(), hashCode(), toString(), copy(), componentN(). Primary constructor must have at least one val/var parameter. Cannot be abstract, open, sealed, or inner.",
            difficulty = Difficulty.EASY,
            tags = listOf("data-classes", "basics", "generated-functions")
        ),
        Question(
            id = "data_002",
            topicId = "kotlin_data",
            questionText = "How does copy() work in data classes?",
            answerText = "copy() creates new instance with same values, allowing selective changes. Example: val updated = user.copy(name = 'New Name'). Performs shallow copy - nested objects share references. For deep copy, implement manually or use serialization.",
            difficulty = Difficulty.EASY,
            tags = listOf("data-classes", "copy", "immutability")
        ),
        Question(
            id = "data_003",
            topicId = "kotlin_data",
            questionText = "What are componentN() functions in data classes?",
            answerText = "componentN() enables destructuring declarations. data class User(val name: String, val age: Int) generates component1() (name) and component2() (age). Usage: val (name, age) = user. Order matches constructor parameter order. Max practical limit ~5 components.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("data-classes", "destructuring", "componentn")
        ),
        Question(
            id = "data_004",
            topicId = "kotlin_data",
            questionText = "What are the limitations of data classes?",
            answerText = "Cannot be abstract, open (no inheritance from data class), sealed, or inner. Properties in body aren't included in generated functions. No no-arg constructor by default (need defaults or plugin). Shallow equals/copy for nested objects. Consider value class for single-value wrappers.",
            difficulty = Difficulty.MEDIUM,
            tags = listOf("data-classes", "limitations", "inheritance")
        ),
        Question(
            id = "data_005",
            topicId = "kotlin_data",
            questionText = "When should you use data class vs regular class?",
            answerText = "Use data class: DTOs, domain models, value objects, API responses, immutable state. Use regular class: When you need inheritance, custom equals/hashCode logic, mutable objects with complex identity, classes with primarily behavior over data.",
            difficulty = Difficulty.EASY,
            tags = listOf("data-classes", "design", "best-practices")
        )
    )

    // ============================================================================
    // ALL QUESTIONS COMBINED
    // ============================================================================
    
    val questions: List<Question> = arrayQuestions +
            stringQuestions +
            treeQuestions +
            graphQuestions +
            dpQuestions +
            activityQuestions +
            viewModelQuestions +
            composeQuestions +
            lifecycleQuestions +
            coroutineQuestions +
            flowQuestions +
            sealedQuestions +
            dataClassQuestions

    /**
     * Returns topics with updated question counts based on actual questions.
     */
    fun getTopicsWithCounts(): List<Topic> {
        val questionCountByTopic = questions.groupBy { it.topicId }.mapValues { it.value.size }
        return topics.map { topic ->
            topic.copy(questionCount = questionCountByTopic[topic.id] ?: 0)
        }
    }

    /**
     * Returns questions for a specific topic.
     */
    fun getQuestionsForTopic(topicId: String): List<Question> {
        return questions.filter { it.topicId == topicId }
    }

    /**
     * Searches questions by query string.
     * Searches in question text, answer text, and tags.
     */
    fun searchQuestions(query: String): List<Question> {
        if (query.isBlank()) return emptyList()
        val lowerQuery = query.lowercase()
        return questions.filter { question ->
            question.questionText.lowercase().contains(lowerQuery) ||
                    question.answerText.lowercase().contains(lowerQuery) ||
                    question.tags.any { it.lowercase().contains(lowerQuery) }
        }
    }
}
