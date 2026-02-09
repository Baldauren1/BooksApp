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
import kz.aitu.booksapp.vm.SearchViewModel
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(nav: NavController, vm: SearchViewModel) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Search") }) }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().padding(12.dp)) {

            OutlinedTextField(
                value = state.query,
                onValueChange = vm::onQueryChange,
                label = { Text("Search books (debounced)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                TextButton(onClick = vm::retry) { Text("Retry") }
            }

            when {
                state.loading && state.items.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                state.items.isEmpty() && state.query.isNotBlank() && !state.loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No results")
                    }
                }

                else -> {
                    LazyColumn(Modifier.weight(1f)) {
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
                            if (state.canLoadMore) {
                                Button(
                                    onClick = vm::loadMore,
                                    enabled = !state.loading,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp)
                                ) {
                                    Text(if (state.loading) "Loading..." else "Load more")
                                }
                            } else {
                                Spacer(Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
