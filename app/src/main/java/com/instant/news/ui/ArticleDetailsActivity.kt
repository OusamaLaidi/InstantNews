package com.instant.news.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.instant.news.R
import com.instant.news.models.Article
import com.instant.news.utils.TAG
import com.instant.news.utils.getFaviconUrl
import kotlinx.android.synthetic.main.activity_article_details.*
import kotlinx.android.synthetic.main.item_article_preview.articleDate
import kotlinx.android.synthetic.main.item_article_preview.articleDescription
import kotlinx.android.synthetic.main.item_article_preview.articleImage
import kotlinx.android.synthetic.main.item_article_preview.articleSource
import kotlinx.android.synthetic.main.item_article_preview.articleSourceIcon
import kotlinx.android.synthetic.main.item_article_preview.articleTitle
import java.text.DateFormat


class ArticleDetailsActivity : AppCompatActivity() {
    private var article:Article?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_details)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        article = intent.getSerializableExtra(ARTICLE_DETAIL) as? Article
        article?.let { setContent(it) }
    }

    @SuppressLint("SetTextI18n")
    fun setContent(article:Article){
        Glide.with(this)
            .load(article.urlToImage)
            .into(articleImage)
        Glide.with(this)
            .load(getFaviconUrl(article.url))
            .into(articleSourceIcon)
        articleSource.text = article.source.name

        articleAuthor.text = article.author
        articleTitle.text = article.title
        articleDescription.text = (article.description?:"") +"\n\n"+ (article.content?:"")

        val df: DateFormat = DateFormat.getDateInstance(DateFormat.LONG)
        articleDate.text = df.format(article.publishedAt)
    }

    fun openLink(v: View){
        val url = if(article?.url != null) article?.url else {
            Log.e(TAG, "openLink: article url is null" )
            return
        }

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected: ${item.itemId}")
        if(item.itemId == android.R.id.home){
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}