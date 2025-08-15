package com.example.newsevents.di

import android.content.Context
import androidx.room.Room
import com.example.newsevents.data.local.AppDatabase
import com.example.newsevents.data.remote.NewsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds
import okhttp3.MediaType.Companion.toMediaType

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .callTimeout(30.seconds)
        .readTimeout(30.seconds)
        .connectTimeout(15.seconds)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        .build()

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder()
            // ✅ Do NOT include /v4/ here, because NewsApi uses @GET("v4/articles/")
            .baseUrl("https://api.spaceflightnewsapi.net/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext app: Context): AppDatabase =
        Room.databaseBuilder(app, AppDatabase::class.java, "localhub.db")
            .fallbackToDestructiveMigration() // helpful during development
            .build()
}
