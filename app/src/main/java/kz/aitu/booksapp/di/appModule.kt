package kz.aitu.booksapp.di

import androidx.room.Room
import kz.aitu.booksapp.data.local.AppDatabase
import kz.aitu.booksapp.data.remote.GoogleBooksRepository
import kz.aitu.booksapp.data.repo.BooksRepository
import kz.aitu.booksapp.data.repo.FeedRepository
import kz.aitu.booksapp.data.repo.FirebaseAuthRepository
import kz.aitu.booksapp.data.repo.FirebaseCommentsRepository
import kz.aitu.booksapp.data.repo.FirebaseFavoritesRepository
import kz.aitu.booksapp.vm.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // --- Room ---
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "booksapp.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().bookDao() }

    // --- Remote ---
    single { GoogleBooksRepository() }

    // --- Firebase ---
    single { FirebaseAuthRepository() }
    single { FirebaseCommentsRepository() }
    single { FirebaseFavoritesRepository() }

    // --- Repos ---
    single { BooksRepository() }
    single { FeedRepository(dao = get(), remote = get()) }

    // --- ViewModels ---
    viewModel { MainViewModel(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { CommentsViewModel(get()) }

    viewModel { FeedViewModel(get()) }      // FeedRepository
    viewModel { DetailsViewModel(get()) }   // BooksRepository
    viewModel { SearchViewModel(get()) }    // GoogleBooksRepository
}
