package kz.aitu.booksapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kz.aitu.booksapp.navigation.Routes
import kz.aitu.booksapp.vm.DetailsViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(nav: NavController, bookId: String, vm: DetailsViewModel) {
    val state by vm.state.collectAsState()

    LaunchedEffect(bookId) { vm.load(bookId) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Details") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (state.loading) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
                return@Column
            }

            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                TextButton(onClick = vm::clearError) { Text("Dismiss") }
            }

            val book = state.book
            if (book == null) {
                Button(onClick = { nav.popBackStack() }) { Text("Back") }
                return@Column
            }

            Text(book.title, style = MaterialTheme.typography.headlineSmall)
            Text("By: ${book.authors}")
            Text(book.description)

            Button(
                onClick = { vm.toggleFavorite(book.id, !state.isFavorite) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.isFavorite) "Remove from favorites" else "Add to favorites")
            }

            Button(
                onClick = { nav.navigate(Routes.note(book.id)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Note (Create/Edit)")
            }

            Button(
                onClick = { nav.navigate(Routes.comments(book.id)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Comments (Realtime)")
            }
        }
    }
}

