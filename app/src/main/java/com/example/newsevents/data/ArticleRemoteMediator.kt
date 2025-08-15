package com.example.newsevents.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.newsevents.data.local.AppDatabase
import com.example.newsevents.data.local.RemoteKeys
import com.example.newsevents.data.model.ArticleEntity
import com.example.newsevents.data.model.toEntity
import com.example.newsevents.data.remote.NewsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class ArticlesRemoteMediator(
    private val db: AppDatabase,
    private val api: NewsApi,
    private val pageSize: Int = 20
) : RemoteMediator<Int, ArticleEntity>() {

    override suspend fun initialize() = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult = withContext(Dispatchers.IO) {
        try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> {
                    // Not paging upward in this feed
                    return@withContext MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    val lastKey = lastItem?.let { db.remoteKeysDao().remoteKeysByArticleId(it.id) }
                    val next = lastKey?.nextKey
                    if (next == null) {
                        return@withContext MediatorResult.Success(endOfPaginationReached = lastKey != null)
                    }
                    next
                }
            }

            // Spaceflight v4 expects page_size (snake_case)
            val resp = api.getArticles(page = page, pageSize = pageSize)
            val dtos = resp.results
            val entities = dtos.map { it.toEntity() }
            val endReached = resp.next == null

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeysDao().clearAll()
                    db.articleDao().clearAll()
                }

                val keys = dtos.map { dto ->
                    RemoteKeys(
                        articleId = dto.id,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (endReached) null else page + 1
                    )
                }
                db.remoteKeysDao().insertAll(keys)
                db.articleDao().upsertAll(entities)
            }

            Log.d("Mediator", "Inserted=${entities.size} page=$page end=$endReached")
            MediatorResult.Success(endOfPaginationReached = endReached)
        } catch (t: Throwable) {
            Log.e("Mediator", "load failed", t)
            MediatorResult.Error(t)
        }
    }
}
