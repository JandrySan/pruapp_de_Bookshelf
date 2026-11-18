package com.example.bookshelf.data

interface BooksRepository { suspend fun searchBooks(query: String): List<Book> }

class NetworkBooksRepository(private val service: BooksApiService) : BooksRepository {
    override suspend fun searchBooks(query: String): List<Book> {
        val ids = service.searchBooks(query.trim()).items.orEmpty().map { it.id }.distinct()
        return ids.mapNotNull { id ->
            runCatching { service.getVolume(id) }.getOrNull()?.let { volume ->
                val info = volume.volumeInfo ?: return@let null
                val title = info.title?.takeIf(String::isNotBlank) ?: return@let null
                val image = info.imageLinks?.thumbnail ?: info.imageLinks?.smallThumbnail
                Book(volume.id ?: id, title, info.authors.orEmpty(), image?.replace("http://", "https://"), info.description)
            }
        }
    }
}

class FakeBooksRepository : BooksRepository {
    override suspend fun searchBooks(query: String) = listOf(
        Book("fake-1", "El principito", listOf("Antoine de Saint-Exupéry")),
        Book("fake-2", "Cien años de soledad", listOf("Gabriel García Márquez")),
    )
}
