package data.repository

import data.local.BookDao
import data.local.toBook
import data.local.toEntity
import domain.Book
import domain.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookRepositoryImpl(
    private val bookDao: BookDao
) : BookRepository {

    override fun getAllBooks(): Flow<List<Book>> {
        return bookDao.getAllBooks().map { entities ->
            entities.map { it.toBook() }
        }
    }

    override suspend fun getBookById(id: Long): Book? {
        return bookDao.getBookById(id)?.toBook()
    }

    override suspend fun insertBook(book: Book) {
        bookDao.insert(book.toEntity())
    }

    override suspend fun updateBook(book: Book) {
        android.util.Log.d("BookLibrary", "REPO updateBook: ${book.title}, id=${book.id}")
        bookDao.update(book.toEntity())
    }

    override suspend fun deleteBook(book: Book) {
        bookDao.delete(book.toEntity())
    }
}