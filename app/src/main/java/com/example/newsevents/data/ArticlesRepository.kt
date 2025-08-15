package com.example.newsevents.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.newsevents.data.local.AppDatabase
import com.example.newsevents.data.model.ArticleEntity
import com.example.newsevents.data.remote.NewsApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticlesRepository @Inject constructor(
    private val db: AppDatabase,
    private val api: NewsApi
) {
    private companion object {
        const val PAGE_SIZE = 20
    }

    @OptIn(ExperimentalPagingApi::class)
    fun pagedFeed(): Flow<PagingData<ArticleEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE / 2,
                enablePlaceholders = false
            ),
            remoteMediator = ArticlesRemoteMediator(db, api, pageSize = PAGE_SIZE),
            pagingSourceFactory = { db.articleDao().pagingSource() }
        ).flow
}
