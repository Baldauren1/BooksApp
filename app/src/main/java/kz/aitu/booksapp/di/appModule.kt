package kz.aitu.booksapp.di

import kz.aitu.booksapp.data.repo.BooksRepository
import kz.aitu.booksapp.data.repo.FirebaseAuthRepository
import kz.aitu.booksapp.data.repo.FirebaseCommentsRepository
import kz.aitu.booksapp.data.repo.FirebaseFavoritesRepository
import kz.aitu.booksapp.vm.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import kz.aitu.booksapp.data.repo.remote.GoogleBooksRepository
import kz.aitu.booksapp.vm.SearchViewModel

val appModule = module {

    // repositories
    single { BooksRepository() }
    single { FirebaseAuthRepository() }
    single { FirebaseCommentsRepository() }
    single { FirebaseFavoritesRepository() }

    single { GoogleBooksRepository() }

    // viewmodels
    viewModel { MainViewModel(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { FeedViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
    viewModel { CommentsViewModel(get()) }
    viewModel { ProfileViewModel(get()) }

    viewModel { SearchViewModel(get()) }
}
