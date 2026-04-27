package screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBookScreen(
    book: Book?,
    onSave: (Book) -> Unit,
    onBack: () -> Unit
) {
    val title = remember { mutableStateOf(book?.title ?: "") }
    val author = remember { mutableStateOf(book?.author ?: "") }
    val year = remember { mutableStateOf(book?.year?.toString() ?: "") }
    val genre = remember { mutableStateOf(book?.genre ?: "") }
    val isRead = remember { mutableStateOf(book?.isRead ?: false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (book == null) "Добавление книги" else "Редактирование книги") },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = onBack) {
                        Text("←")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title.value,
                onValueChange = { title.value = it },
                label = { Text("Название") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = author.value,
                onValueChange = { author.value = it },
                label = { Text("Автор") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = year.value,
                onValueChange = { year.value = it },
                label = { Text("Год") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = genre.value,
                onValueChange = { genre.value = it },
                label = { Text("Жанр") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isRead.value,
                    onCheckedChange = { isRead.value = it }
                )
                Text("Прочитана")
            }

            Button(
                onClick = {
                    val yearInt = year.value.toIntOrNull()
                    if (title.value.isNotBlank() &&
                        author.value.isNotBlank() &&
                        yearInt != null &&
                        yearInt > 0 &&
                        genre.value.isNotBlank()) {

                        val newBook = Book(
                            id = book?.id ?: 0,
                            title = title.value.trim(),
                            author = author.value.trim(),
                            year = yearInt,
                            genre = genre.value.trim(),
                            isRead = isRead.value
                        )
                        onSave(newBook)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text(if (book == null) "Добавить" else "Сохранить")
            }
        }
    }
}