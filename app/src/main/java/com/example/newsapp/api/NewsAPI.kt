package com.example.newsapp.api

import com.example.newsapp.util.Constants.Companion.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

//We have created the response as data class using JSON to kotlin plugin
//now this is the request in which we will alter the parameters of API URL according to need
interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country")
        countryCode:String="in",
        @Query("page")
        pageNumber:Int=1,
        @Query("apikey")
        apikey:String=API_KEY
    )

}