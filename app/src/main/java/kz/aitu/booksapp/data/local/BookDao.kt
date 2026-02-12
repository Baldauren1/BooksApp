package kz.aitu.booksapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM books ORDER BY cachedAt DESC")
    fun observeFeed(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books ORDER BY cachedAt DESC")
    suspend fun getFeedOnce(): List<BookEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<BookEntity>)

    @Query("DELETE FROM books")
    suspend fun clearAll()
}
