package kz.aitu.booksapp.vm

import androidx.lifecycle.ViewModel
import kz.aitu.booksapp.data.BooksRepository
import kz.aitu.booksapp.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DetailsViewModel(
    private val repo: BooksRepository = BooksRepository()
) : ViewModel() {
    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book

    fun load(bookId: String) {
        _book.value = repo.getById(bookId)
    }
}
