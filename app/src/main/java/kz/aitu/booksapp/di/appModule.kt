package kz.aitu.booksapp.di

import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kz.aitu.booksapp.data.local.AppDatabase
import kz.aitu.booksapp.data.remote.GoogleBooksApi
import kz.aitu.booksapp.data.remote.GoogleBooksRepository
import kz.aitu.booksapp.data.repo.FirebaseAuthRepository
import kz.aitu.booksapp.data.repo.FirebaseCommentsRepository
import kz.aitu.booksapp.data.repo.FirebaseFavoritesRepository
import kz.aitu.booksapp.data.repo.FeedRepository
import kz.aitu.booksapp.vm.*
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {

    //  JSON / OkHttp / Retrofit
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
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

    single {
        get<Retrofit>().create(GoogleBooksApi::class.java)
    }

    //  Room
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "booksapp.db"
        ).build()
    }

    single { get<AppDatabase>().bookDao() }

    //  Repositories
    single { GoogleBooksRepository(api = get()) }
    single { FeedRepository(dao = get(), remote = get()) }

    single { FirebaseAuthRepository() }
    single { FirebaseCommentsRepository() }
    single { FirebaseFavoritesRepository() }

    // ---------- ViewModels ----------
    viewModel { MainViewModel(get()) }
    viewModel { AuthViewModel(get()) }

    viewModel { FeedViewModel(get()) }        // FeedRepository
    viewModel { DetailsViewModel(get()) }     // FeedRepository
    viewModel { SearchViewModel(get()) }      // GoogleBooksRepository

    viewModel { CommentsViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
}
