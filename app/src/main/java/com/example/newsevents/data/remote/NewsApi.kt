package com.example.newsevents.data.remote

import com.example.newsevents.data.model.ArticleDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v4/articles/")
    suspend fun getArticles(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,          // Spaceflight v4 uses snake_case
        @Query("ordering") ordering: String = "-published_at"
    ): ApiPage<ArticleDto>                          // Return DTOs; map to Entity downstream
}
