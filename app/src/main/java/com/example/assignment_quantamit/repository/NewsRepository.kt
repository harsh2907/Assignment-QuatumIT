package com.example.assignment_quantamit.repository

import com.example.assignment_quantamit.remote_data.NewsDTO
import com.example.assignment_quantamit.remote_data.NewsResponse
import com.example.assignment_quantamit.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class NewsRepository(
    private val api: NewsResponse
) {
     suspend fun getNews(): Flow<Response<NewsDTO>> = flow {
        emit(Response.Loading())
        try {
            val response = api.getHeadlines()
            emit(Response.Success(response))
        }catch (e: HttpException){
            emit(Response.Error(message = "Oops, something went wrong"))
        }
        catch (e: IOException){
            emit(Response.Error(message = "Couldn't reach server check your internet connection"))
        }
    }

     suspend fun searchNews(query: String): Flow<Response<NewsDTO>> = flow {
        emit(Response.Loading())
        try {
            val response = api.getSearchedNews(query=query)
            emit(Response.Success(response))
        }catch (e: HttpException){
            emit(Response.Error(message = "Oops, something went wrong"))
        }
        catch (e: IOException){
            emit(Response.Error(message = "Couldn't reach server check your internet connection"))
        }

    }

}