package kz.aitu.booksapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import kz.aitu.booksapp.ui.screens.*
import kz.aitu.booksapp.vm.*

@Composable
fun AppNavGraph() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Routes.Splash) {
        composable(Routes.Splash) {
            val mainVm: MainViewModel = viewModel()
            SplashScreen(nav, mainVm)
        }

        composable(Routes.Login) {
            val authVm: AuthViewModel = viewModel()
            LoginScreen(nav, authVm)
        }

        composable(Routes.Register) {
            val authVm: AuthViewModel = viewModel()
            RegisterScreen(nav, authVm)
        }

        composable(Routes.Feed) {
            val feedVm: FeedViewModel = viewModel()
            FeedScreen(nav, feedVm)
        }

        composable(Routes.Profile) {
            val profileVm: ProfileViewModel = viewModel()
            ProfileScreen(nav, profileVm)
        }

        composable(Routes.Details) { backStack ->
            val bookId = backStack.arguments?.getString("bookId") ?: return@composable
            val detailsVm: DetailsViewModel = viewModel()
            DetailsScreen(nav, bookId, detailsVm)
        }

        composable(Routes.Comments) { backStack ->
            val bookId = backStack.arguments?.getString("bookId") ?: return@composable
            val commentsVm: CommentsViewModel = viewModel()
            CommentsScreen(nav, bookId, commentsVm)
        }
    }
}
