    package kz.aitu.booksapp.data.repo

    import kz.aitu.booksapp.data.local.BookDao
    import kz.aitu.booksapp.data.local.BookEntity
    import kz.aitu.booksapp.data.remote.GoogleBooksRepository
    import kz.aitu.booksapp.domain.model.Book
    import kotlinx.coroutines.flow.Flow
    import kotlinx.coroutines.flow.map

    class FeedRepository(
        private val dao: BookDao,
        private val api: GoogleBooksRepository
    ) {
        fun observeFeed(): Flow<List<Book>> =
            dao.observeAll().map { list -> list.map { it.toDomain() } }

        suspend fun isCacheEmpty(): Boolean = dao.count() == 0

        /**
         * Sync policy (simple + reliable):
         * - If online: fetch fresh feed and REPLACE by id (no duplicates).
         * - If offline: exception -> UI shows cache (if exists).
         */
        suspend fun refreshFeed() {
            val result = api.search(query = "subject:programming", page = 0, pageSize = 20)
            val now = System.currentTimeMillis()

            val entities = result.items.map { it.toEntity(cachedAt = now) }

            dao.insertAll(entities)
        }
    }

    private fun BookEntity.toDomain(): Book = Book(
        id = id,
        title = title,
        authors = authors,
        thumbnailUrl = thumbnailUrl,
        description = description
    )

    private fun Book.toEntity(cachedAt: Long): BookEntity = BookEntity(
        id = id,
        title = title,
        authors = authors,
        thumbnailUrl = thumbnailUrl,
        description = description,
        cachedAt = cachedAt
    )
