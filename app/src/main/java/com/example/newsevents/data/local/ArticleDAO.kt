package com.example.newsevents.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsevents.data.model.ArticleEntity

@Dao
interface ArticleDao {

    // Stable ordering helps Paging avoid jitter when timestamps tie
    @Query("SELECT * FROM articles ORDER BY publishedAt DESC, id DESC")
    fun pagingSource(): PagingSource<Int, ArticleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<ArticleEntity>)

    @Query("DELETE FROM articles")
    suspend fun clearAll()

    // Handy to verify that rows are being written
    @Query("SELECT COUNT(*) FROM articles")
    suspend fun count(): Int
}
