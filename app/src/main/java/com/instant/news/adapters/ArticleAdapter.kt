package com.instant.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.instant.news.R
import com.instant.news.models.Article
import com.instant.news.utils.getFaviconUrl
import kotlinx.android.synthetic.main.item_article_preview.view.*
import java.text.DateFormat


class ArticleAdapter(private val onClick: (Article) -> Unit) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)


    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article_preview,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this)
                .load(article.urlToImage)
                .into(articleImage)
            articleTitle.text = article.title
            articleDescription.text = article.description

            Glide.with(this)
                .load(getFaviconUrl(article.url))
                .into(articleSourceIcon)
            articleSource.text = article.source.name

            val df: DateFormat = DateFormat.getDateInstance(DateFormat.LONG)
            articleDate.text = df.format(article.publishedAt)


            setOnClickListener {
                onClick(article)
            }
        }
    }

}













