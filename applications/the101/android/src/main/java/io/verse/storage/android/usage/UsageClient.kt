package io.verse.storage.android.usage

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.tagd.langx.isNull
import io.verse.storage.android.MyApplicationTheme
import io.verse.storage.android.domain.dao.BookDao
import io.verse.storage.android.domain.dto.AuthorDto
import io.verse.storage.android.domain.dto.BookDto
import io.verse.storage.android.domain.dto.BookStatsDto

@Composable
fun UsageClient(dao: BookDao) {
    MyApplicationTheme {
        MyApplicationTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                var books by remember { mutableStateOf(emptyList<BookDto>()) }
                var showEditDialog by remember { mutableStateOf(false) }

                fun fetchAll() {
                    dao.getAllAsync {
                        books = it
                    }
                }

                Scaffold(
                    floatingActionButton = {
                        SqlDemoFab(
                            onAddClicked = { showEditDialog = true },
                            onRefreshClicked = ::fetchAll
                        )
                    }
                ) { contentPadding ->
                    BookListing(
                        modifier = Modifier.padding(contentPadding),
                        books = books,
                        dao = dao,
                        fetchAll = ::fetchAll
                    )
                }

                if (showEditDialog) {
                    BookAddOrEdit(
                        dao = dao,
                        fetchAll = ::fetchAll,
                    ) {
                        showEditDialog = false
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
private fun BookListing(
    modifier: Modifier = Modifier,
    books: List<BookDto>,
    dao: BookDao,
    fetchAll: () -> Unit,
) {
    var selectedBook by remember { mutableStateOf<BookDto?>(null) }
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(books.count()) {
            val book = books[it]
            val dismissState = rememberDismissState(
                confirmStateChange = {
                    dao.deleteCascadeAsync(book.bookId
                    ) {
                        fetchAll()
                    }
                    true
                }
            )
            SwipeToDismiss(state = dismissState, background = {}) {
                BookCard(
                    modifier = Modifier.combinedClickable(
                        onLongClick = {
                            selectedBook = book
                        },
                        onClick = {
                            dao.deleteAsync(book.bookId) { fetchAll() }
                        }
                    ),
                    bookDto = book
                )
            }
        }
    }
    if (!selectedBook.isNull()) {
        BookAddOrEdit(
            bookDto = selectedBook,
            dao = dao,
            fetchAll = fetchAll,
        ) {
            selectedBook = null
        }
    }
}

@Composable
private fun BookAddOrEdit(
    bookDto: BookDto? = null,
    dao: BookDao,
    fetchAll: () -> Unit,
    onDismissRequest: () -> Unit,
) {

    var bookId by remember { mutableStateOf(bookDto?.bookId.orEmpty()) }
    var bookName by remember { mutableStateOf(bookDto?.bookName.orEmpty()) }
    var author by remember { mutableStateOf(bookDto?.authorDto?.authorId.orEmpty()) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {

                OutlinedTextField(
                    label = { Text(text = "Book ID") },
                    value = bookId,
                    onValueChange = { bookId = it },
                    readOnly = !bookDto.isNull()
                )
                OutlinedTextField(
                    value = bookName,
                    onValueChange = { bookName = it },
                    label = { Text(text = "Book Name") },
                )
                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text(text = "Author") },
                )

                TextButton(
                    modifier = Modifier.align(Alignment.End),
                    onClick = {
                        if (bookDto == null) {
                            bookId.takeIf { it.isNotBlank() }?.let {
                                val newBook = newBookDto(it.trim(), bookName.trim(), author.trim())
                                dao.createAsync(newBook) {
                                    onDismissRequest()
                                    fetchAll()
                                }
                            }
                        } else {
                            val updatedBook = newBookDto(bookId, bookName.trim(), author.trim())
                            dao.updateAsync(updatedBook) {
                                onDismissRequest()
                                fetchAll()
                            }
                        }
                    }
                ) {
                    Text(text = "Ok")
                }
            }
        }
    }
}

@Composable
private fun SqlDemoFab(
    onRefreshClicked: () -> Unit,
    onAddClicked: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        FloatingActionButton(
            modifier = Modifier.size(40.dp),
            onClick = onAddClicked
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
        FloatingActionButton(onClick = onRefreshClicked) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
        }
    }
}


@Composable
fun BookCard(
    modifier: Modifier = Modifier,
    bookDto: BookDto,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Text(text = bookDto.bookName, style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(2.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "By: ${bookDto.authorDto.authorName}", style = MaterialTheme.typography.body2)
            Text(text = "UID: ${bookDto.bookId}", style = MaterialTheme.typography.body2)
        }
    }
}

fun newBookDto(id: String, name: String, author: String): BookDto {
    return BookDto(id, name, BookStatsDto(200), AuthorDto(author, author))
}

@Preview(showBackground = true)
@Composable
fun BookCardPreView() {
    BookCard(bookDto = newBookDto("1", "Harry Potter", "J.K. Rowling"))
}
