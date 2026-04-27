package viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.Book
import domain.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: BookRepository
) : ViewModel() {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllBooks().collect { bookList ->
                android.util.Log.d("BookLibrary", "loadBooks: ${bookList.map { it.title + "=" + it.isRead }}")
                _books.value = bookList
                _isLoading.value = false
            }
        }
    }

    fun insertBook(book: Book) {
        viewModelScope.launch {
            repository.insertBook(book)
            loadBooks()
        }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch {
            android.util.Log.d("BookLibrary", "=== UPDATE BOOK: id=${book.id}, title=${book.title}, isRead=${book.isRead} ===")
            repository.updateBook(book)
            loadBooks()
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            repository.deleteBook(book)
            loadBooks()
        }
    }
}