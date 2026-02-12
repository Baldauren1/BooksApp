package kz.aitu.booksapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kz.aitu.booksapp.vm.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(nav: NavController, bookId: String, vm: NoteViewModel) {
    val state by vm.state.collectAsState()

    LaunchedEffect(bookId) { vm.start(bookId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My note") },
                navigationIcon = {
                    TextButton(onClick = { nav.popBackStack() }) { Text("Back") }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (state.loading) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }

            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                TextButton(onClick = vm::clearError) { Text("Dismiss") }
            }

            if (state.savedHint) {
                Text("Saved!", style = MaterialTheme.typography.labelLarge)
            }

            OutlinedTextField(
                value = state.text,
                onValueChange = vm::onTextChange,
                label = { Text("Write your note (max 500 chars)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )

            Button(
                onClick = { vm.save(bookId) },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save") }

            OutlinedButton(
                onClick = { vm.delete(bookId) },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Delete note") }
        }
    }
}
