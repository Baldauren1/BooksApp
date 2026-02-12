package kz.aitu.booksapp.vm

import androidx.lifecycle.ViewModel
import kz.aitu.booksapp.data.repo.BooksRepository
import kz.aitu.booksapp.domain.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DetailsViewModel(
    private val repo: BooksRepository
) : ViewModel() {

    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book

    fun load(bookId: String) {
        _book.value = repo.getById(bookId)
    }
}
