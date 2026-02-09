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
fun RegisterScreen(nav: NavController, vm: AuthViewModel) {
    val state by vm.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var pass2 by remember { mutableStateOf("") }

    val validationError =
        when {
            email.trim().isEmpty() -> "Email is required"
            pass.length < 6 -> "Password must be at least 6 chars"
            pass != pass2 -> "Passwords do not match"
            else -> null
        }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Register", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(email, { email = it }, label = { Text("Email") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(pass, { pass = it }, label = { Text("Password") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(pass2, { pass2 = it }, label = { Text("Repeat password") }, singleLine = true, modifier = Modifier.fillMaxWidth())

        validationError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        Button(
            onClick = {
                if (validationError == null) {
                    vm.signUp(email, pass) {
                        nav.navigate(Routes.Feed) { popUpTo(Routes.Login) { inclusive = true } }
                    }
                }
            },
            enabled = !state.loading && validationError == null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (state.loading) "Loading..." else "Create account")
        }

        TextButton(onClick = { nav.popBackStack() }) { Text("Back") }
    }
}
