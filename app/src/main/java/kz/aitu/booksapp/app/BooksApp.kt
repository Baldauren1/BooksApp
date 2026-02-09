package kz.aitu.booksapp.app

import android.app.Application
import kz.aitu.booksapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BooksApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BooksApp)
            modules(appModule)
        }
    }
}
