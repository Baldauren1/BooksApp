package kz.aitu.booksapp.vm

import androidx.lifecycle.ViewModel
import kz.aitu.booksapp.data.repo.BooksRepository
import kz.aitu.booksapp.domain.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FeedViewModel(
    private val repo: BooksRepository = BooksRepository()
) : ViewModel() {
    private val _books = MutableStateFlow(repo.getFeed())
    val books: StateFlow<List<Book>> = _books
}
