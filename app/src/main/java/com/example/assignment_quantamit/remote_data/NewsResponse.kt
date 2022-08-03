package com.example.assignment_quantamit.remote_data

import com.example.thelastnews.data_source.remote.utils.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsResponse {

    @GET("/v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country") country:String = "in",
        @Query("apiKey") apiKey:String = API_KEY
    ): NewsDTO

    @GET("/v2/everything")
    suspend fun getSearchedNews(
        @Query("q") query:String ,
        @Query("apiKey") apiKey:String = API_KEY
    ): NewsDTO

}