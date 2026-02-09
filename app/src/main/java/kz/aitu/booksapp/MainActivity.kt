package kz.aitu.booksapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kz.aitu.booksapp.navigation.AppNavGraph
import kz.aitu.booksapp.ui.theme.BooksAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BooksAppTheme {
                AppNavGraph()
            }
        }
    }
}
