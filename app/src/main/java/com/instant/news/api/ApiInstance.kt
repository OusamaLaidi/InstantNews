package com.instant.news.api

import com.instant.news.utils.API_BASE_URL
import com.instant.news.utils.API_AUTHORISATION_KEY
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiInstance {
    companion object {
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(HeaderAuthorisationInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build()
            Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val news: NewsApi by lazy {
            retrofit.create(NewsApi::class.java)
        }

    }

    object  HeaderAuthorisationInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Authorization", API_AUTHORISATION_KEY)
            val request = requestBuilder.build()
            return chain.proceed(request)
        }
    }
}