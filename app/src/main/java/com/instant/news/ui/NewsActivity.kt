package com.instant.news.ui

import android.app.ActivityOptions
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.instant.news.R
import com.instant.news.adapters.ArticleAdapter
import com.instant.news.models.Article
import com.instant.news.models.NewsApiResponse
import com.instant.news.repository.NewsRepository
import com.instant.news.utils.Resource
import com.instant.news.utils.TAG
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.item_article_preview.*
import android.util.Pair as UtilPair

const val ARTICLE_DETAIL = "article_detail"

class NewsActivity : AppCompatActivity() {
    private lateinit var newsAdapter: ArticleAdapter
    private val newsViewModel by viewModels<NewsViewModel> {
        NewsViewModelProviderFactory(Application(), NewsRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        initAdapter()

        swipeRefreshController.setOnRefreshListener {
            Log.d(TAG, "onCreate: ${swipeRefreshController.isRefreshing}")
            newsViewModel.resetNews()
            swipeRefreshController.isRefreshing = false
        }

        newsViewModel.getNews()
    }

    private fun initAdapter(){
        newsAdapter = ArticleAdapter{ article -> adapterOnClick(article)}
        recyclerView.adapter=newsAdapter

        recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(Application())
            addOnScrollListener(scrollListener)

            val itemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
            itemDecoration.setDrawable(getDrawable(R.drawable.item_row_divider)!!)
            addItemDecoration(itemDecoration)
        }

        newsViewModel.newsLiveData.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    showErrorMessage(response)
                }
                is Resource.Loading -> {
                    showProgressBar()
                    if (newsViewModel.isLoadingFirstPage()) {
                        newsAdapter.differ.submitList(null)
                    }
                }
            }
        }
    }


    var isLoadError = false
    var isLoading = false

    /**
     * Gestion de la pagination du listing
     * Lancer le chargement de la prochaine page dès qu'il reste 3 item
     */
    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if(isLoadError || isLoading){
                return
            }
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager

            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            val totalItemCount = layoutManager.itemCount

            if(lastVisibleItemPosition >= totalItemCount - 3){
                newsViewModel.getNews()
            }
        }

    }

    private fun hideProgressBar() {
        isLoading = false
        activityIndicator.visibility = View.GONE
    }

    private fun showProgressBar() {
        isLoading = true
        hideErrorMessage()
        activityIndicator.visibility = View.VISIBLE
    }

    // Vider le listing en cas d'erreur
    private fun showErrorMessage(response: Resource.Error<NewsApiResponse>) {
        isLoadError = true
        newsAdapter.differ.submitList(null)
        errorLoadView.visibility = View.VISIBLE
        tvErrorLoad.text = when(response.errorCode){
            ERROR_NETWORK-> getString(R.string.error_load_network)
            ERROR_API, "", null -> getString(R.string.error_load_api)
            else -> getString(R.string.error_api_unauthorized, response.errorCode)
        }
    }

    private fun hideErrorMessage() {
        errorLoadView.visibility = View.GONE
        isLoadError = false
    }

    private fun adapterOnClick(article: Article) {
        val intent = Intent(this,ArticleDetailsActivity::class.java)
        intent.putExtra(ARTICLE_DETAIL, article)

        val options = ActivityOptions.makeSceneTransitionAnimation(this,
            UtilPair.create(articleImage, "articleImage"),
            UtilPair.create(articleTitle, "articleTitle")
        )

        startActivity(intent, options.toBundle())

    }
}