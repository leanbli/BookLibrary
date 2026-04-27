package screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.Book
import viewmodel.MainViewModel

@Composable
fun BookListScreen(
    viewModel: MainViewModel,
    onAddBook: () -> Unit,
    onBookClick: (Book) -> Unit
) {
    val books by viewModel.books.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var sortExpanded by remember { mutableStateOf(false) }
    var sortType by remember { mutableStateOf("title") }

    val sortedBooks = when (sortType) {
        "unread" -> books.sortedByDescending { !it.isRead }
        else -> books.sortedBy { it.title.lowercase() }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddBook) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column {
                    Box(modifier = Modifier.padding(8.dp)) {
                        Button(onClick = { sortExpanded = true }) {
                            Text(
                                when (sortType) {
                                    "unread" -> "Сначала непрочитанные"
                                    else -> "По названию"
                                }
                            )
                        }
                        DropdownMenu(
                            expanded = sortExpanded,
                            onDismissRequest = { sortExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("По названию") },
                                onClick = {
                                    sortType = "title"
                                    sortExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Сначала непрочитанные") },
                                onClick = {
                                    sortType = "unread"
                                    sortExpanded = false
                                }
                            )
                        }
                    }

                    LazyColumn {
                        items(sortedBooks) { book ->
                            BookCard(
                                book = book,
                                onClick = { onBookClick(book) },
                                onToggleRead = {
                                    viewModel.updateBook(book.copy(isRead = !book.isRead))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookCard(
    book: Book,
    onClick: () -> Unit,
    onToggleRead: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onToggleRead) {
                Icon(
                    imageVector = if (book.isRead) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = if (book.isRead) "Прочитана" else "Не прочитана",
                    tint = if (book.isRead) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = if (book.isRead) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                Text(text = book.author, style = MaterialTheme.typography.bodyMedium)
                Text(text = "${book.year} • ${book.genre}", style = MaterialTheme.typography.bodySmall)
            }

            Text(
                text = if (book.isRead) "Прочитана" else "Не прочитана",
                style = MaterialTheme.typography.bodySmall,
                color = if (book.isRead) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}