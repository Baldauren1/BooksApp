package kz.aitu.booksapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kz.aitu.booksapp.navigation.Routes
import kz.aitu.booksapp.vm.AuthViewModel

@Composable
fun LoginScreen(nav: NavController, vm: AuthViewModel) {
    val state by vm.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") }, singleLine = true, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = pass, onValueChange = { pass = it },
            label = { Text("Password") }, singleLine = true, modifier = Modifier.fillMaxWidth()
        )

        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        Button(
            onClick = {
                vm.signIn(email, pass) {
                    nav.navigate(Routes.Feed) { popUpTo(Routes.Login) { inclusive = true } }
                }
            },
            enabled = !state.loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (state.loading) "Loading..." else "Sign In")
        }

        TextButton(onClick = { nav.navigate(Routes.Register) }) {
            Text("No account? Register")
        }
    }
}
