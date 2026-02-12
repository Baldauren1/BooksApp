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
import coil.compose.AsyncImage
import kz.aitu.booksapp.navigation.Routes
import kz.aitu.booksapp.vm.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(nav: NavController, vm: SearchViewModel) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search") },
                actions = {
                    TextButton(onClick = { nav.navigate(Routes.Feed) }) { Text("Feed") }
                }
            )
        }
    ) { padding ->

        Column(
            Modifier.padding(padding).fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            OutlinedTextField(
                value = state.query,
                onValueChange = { vm.onQueryChange(it) },
                label = { Text("Search books (min 2 chars)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { vm.searchFirstPage() },
                    enabled = !state.loading,
                    modifier = Modifier.weight(1f)
                ) { Text("Search") }

                OutlinedButton(
                    onClick = { nav.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) { Text("Back") }
            }

            state.error?.let {
                Card {
                    Column(Modifier.padding(12.dp)) {
                        Text(it, color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(onClick = { vm.clearError() }) { Text("Dismiss") }
                            TextButton(onClick = { vm.searchFirstPage() }) { Text("Retry") }
                        }
                    }
                }
            }

            when {
                state.loading && state.items.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                !state.loading && state.items.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No results")
                    }
                }

                else -> {
                    LazyColumn(Modifier.weight(1f)) {
                        items(state.items) { book ->
                            ListItem(
                                leadingContent = {
                                    AsyncImage(
                                        model = book.thumbnailUrl,
                                        contentDescription = null,
                                        modifier = Modifier.size(56.dp)
                                    )
                                },
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

                            Button(
                                onClick = { vm.loadMore() },
                                enabled = !state.loading && !state.endReached,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    when {
                                        state.endReached -> "No more"
                                        state.loading -> "Loading..."
                                        else -> "Load more"
                                    }
                                )
                            }

                            Spacer(Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}
