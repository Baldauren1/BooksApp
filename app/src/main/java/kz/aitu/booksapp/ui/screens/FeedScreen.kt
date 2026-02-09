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
    val books by vm.books.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Books Feed") },
                actions = {
                    TextButton(onClick = { nav.navigate(Routes.Profile) }) { Text("Profile") }
                    TextButton(onClick = { nav.navigate(Routes.Search) }) { Text("Search") }
                }
            )
        }
    ) { padding ->
        LazyColumn(Modifier.padding(padding).fillMaxSize()) {
            items(books) { book ->
                ListItem(
                    headlineContent = { Text(book.title) },
                    supportingContent = { Text(book.authors) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { nav.navigate(Routes.details(book.id)) }
                        .padding(horizontal = 8.dp)
                )
                HorizontalDivider()
            }
        }
    }
}
