package kz.aitu.booksapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import kz.aitu.booksapp.ui.screens.*
import kz.aitu.booksapp.vm.*
import kz.aitu.booksapp.vm.SearchViewModel
import org.koin.androidx.compose.koinViewModel
import android.net.Uri

@Composable
fun AppNavGraph() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Routes.Splash) {
        composable(Routes.Splash) {
            val mainVm: MainViewModel = koinViewModel()
            SplashScreen(nav, mainVm)
        }

        composable(Routes.Login) {
            val authVm: AuthViewModel = koinViewModel()
            LoginScreen(nav, authVm)
        }

        composable(Routes.Register) {
            val authVm: AuthViewModel = koinViewModel()
            RegisterScreen(nav, authVm)
        }

        composable(Routes.Feed) {
            val feedVm: FeedViewModel = koinViewModel()
            FeedScreen(nav, feedVm)
        }

        composable(Routes.Profile) {
            val profileVm: ProfileViewModel = koinViewModel()
            ProfileScreen(nav, profileVm)
        }

        composable(Routes.Details) { backStack ->
            val encoded = backStack.arguments?.getString("bookId") ?: return@composable
            val bookId = Uri.decode(encoded)
            val vm: DetailsViewModel = koinViewModel()
            DetailsScreen(nav, bookId, vm)
        }

        composable(Routes.Comments) { backStack ->
            val encoded = backStack.arguments?.getString("bookId") ?: return@composable
            val bookId = Uri.decode(encoded)
            val vm: CommentsViewModel = koinViewModel()
            CommentsScreen(nav, bookId, vm)
        }

        composable(Routes.Search) {
            val vm: SearchViewModel = koinViewModel()
            SearchScreen(nav, vm)
        }

        composable(Routes.Note) { backStack ->
            val encoded = backStack.arguments?.getString("bookId") ?: return@composable
            val bookId = Uri.decode(encoded)
            val vm: NoteViewModel = koinViewModel()
            NoteScreen(nav, bookId, vm)
        }
    }
}
