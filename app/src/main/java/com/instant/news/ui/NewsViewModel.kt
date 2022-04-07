package com.instant.news.ui

import android.app.Application
import androidx.lifecycle.*
import com.instant.news.models.NewsApiResponse
import com.instant.news.repository.NewsRepository
import com.instant.news.utils.API_LISTING_PAGE_SIZE
import com.instant.news.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

const val ERROR_API: String = "api_error"
const val ERROR_NETWORK: String = "api_network_error"

class NewsViewModel(app: Application, private val newsRepository: NewsRepository):  AndroidViewModel(app){
    val newsLiveData: MutableLiveData<Resource<NewsApiResponse>> = MutableLiveData()

    private var stopPagination: Boolean=false
    private var currentPage = 1
    private var newsResponse: NewsApiResponse? = null

    private var newsFilter: String? = null

    fun resetNews(){
        stopPagination = false
        currentPage = 1
        newsResponse = null
        getNews()
    }

    fun isLoadingFirstPage():Boolean{
        return currentPage == 1
    }

    fun getNews() {
        if(stopPagination){
            return
        }
        viewModelScope.launch {
            newsLiveData.postValue(Resource.Loading())
            try {
                val response = if(newsFilter == null) {
                    newsRepository.getTopHeadlines(currentPage)
                } else {
                    newsRepository.getByQuery(newsFilter!!, currentPage)
                }
                newsLiveData.postValue(handleNewsResponse(response))

            } catch(t: Throwable) {
                when(t) {
                    is IOException -> newsLiveData.postValue(Resource.Error(ERROR_NETWORK))
                    else -> newsLiveData.postValue(Resource.Error(ERROR_API))
                }
            }
        }
    }


    private fun handleNewsResponse(response: Response<NewsApiResponse>) : Resource<NewsApiResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                stopPagination = resultResponse.totalResults <= API_LISTING_PAGE_SIZE * currentPage
                if(newsResponse == null) {
                    newsResponse = resultResponse
                } else {
                    val oldArticles = newsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                currentPage++
                return Resource.Success(newsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.code().toString())
    }
}

class NewsViewModelProviderFactory(
    private val app: Application,
    private val newsRepository: NewsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app, newsRepository) as T
    }
}