package com.example.bookshelf.data

import com.example.bookshelf.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class SearchResponse(val items: List<SearchItem>?)
data class SearchItem(val id: String)
data class VolumeResponse(val id: String?, val volumeInfo: VolumeInfo?)
data class VolumeInfo(val title: String?, val authors: List<String>?, val imageLinks: ImageLinks?, val description: String?)
data class ImageLinks(val thumbnail: String?, val smallThumbnail: String?)

interface BooksApiService {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 24,
        @Query("key") key: String? = BuildConfig.BOOKS_API_KEY.takeIf(String::isNotBlank),
    ): SearchResponse
    @GET("volumes/{volumeId}")
    suspend fun getVolume(
        @Path("volumeId") volumeId: String,
        @Query("key") key: String? = BuildConfig.BOOKS_API_KEY.takeIf(String::isNotBlank),
    ): VolumeResponse

    companion object {
        fun create(): BooksApiService = Retrofit.Builder().baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build().create(BooksApiService::class.java)
    }
}
