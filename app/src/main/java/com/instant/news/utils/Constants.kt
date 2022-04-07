package com.instant.news.utils

import com.instant.news.utils.Keys.apiKey

const val TAG = "InstantNewsTag"

val API_AUTHORISATION_KEY = apiKey()
const val API_BASE_URL = "https://newsapi.org"
//const val API_BASE_URL = "http://192.168.1.2:4301"
const val API_LISTING_PAGE_SIZE = 1


object Keys {
    init {
        System.loadLibrary("native-lib")
    }
    external fun apiKey(): String
}