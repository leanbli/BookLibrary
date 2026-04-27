package domain

data class Book(
    val id: Long = 0,
    val title: String,
    val author: String,
    val year: Int,
    val genre: String,
    val isRead: Boolean = false
)