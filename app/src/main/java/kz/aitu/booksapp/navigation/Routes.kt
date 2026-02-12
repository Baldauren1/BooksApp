package kz.aitu.booksapp.navigation
import android.net.Uri

object Routes {
    const val Splash = "splash"
    const val Login = "login"
    const val Register = "register"
    const val Feed = "feed"
    const val Details = "details/{bookId}"
    const val Comments = "comments/{bookId}"
    const val Profile = "profile"

    const val Search = "search"

    const val Note = "note/{bookId}"
    fun details(bookId: String) = "details/${Uri.encode(bookId)}"
    fun comments(bookId: String) = "comments/${Uri.encode(bookId)}"
    fun note(bookId: String) = "note/${Uri.encode(bookId)}"
}
