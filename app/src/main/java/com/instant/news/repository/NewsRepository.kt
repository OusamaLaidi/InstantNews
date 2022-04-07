package com.instant.news.repository

import com.instant.news.api.ApiInstance

class NewsRepository {
    suspend fun getTopHeadlines(page: Int) =
        ApiInstance.news.topHeadlines(page=page)


    suspend fun getByQuery(searchQuery: String, page: Int) =
        ApiInstance.news.getByQuery(searchQuery=searchQuery, page=page)
}