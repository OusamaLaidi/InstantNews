package com.instant.news.utils

import java.net.URI

fun getFaviconUrl(url:String?): String? {
    if(url == null){
        return null
    }
    val uri = URI(url)
    if(uri.host == null){
        return null
    }

    return "https://${uri.host}/favicon.ico"
}