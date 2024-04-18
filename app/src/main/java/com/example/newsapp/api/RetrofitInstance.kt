package com.example.newsapp.api

import com.example.newsapp.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

//this is a singleton clas
//Definition of singleton class - Singleton class is a class that is defined in such a way that only
//one instance of the class can be created and used everywhere.
//Many times we create the two different objects of the same class,
//but we have to remember that creating two different objects also requires the allocation of two different memories for the objects.
//So it is better to make a single object and use it again and again
class RetrofitInstance {
    companion object{
        private val retrofit by lazy{
        val logging = HttpLoggingInterceptor()
        logging. setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient. Builder()
            .addInterceptor(logging)
            .build()
        Retrofit. Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        }
        val api by lazy{
            retrofit.create(NewsAPI::class.java)
        }
        //lazy->instance will only be created when it is first accessed
        //HTTP logging interceptor->lop HTTP requests and response details
        //OkHTTPClient->created using above interceptor
        //Retrofit builder->used to configure the base URL, GSON convertor set OKHTTP client and build the instance
        //API by lazy -> creates an implementation of the NewsAPI interface using retrofit
    }
}