package kz.aitu.booksapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kz.aitu.booksapp.navigation.Routes
import kz.aitu.booksapp.vm.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(nav: NavController, vm: SearchViewModel) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Search") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = state.query,
                onValueChange = vm::onQueryChange,
                label = { Text("Search books") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { vm.searchFirstPage() },
                enabled = !state.loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.loading && state.items.isEmpty()) "Searching..." else "Search")
            }

            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                TextButton(onClick = { vm.clearError() }) { Text("Dismiss") }
            }

            if (!state.loading && state.items.isEmpty() && state.error == null) {
                Text("No results yet. Type a query and press Search.")
            }

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(state.items) { book ->
                    ListItem(
                        headlineContent = { Text(book.title) },
                        supportingContent = { Text(book.authors) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { nav.navigate(Routes.details(book.id)) }
                    )
                    HorizontalDivider()
                }

                item {
                    Spacer(Modifier.height(12.dp))

                    if (state.items.isNotEmpty()) {
                        when {
                            state.endReached -> {
                                Text(
                                    "End of results",
                                    modifier = Modifier.fillMaxWidth(),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            else -> {
                                Button(
                                    onClick = { vm.loadMore() },
                                    enabled = !state.loading,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(if (state.loading) "Loading..." else "Load more")
                                }
                            }
                        }
                    }
                }
            }

            if (state.loading && state.items.isNotEmpty()) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
