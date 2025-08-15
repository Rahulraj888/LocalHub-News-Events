package com.example.newsevents

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsevents.ui.feed.FeedScreen
import com.example.newsevents.ui.feed.FeedViewModel

private const val ROUTE_FEED = "feed"

@Composable
fun LocalHubNavHost(feedViewModel: FeedViewModel) {
    val nav = rememberNavController()
    val ctx = LocalContext.current
    // Build once to avoid recreating the intent on every recomposition
    val customTabs = remember { CustomTabsIntent.Builder().build() }

    NavHost(navController = nav, startDestination = ROUTE_FEED) {
        composable(ROUTE_FEED) {
            FeedScreen(
                viewModel = feedViewModel,
                onOpenLink = { url ->
                    customTabs.launchUrl(ctx, Uri.parse(url))
                }
            )
        }
    }
}
