package com.example.bookshelf.data

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BooksRepositoryTest {
    @Test
    fun `search gets volume details and upgrades thumbnail to https`() = runBlocking {
        val api = FakeBooksApiService(
            SearchResponse(listOf(SearchItem("vol-1"))),
            mapOf("vol-1" to VolumeResponse("vol-1", VolumeInfo("Compose para todos", listOf("Ana Pérez"), ImageLinks("http://books.example/cover", null), "Una guía"))),
        )

        val books = NetworkBooksRepository(api).searchBooks("  compose  ")

        assertEquals(1, books.size)
        assertEquals("Compose para todos", books.single().title)
        assertEquals("Ana Pérez", books.single().authorLine)
        assertEquals("https://books.example/cover", books.single().thumbnail)
        assertEquals("Una guía", books.single().description)
    }

    @Test
    fun `search skips books whose details cannot be loaded`() = runBlocking {
        val api = FakeBooksApiService(SearchResponse(listOf(SearchItem("missing"))), emptyMap())

        assertTrue(NetworkBooksRepository(api).searchBooks("books").isEmpty())
    }
}

private class FakeBooksApiService(
    private val response: SearchResponse,
    private val details: Map<String, VolumeResponse>,
) : BooksApiService {
    override suspend fun searchBooks(query: String, maxResults: Int, key: String?) = response
    override suspend fun getVolume(volumeId: String, key: String?): VolumeResponse =
        details[volumeId] ?: throw IllegalStateException("offline")
}
