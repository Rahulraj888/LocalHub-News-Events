package com.example.newsevents.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsevents.data.model.ArticleEntity

@Database(entities = [ArticleEntity::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}
