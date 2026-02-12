package kz.aitu.booksapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kz.aitu.booksapp.navigation.Routes
import kz.aitu.booksapp.vm.DetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(nav: NavController, bookId: String, vm: DetailsViewModel) {
    val book by vm.book.collectAsState()

    LaunchedEffect(bookId) { vm.load(bookId) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Details") }) }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (book == null) {
                Text("Book not found", color = MaterialTheme.colorScheme.error)
                Button(onClick = { nav.popBackStack() }) { Text("Back") }
                return@Column
            }

            Text(book!!.title, style = MaterialTheme.typography.headlineSmall)
            Text("By: ${book!!.authors}")
            Text(book!!.description)

            Button(
                onClick = { nav.navigate(Routes.comments(bookId)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open comments (Realtime)")
            }
        }
    }
}
