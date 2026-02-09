package kz.aitu.booksapp.data

import kz.aitu.booksapp.model.Book

class BooksRepository {
    private val books = listOf(
        Book("1", "Clean Code", "Robert C. Martin", description = "A Handbook of Agile Software Craftsmanship."),
        Book("2", "The Pragmatic Programmer", "Andrew Hunt, David Thomas", description = "Classic programming wisdom."),
        Book("3", "Kotlin in Action", "Dmitry Jemerov, Svetlana Isakova", description = "Practical Kotlin guide."),
        Book("4", "Android Programming", "Big Nerd Ranch", description = "Android fundamentals."),
        Book("5", "Designing Data-Intensive Applications", "Martin Kleppmann", description = "Data systems concepts.")
    )

    fun getFeed(): List<Book> = books
    fun getById(id: String): Book? = books.firstOrNull { it.id == id }
}
