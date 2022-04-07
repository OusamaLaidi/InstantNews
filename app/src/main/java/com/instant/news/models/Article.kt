package com.instant.news.models

import java.io.Serializable
import java.util.*


data class Article(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: Date,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
): Serializable