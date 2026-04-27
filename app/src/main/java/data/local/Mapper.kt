package data.local

import domain.Book

fun BookEntity.toBook(): Book = Book(
    id = id,
    title = title,
    author = author,
    year = year,
    genre = genre,
    isRead = isRead  // ← добавить
)

fun Book.toEntity(): BookEntity = BookEntity(
    id = id,
    title = title,
    author = author,
    year = year,
    genre = genre,
    isRead = isRead  // ← добавить
)