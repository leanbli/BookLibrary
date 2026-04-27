package booklibrary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import data.local.BookDatabase
import data.repository.BookRepositoryImpl
import screens.AddEditBookScreen
import screens.BookDetailScreen
import screens.BookListScreen
import viewmodel.MainViewModel
import viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = BookDatabase.getInstance(this)
        val repository = BookRepositoryImpl(database.bookDao())

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BookLibraryApp(repository = repository)
                }
            }
        }
    }
}

@Composable
fun BookLibraryApp(repository: BookRepositoryImpl) {
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(repository)
    )
    val books by viewModel.books.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "list"
    ) {
        // Список книг
        composable("list") {
            BookListScreen(
                viewModel = viewModel,
                onAddBook = { navController.navigate("add") },
                onBookClick = { book ->
                    navController.navigate("detail/${book.id}")
                }
            )
        }

        // Детальный экран
        composable(
            route = "detail/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getLong("bookId") ?: 0
            val book = books.find { it.id == bookId }
            if (book != null) {
                BookDetailScreen(
                    book = book,
                    onBack = { navController.popBackStack() },
                    onEdit = { navController.navigate("edit/${book.id}") },
                    onDelete = {
                        viewModel.deleteBook(book)
                        navController.popBackStack()
                    },
                    onToggleRead = { updatedBook ->
                        viewModel.updateBook(updatedBook)
                    }
                )
            } else {
                navController.popBackStack()
            }
        }

        // Экран добавления
        composable("add") {
            AddEditBookScreen(
                book = null,
                onSave = { newBook ->
                    viewModel.insertBook(newBook)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Экран редактирования
        composable(
            route = "edit/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getLong("bookId") ?: 0
            val book = books.find { it.id == bookId }
            if (book != null) {
                AddEditBookScreen(
                    book = book,
                    onSave = { updatedBook ->
                        android.util.Log.d("BookLibrary", "=== SAVING: ${updatedBook.title}, id=${updatedBook.id} ===")
                        viewModel.updateBook(updatedBook)
                        navController.popBackStack()
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            } else {
                navController.popBackStack()
            }
        }
    }
}