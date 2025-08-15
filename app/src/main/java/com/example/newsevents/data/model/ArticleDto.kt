package com.example.newsevents.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleDto(
    val id: Long,
    val title: String,
    val url: String,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("news_site") val sourceName: String? = null,
    val summary: String? = null,
    @SerialName("published_at") val publishedAt: String,
    @SerialName("updated_at") val updatedAt: String? = null
)

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val summary: String?,
    val imageUrl: String?,
    val publishedAt: String,
    val sourceName: String?,
    val url: String
)

fun ArticleDto.toEntity() = ArticleEntity(
    id = id,
    title = title,
    summary = summary,
    imageUrl = imageUrl,
    publishedAt = publishedAt,
    sourceName = sourceName,
    url = url
)
