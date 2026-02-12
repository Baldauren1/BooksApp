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
import kz.aitu.booksapp.vm.FeedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(nav: NavController, vm: FeedViewModel) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Books Feed") },
                actions = {
                    TextButton(onClick = { nav.navigate(Routes.Search) }) { Text("Search") }
                    TextButton(onClick = { nav.navigate(Routes.Profile) }) { Text("Profile") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {
            if (state.offlineHint) {
                Text(
                    "Offline mode: showing cached feed",
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(12.dp))
                TextButton(onClick = vm::clearError, modifier = Modifier.padding(horizontal = 12.dp)) {
                    Text("Dismiss")
                }
            }

            Button(
                onClick = vm::refresh,
                enabled = !state.loading,
                modifier = Modifier.fillMaxWidth().padding(12.dp)
            ) {
                Text(if (state.loading) "Loading..." else "Refresh feed")
            }

            if (state.items.isEmpty() && state.loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp))
            }

            LazyColumn(Modifier.fillMaxSize()) {
                items(state.items) { book ->
                    ListItem(
                        headlineContent = { Text(book.title) },
                        supportingContent = { Text(book.authors) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (book.id.isNotBlank()) {
                                    nav.navigate(Routes.details(book.id))
                                }
                            }
                            .padding(horizontal = 8.dp)
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
