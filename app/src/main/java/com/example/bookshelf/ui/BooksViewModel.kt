package com.example.bookshelf.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.Book
import com.example.bookshelf.data.BooksApiService
import com.example.bookshelf.data.BooksRepository
import com.example.bookshelf.data.NetworkBooksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BooksUiState(val query: String = "android", val books: List<Book> = emptyList(), val loading: Boolean = false, val error: String? = null)

class BooksViewModel(private val repository: BooksRepository = NetworkBooksRepository(BooksApiService.create())) : ViewModel() {
    private val _uiState = MutableStateFlow(BooksUiState())
    val uiState: StateFlow<BooksUiState> = _uiState.asStateFlow()
    init { search() }

    fun search(query: String = _uiState.value.query) {
        val term = query.trim()
        if (term.isEmpty()) return
        _uiState.value = _uiState.value.copy(query = term, loading = true, error = null)
        viewModelScope.launch {
            runCatching { repository.searchBooks(term) }
                .onSuccess { _uiState.value = _uiState.value.copy(books = it, loading = false) }
                .onFailure { _uiState.value = _uiState.value.copy(loading = false, error = "No se pudieron cargar los libros. Revisa tu conexión e inténtalo de nuevo.") }
        }
    }
}
