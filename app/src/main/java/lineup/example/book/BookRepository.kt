package lineup.example.book

import lineup.example.BaseRepository


class BookRepository : BaseRepository() {
    suspend fun getAllBooks(): APIResult<List<BookResponse>> {
        return handleRequest { Api.book().getAllBooks() }
    }
}