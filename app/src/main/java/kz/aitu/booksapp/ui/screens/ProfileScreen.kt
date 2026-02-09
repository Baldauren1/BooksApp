package kz.aitu.booksapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kz.aitu.booksapp.navigation.Routes
import kz.aitu.booksapp.vm.ProfileViewModel

@Composable
fun ProfileScreen(nav: NavController, vm: ProfileViewModel) {
    val email by vm.email.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Profile / Settings", style = MaterialTheme.typography.headlineMedium)
        Text("Signed in as: $email")

        Button(
            onClick = {
                vm.signOut()
                nav.navigate(Routes.Login) {
                    popUpTo(Routes.Feed) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign out")
        }

        TextButton(onClick = { nav.popBackStack() }) { Text("Back") }
    }
}
