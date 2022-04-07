package com.instant.news.api


import com.instant.news.models.NewsApiResponse
import com.instant.news.utils.API_LISTING_PAGE_SIZE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/top-headlines")
    suspend fun topHeadlines(
        @Query("country")
        country: String = "fr",
        @Query("pageSize")
        pageSize:Int=API_LISTING_PAGE_SIZE,
        @Query("page")
        page: Int
    ): Response<NewsApiResponse>

    @GET("v2/everything")
    suspend fun getByQuery(
        @Query("language")
        country: String = "fr",
        @Query("pageSize")
        pageSize:Int=API_LISTING_PAGE_SIZE,
        @Query("page")
        page: Int,
        @Query("q")
        searchQuery: String
    ): Response<NewsApiResponse>
}

