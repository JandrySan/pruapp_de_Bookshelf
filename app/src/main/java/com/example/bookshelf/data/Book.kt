package com.example.bookshelf.data

data class Book(val id: String, val title: String, val authors: List<String> = emptyList(), val thumbnail: String? = null, val description: String? = null) {
    val authorLine: String get() = authors.joinToString(", ").ifBlank { "Autor desconocido" }
}
