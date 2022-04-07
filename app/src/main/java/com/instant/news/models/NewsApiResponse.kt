package com.instant.news.models

data class NewsApiResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)