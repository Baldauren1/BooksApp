package kz.aitu.booksapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kz.aitu.booksapp.vm.CommentsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsScreen(nav: NavController, bookId: String, vm: CommentsViewModel) {
    val state by vm.state.collectAsState()
    var text by remember { mutableStateOf("") }

    LaunchedEffect(bookId) { vm.start(bookId) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Comments") }) }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().padding(12.dp)) {
            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(6.dp))
                TextButton(onClick = { vm.clearError() }) { Text("Dismiss") }
            }

            LazyColumn(Modifier.weight(1f)) {
                items(state.comments) { c ->
                    Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(c.userEmail.ifEmpty { c.userId }, style = MaterialTheme.typography.labelMedium)
                            Text(c.text)
                            TextButton(onClick = { vm.delete(bookId, c.id) }) {
                                Text("Delete (only own)")
                            }
                        }
                    }
                }
            }

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Write a comment") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    vm.add(bookId, text)
                    text = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send")
            }
        }
    }
}
