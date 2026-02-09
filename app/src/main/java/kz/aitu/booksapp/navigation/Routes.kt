package kz.aitu.booksapp.navigation

object Routes {
    const val Splash = "splash"
    const val Login = "login"
    const val Register = "register"
    const val Feed = "feed"
    const val Details = "details/{bookId}"
    const val Comments = "comments/{bookId}"
    const val Profile = "profile"

    fun details(bookId: String) = "details/$bookId"
    fun comments(bookId: String) = "comments/$bookId"
}
