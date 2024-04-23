package com.example.newsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class NewsViewModel(app:Application, val newsRepository: NewsRepository):AndroidViewModel(app) {
    val headlines:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var headlinesPage=1
    var headlineResponse:NewsResponse?=null

    val searchNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage=1
    var searchNewsResponse:NewsResponse?=null
    var newSearchQuery:String?=null
    var oldSearchQuery:String?=null

    init {
        getHeadlines("in")
    }

    fun getHeadlines(countryCode: String)=viewModelScope.launch {
        headlinesInternet(countryCode)
    }
    fun searchNews(searchQuery:String)=viewModelScope.launch {
        searchNewsInternet(searchQuery)
    }

    //here we are handling response for headline fragment
    //i.e what should be done if we get a success response for headline page
    //it will take response as a parameter that we will get from getHeadlines and give resource as output
    private fun handleHeadlineResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {resultResponse->
                headlinesPage++
                if(headlineResponse==null) headlineResponse=resultResponse
                else{
                    val oldArticles=headlineResponse?.articles
                    val newArticles=resultResponse.articles
                    oldArticles?.addAll(newArticles)
//                    headlineResponse?.articles=oldArticles
                }
                return Resource.Success(headlineResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchResponse(response:Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {resultResponse->
                if(searchNewsResponse==null || newSearchQuery!=oldSearchQuery){
                    searchNewsPage=1
                    oldSearchQuery=newSearchQuery
                    searchNewsResponse=resultResponse
                }
                else{
                    searchNewsPage++
                    val oldArticles=searchNewsResponse?.articles
                    val newArticles=resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    fun addToFavourites(article: Article)=viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getFavouriteNews()=newsRepository.getFavouriteNews()

    fun deleteArticle(article: Article)=viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    //here we check if device is connected to the internet
    fun internetConnection(context: Context):Boolean{
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when{
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)->true
                    else->false
                }
            }?:false
        }
    }

    //https://youtu.be/8ygz_fZuyow?list=PLQ_Ai1O7sMV3ZUWdh5gnvxNxNewx9Vacz&t=695 - explanation
    //here we get data from API and store it in headlines after processing the response
    private suspend fun headlinesInternet(countryCode:String){
        headlines.postValue(Resource.Loading())
        try {
            if(internetConnection(this.getApplication())){
                val response=newsRepository.getHeadlines(countryCode, headlinesPage)
                headlines.postValue(handleHeadlineResponse(response))
            }
            else{
                headlines.postValue(Resource.Error("No Internet Connection!!!"))
            }
        }
        catch (t:Throwable){
            when(t){
                is IOException->headlines.postValue(Resource.Error(""))
                else ->headlines.postValue(Resource.Error("No Signal"))
            }
        }
    }

    private suspend fun searchNewsInternet(searchQuery:String){
        newSearchQuery=searchQuery
        searchNews.postValue(Resource.Loading())
        try {
            if(internetConnection(this.getApplication())){
                val response=newsRepository.searchNews(searchQuery,searchNewsPage)
                searchNews.postValue(handleSearchResponse(response))
            }
            else{
                searchNews.postValue(Resource.Error("No Internet Connection!!"))
            }
        }
        catch (t:Throwable){
            when(t){
                is IOException->headlines.postValue(Resource.Error(""))
                else ->headlines.postValue(Resource.Error("No Signal"))
            }
        }
    }
}