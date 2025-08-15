package com.example.newsevents.ui.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.example.newsevents.data.model.ArticleEntity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.shape.RoundedCornerShape

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun FeedScreen(
    viewModel: FeedViewModel,
    onOpenLink: (String) -> Unit
) {
    val articles = viewModel.feed.collectAsLazyPagingItems()
    FeedList(articles, onOpenLink)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun FeedList(
    articles: LazyPagingItems<ArticleEntity>,
    onOpenLink: (String) -> Unit
) {
    var refreshing by remember { mutableStateOf(false) }
    val refreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = { articles.refresh() }
    )
    // Keep this in sync with Paging refresh state
    refreshing = articles.loadState.refresh is LoadState.Loading

    Scaffold(
        topBar = { TopAppBar(title = { Text("LocalHub") }) }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .pullRefresh(refreshState)
        ) {
            // Initial load state (avoid blank screen on error)
            when (val r = articles.loadState.refresh) {
                is LoadState.Loading -> {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(16.dp)
                    )
                }
                is LoadState.Error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    ) {
                        Text("Failed to load: ${r.error.localizedMessage ?: "Unknown error"}")
                        TextButton(onClick = { articles.retry() }) { Text("Retry") }
                    }
                }
                else -> Unit
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // Index-based API (no paging-compose items import needed)
                items(
                    count = articles.itemCount,
                    key = { index -> articles.peek(index)?.id ?: "placeholder-$index" },
                    contentType = { index -> if (articles.peek(index) == null) "placeholder" else "article" }
                ) { index ->
                    val article = articles[index]
                    if (article != null) {
                        ArticleRow(article, onOpenLink)
                    } else {
                        PlaceholderRow()
                    }
                    Divider()
                }

                when (val state = articles.loadState.append) {
                    is LoadState.Loading -> item {
                        LinearProgressIndicator(Modifier.padding(16.dp))
                    }
                    is LoadState.Error -> item {
                        TextButton(
                            onClick = { articles.retry() },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Retry loading more…")
                        }
                    }
                    else -> Unit
                }
            }

            PullRefreshIndicator(
                refreshing = refreshing,
                state = refreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun ArticleRow(article: ArticleEntity, onOpenLink: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = article.imageUrl ?: "",
            contentDescription = null,
            modifier = Modifier
                .size(56.dp) // explicit size avoids bad constraints
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = article.summary.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = { onOpenLink(article.url) }) {
                Text("Read")
            }
        }
    }
}

// Simple placeholder while items are being paged in.
@Composable
private fun PlaceholderRow() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("Loading…", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text("Please wait", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
