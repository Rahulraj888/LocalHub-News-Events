package com.example.newsevents.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class ApiPage<T>(
    val count: Int,
    val next: String?,       // full URL of next page or null
    val previous: String?,
    val results: List<T>
)
