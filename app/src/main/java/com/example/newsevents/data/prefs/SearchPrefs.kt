package com.example.newsevents.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException

private val Context.dataStore by preferencesDataStore("localhub_prefs")
private val KEY_RECENT_JSON = stringPreferencesKey("recent_queries_json")
private const val MAX_RECENT = 10

class SearchPrefs(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }

    // Always emits a list in insertion (most-recent-first) order
    val recent: Flow<List<String>> = context.dataStore.data
        .catch { e ->
            if (e is IOException) emit(emptyPreferences()) else throw e
        }
        .map { prefs ->
            val raw = prefs[KEY_RECENT_JSON] ?: "[]"
            runCatching { json.decodeFromString<List<String>>(raw) }.getOrElse { emptyList() }
        }

    // Push a query to the top, de-dupe (case-insensitive), cap at MAX_RECENT
    suspend fun push(query: String) {
        val q = query.trim()
        if (q.isEmpty()) return

        context.dataStore.edit { prefs ->
            val current = prefs[KEY_RECENT_JSON]?.let {
                runCatching { json.decodeFromString<List<String>>(it) }.getOrElse { emptyList() }
            } ?: emptyList()

            val updated = buildList {
                add(q) // most recent first
                current.forEach { if (!it.equals(q, ignoreCase = true)) add(it) }
            }.take(MAX_RECENT)

            prefs[KEY_RECENT_JSON] = json.encodeToString(updated)
        }
    }
}
