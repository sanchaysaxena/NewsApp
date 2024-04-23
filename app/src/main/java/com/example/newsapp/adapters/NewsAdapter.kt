package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.ItemNewsBinding
import com.example.newsapp.models.Article

class NewsAdapter:RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
    inner class ArticleViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    lateinit var articleImage:ImageView
    lateinit var articleSource:TextView
    lateinit var articleTitle: TextView
    lateinit var articleDescription: TextView
    lateinit var articleDateTime: TextView


    // differ callback is used to efficiently update the contents of a recycler view without refreshing the entire dataset
    private val differCallback=object :DiffUtil.ItemCallback<Article>(){
        //Diff util is used to determine the difference between 2 lists
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            //check weather 2 items points to same item in dataclass
            return oldItem.url==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            //check weather the contents of 2 items are same
            return oldItem==newItem
        }
    }
    val differ=AsyncListDiffer(this,differCallback)
    //async list differ determine difference between old and new list on background thread


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener:((Article)->Unit)?=null

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article=differ.currentList[position]

        articleImage=holder.itemView.findViewById(R.id.articleImage)
        articleSource=holder.itemView.findViewById(R.id.articleSource)
        articleTitle = holder.itemView.findViewById(R.id.articleTitle)
        articleDescription = holder.itemView.findViewById(R.id.articleDescription)
        articleDateTime = holder.itemView.findViewById(R.id.articleDateTime)

        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(articleImage)
            articleSource.text=article.source?.name
            articleTitle.text=article.title
            articleDescription.text=article.description
            articleDateTime.text=article.publishedAt

            setOnClickListener{
                onItemClickListener?.let {
                    it(article)
                }
            }
        }
    }
    fun setOnItemClickListener(listener:(Article)->Unit){
        onItemClickListener=listener
    }
}