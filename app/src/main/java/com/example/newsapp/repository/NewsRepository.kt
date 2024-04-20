package com.example.newsapp.repository

import androidx.lifecycle.Observer
import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.models.Article

class NewsRepository(val db:ArticleDatabase) {
    suspend fun getHeadlines(countryCode:String,pageNumber:Int)=
        RetrofitInstance.api.getHeadlines(countryCode,pageNumber)

    suspend fun searchNews(searchQuery:String,pageNumber: Int)=
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)

    suspend fun upsert(article: Article)=
        db.getArticleDAO().upsert(article)

    fun getFavouriteNews()=db.getArticleDAO().getAllArticles()

    suspend fun deleteArticle(article: Article)=db.getArticleDAO().deleteArticle(article)
}

//Here we have just created functions at one place for all the functions that we have related to DB or API