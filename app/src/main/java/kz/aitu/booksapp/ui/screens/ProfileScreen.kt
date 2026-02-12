package kz.aitu.booksapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kz.aitu.booksapp.navigation.Routes
import kz.aitu.booksapp.vm.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(nav: NavController, vm: ProfileViewModel) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Profile / Settings") }) }
    ) { padding ->
        Column(
            Modifier.padding(padding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                TextButton(onClick = vm::clearError) { Text("Dismiss") }
            }

            Text("Signed in as: ${state.email}")
            Text("Favorites: ${state.favoriteIds.size}")

            if (state.favoriteIds.isNotEmpty() && state.favorites.isEmpty()) {
                Text(
                    "Favorites saved, but not in local cache yet.\n" +
                            "Open Feed/Search so books get cached in Room.",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (state.favorites.isNotEmpty()) {
                Text("Your favorites:", style = MaterialTheme.typography.titleMedium)

                LazyColumn(Modifier.weight(1f)) {
                    items(state.favorites) { book ->
                        ListItem(
                            headlineContent = { Text(book.title) },
                            supportingContent = { Text(book.authors) },
                            trailingContent = {
                                TextButton(onClick = { vm.removeFavorite(book.id) }) {
                                    Text("Remove")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { nav.navigate(Routes.details(book.id)) }
                        )
                        HorizontalDivider()
                    }
                }
            } else {
                Spacer(Modifier.weight(1f))
            }

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
        }
    }
}
