package com.example.assignment_quantamit.di

import com.example.assignment_quantamit.remote_data.NewsResponse
import com.example.assignment_quantamit.repository.NewsRepository
import com.example.thelastnews.data_source.remote.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit() = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NewsResponse::class.java)

    @Singleton
    @Provides
    fun provideRepository(api:NewsResponse) = NewsRepository(api)

}