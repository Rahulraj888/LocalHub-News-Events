package com.example.newsevents.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsevents.data.ArticlesRepository
import com.example.newsevents.data.model.ArticleEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    repo: ArticlesRepository
) : ViewModel() {
    val feed: Flow<PagingData<ArticleEntity>> = repo.pagedFeed().cachedIn(viewModelScope)
}
