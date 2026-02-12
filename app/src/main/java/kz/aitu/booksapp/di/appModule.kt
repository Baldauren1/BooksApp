package kz.aitu.booksapp.di

import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import kz.aitu.booksapp.data.local.AppDatabase
import kz.aitu.booksapp.data.remote.GoogleBooksApi
import kz.aitu.booksapp.data.remote.GoogleBooksRepository
import kz.aitu.booksapp.data.repo.FeedRepository
import kz.aitu.booksapp.data.repo.FirebaseAuthRepository
import kz.aitu.booksapp.data.repo.FirebaseCommentsRepository
import kz.aitu.booksapp.data.repo.FirebaseFavoritesRepository
import kz.aitu.booksapp.vm.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {

    // --- Room ---
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "booksapp.db"
        ).build()
    }
    single { get<AppDatabase>().bookDao() }

    // --- Network (Retrofit + Kotlinx Serialization) ---
    single {
        Json { ignoreUnknownKeys = true }
    }

    single {
        OkHttpClient.Builder().build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .client(get())
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single<GoogleBooksApi> {
        get<Retrofit>().create(GoogleBooksApi::class.java)
    }

    // --- Repos ---
    single { GoogleBooksRepository(api = get()) }
    single { FeedRepository(dao = get(), api = get()) }

    single { FirebaseAuthRepository() }
    single { FirebaseCommentsRepository() }
    single { FirebaseFavoritesRepository() }

    single { kz.aitu.booksapp.data.repo.FirebaseNotesRepository() }

    // --- ViewModels ---
    viewModel { MainViewModel(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { FeedViewModel(get()) }
    viewModel { CommentsViewModel(get()) }
    viewModel { SearchViewModel(get()) }

    viewModel { DetailsViewModel(get(), get()) } // BooksRepository, FirebaseFavoritesRepository
    viewModel { ProfileViewModel(get(), get(), get()) } // AuthRepo, FavoritesRepo, BookDao

    viewModel { kz.aitu.booksapp.vm.NoteViewModel(get()) }
    viewModel { DetailsViewModel(get(), get()) } // BookDao, FirebaseFavoritesRepository
}
