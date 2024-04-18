package com.example.newsapp.api

import com.example.newsapp.models.NewsResponse
import com.example.newsapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//We have created the response data class using JSON to kotlin plugin
//now this is the request interface which we will alter the parameters of API URL according to need
//interface are class that are meant to be subclass and they define a common contract for the class that implements it
//here we will implement this using retrofit instance class
interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country")
        countryCode:String="in",
        @Query("page")
        pageNumber:Int=1,
        @Query("apiKey")
        apiKey:String=API_KEY
    ):Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery:String,
        @Query("page")
        pageNumber:Int=1,
        @Query("apiKey")
        apiKey:String= API_KEY
    ):Response<NewsResponse>
}
//these functions will return NewsResponse datatype as response