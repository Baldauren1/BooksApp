## Architecture (BooksApp)

### Overview
BooksApp uses MVVM with a clean separation:
- Compose UI screens render state
- ViewModels own UI state and call repositories
- Repositories interact with data sources (Room / Retrofit / Firebase)

### Layer Diagram

UI (Jetpack Compose)

└── Screens (ui/screens)

└── ViewModels (vm)  [StateFlow: loading/data/error]

└── Repositories (data/repo + data/remote + data/local)

├── Remote: GoogleBooksApi (Retrofit)

├── Local: Room (BookDao, AppDatabase)

└── Cloud: Firebase (Auth + Realtime Database)

### Data Flow (Key Scenarios)

### 1) Feed (cached)
1. FeedScreen subscribes to FeedViewModel state
2. FeedViewModel observes Room (`dao.observeAll()`)
3. On refresh, FeedRepository calls GoogleBooksRepository.search()
4. Results are mapped to entities and saved to Room
5. UI automatically updates from Room flow

### 2) Search + Pagination
1. SearchScreen updates query in SearchViewModel
2. SearchViewModel calls GoogleBooksRepository.search(query, page)
3. New items appended to current list
4. "Load more" requests the next page until `endReached=true`

### 3) Favorites / Notes (Firebase user-scoped)
1. User logs in via FirebaseAuthRepository
2. Favorites are stored under `users/{uid}/favorites/{bookId}`
3. Notes are stored under `users/{uid}/notes/{bookId}`

### 4) Realtime Comments
1. CommentsScreen subscribes to Firebase path `books/{bookId}/comments`
2. Firebase returns realtime updates via listeners/Flow
3. UI updates instantly without manual refresh

### Dependency Injection
Koin module (`di/appModule.kt`) provides:
- Room database + DAO
- Retrofit API + repositories
- Firebase repositories
- ViewModels
