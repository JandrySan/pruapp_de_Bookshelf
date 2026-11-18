package com.example.bookshelf.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.bookshelf.data.Book

@Composable
fun BookshelfApp(booksViewModel: BooksViewModel = viewModel()) {
    val state by booksViewModel.uiState.collectAsState()
    var text by remember(state.query) { mutableStateOf(state.query) }
    MaterialTheme(colorScheme = lightColorScheme(primary = Color(0xFF455A49), secondary = Color(0xFFEAAE66), background = Color(0xFFF7F4EC))) {
        Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
            Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(22.dp))
                Text("BOOKSHELF", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text("Tu próxima lectura", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(value = text, onValueChange = { text = it }, modifier = Modifier.fillMaxWidth(), singleLine = true,
                    shape = RoundedCornerShape(18.dp), placeholder = { Text("Busca un libro o autor") },
                    trailingIcon = { IconButton(onClick = { booksViewModel.search(text) }) { Icon(Icons.Default.Search, contentDescription = "Buscar") } })
                Spacer(Modifier.height(18.dp))
                Text("Libros para descubrir", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(12.dp))
                when {
                    state.loading && state.books.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                    state.error != null && state.books.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(state.error!!, style = MaterialTheme.typography.bodyLarge)
                            TextButton(onClick = { booksViewModel.search(text) }) { Text("Reintentar") }
                        }
                    }
                    state.books.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No encontramos libros. Prueba otra búsqueda.") }
                    else -> LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(14.dp), verticalArrangement = Arrangement.spacedBy(18.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
                        items(state.books, key = { it.id }) { BookCard(it) }
                    }
                }
            }
        }
    }
}

@Composable
private fun BookCard(book: Book) {
    var expanded by remember { mutableStateOf(false) }
    val description = book.description?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT).toString().trim() }
    Column(Modifier.clickable { expanded = true }) {
        AsyncImage(model = book.thumbnail, contentDescription = "Portada de ${book.title}", contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surfaceVariant))
        Spacer(Modifier.height(9.dp))
        Text(book.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Text(book.authorLine, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
    if (expanded) AlertDialog(onDismissRequest = { expanded = false }, title = { Text(book.title) },
        text = { Text(description?.takeIf(String::isNotBlank)?.take(600) ?: book.authorLine) }, confirmButton = { TextButton(onClick = { expanded = false }) { Text("Cerrar") } })
}
