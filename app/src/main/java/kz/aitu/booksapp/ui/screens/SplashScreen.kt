package kz.aitu.booksapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kz.aitu.booksapp.navigation.Routes
import kz.aitu.booksapp.vm.MainViewModel

@Composable
fun SplashScreen(nav: NavController, vm: MainViewModel) {
    val loggedIn by vm.isLoggedIn.collectAsState()

    LaunchedEffect(loggedIn) {
        nav.navigate(if (loggedIn) Routes.Feed else Routes.Login) {
            popUpTo(Routes.Splash) { inclusive = true }
        }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
