## BooksApp

BooksApp is an Android application built with Kotlin + Jetpack Compose.
It supports book search using Google Books API, local caching via Room,
and user features (authentication, favorites, notes, realtime comments) using Firebase.

## Tech Stack
- UI: Jetpack Compose, Material 3, Navigation Compose
- Architecture: MVVM (Screens → ViewModels → Repositories)
- DI: Koin
- Networking: Retrofit + OkHttp
- Persistence: Room (SQLite)
- Images: Coil
- Backend services: Firebase Authentication + Firebase Realtime Database

## App Features
- Authentication: Register / Login / Logout (Firebase Auth)
- Feed: cached list of books (Room)
- Search: Google Books search + pagination ("Load more")
- Details: view book details + add/remove favorite
- Notes: create/edit note per book (Firebase)
- Comments: realtime comments per book (Firebase Realtime Database)

## Architecture Overview
The app follows MVVM:
- **UI layer**: Compose screens (ui/screens)
- **State layer**: ViewModels (vm) expose `StateFlow` UI state
- **Data layer**: repositories in `data/repo`, with sources:
  - Remote: Google Books API (Retrofit)
  - Local: Room database cache
  - Cloud: Firebase (Auth + Realtime Database)

See `/docs/ARCHITECTURE.md` for a diagram and data flow.

## Build & Run
### Requirements
- Android Studio (recent version)
- JDK 17
- Android SDK (minSdk 26)

### Setup steps
1) Clone this repository
2) Open the project in Android Studio (open the folder that contains `settings.gradle.kts`)
3) Firebase setup (required):
   - Download `google-services.json` from Firebase Console
   - Place it into: `app/google-services.json`
   - **Do not commit this file** (it is excluded from Git in Final hardening)
4) Sync Gradle (File → Sync Project with Gradle Files)
5) Run the app on emulator or device

## Firebase Setup Notes
This project uses:
- Firebase Authentication (email/password)
- Firebase Realtime Database for:
  - `users/{uid}/favorites`
  - `users/{uid}/notes`
  - `books/{bookId}/comments`

Security rules assumptions are documented in `/docs/FIREBASE_RULES.md` (Final requirement).

## API Used
- Google Books API (public search endpoint via Retrofit).
No paid API key is required for basic search functionality.

## Demo Creditials
- email: test@example.com
- password: 159951

## Project Structure
- `app/src/main/java/kz/aitu/booksapp/`
  - `ui/screens/` — Compose screens
  - `vm/` — ViewModels (StateFlow UI state)
  - `data/local/` — Room database + DAO + entities
  - `data/remote/` — Retrofit API + repository
  - `data/repo/` — Firebase repositories (auth, favorites, notes, comments)
  - `di/` — Koin module
  - `navigation/` — Routes and NavGraph

## Final Add-on Deliverables
- Signed release build evidence: `/docs/release_evidence/`
- Store listing draft: `/docs/store_listing/`
- Architecture diagram: `/docs/ARCHITECTURE.md`
- Release notes: `/docs/RELEASE_NOTES.md`
- QA log: `/docs/QA_LOG.md`
- Performance note: `/docs/PERFORMANCE_NOTE.md`
- Release checklist: `/docs/RELEASE_CHECKLIST.md`
