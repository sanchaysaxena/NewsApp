package com.example.newsapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.models.Article
import retrofit2.http.DELETE

@Dao
interface ArticleDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article:Article):Long

    @Query("SELECT * FROM ARTICLES")
    fun getAllArticles():LiveData<List<Article>>

    @DELETE
    suspend fun deleteArticle(article: Article)
}

//onConflict is used so that if same primary key already exist then it will replace old data with new data
//suspend indicates that it should be called using a coroutine
