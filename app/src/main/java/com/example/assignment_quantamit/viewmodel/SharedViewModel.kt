package com.example.assignment_quantamit.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment_quantamit.Screen
import com.example.assignment_quantamit.repository.NewsRepository
import com.example.assignment_quantamit.utils.NewsState
import com.example.assignment_quantamit.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
   private val repo:NewsRepository
):ViewModel() {

    init {
        getHeadlines()
    }

    val loginScreenState: MutableLiveData<Screen> by lazy { MutableLiveData(Screen.Login) }
    val newsState: MutableStateFlow<NewsState> by lazy { MutableStateFlow(NewsState()) }


    fun changeLoginScreenState(state: Screen) {
        loginScreenState.value = state
    }

    fun getHeadlines() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getNews().collect { response ->

                when (response) {
                    is Response.Success -> {
                        newsState.value = newsState.value.copy(
                            news = response.data?.articles ?: emptyList(),
                            isLoading = false,
                            error = ""
                        )
                    }
                    is Response.Loading -> {
                        newsState.value = newsState.value.copy(
                            news = response.data?.articles ?: emptyList(),
                            isLoading = true,
                            error = ""
                        )
                    }
                    is Response.Error -> {
                        newsState.value = newsState.value.copy(
                            news = response.data?.articles ?: emptyList(),
                            isLoading = false,
                            error = response.message!!
                        )
                    }
                }

            }
        }
    }

    fun searchNews(q: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.searchNews(q).collect { response ->

                when (response) {
                    is Response.Success -> {
                        newsState.value = newsState.value.copy(
                            news = response.data?.articles ?: emptyList(),
                            isLoading = false,
                            error = ""
                        )
                    }
                    is Response.Loading -> {
                        newsState.value = newsState.value.copy(
                            news = response.data?.articles ?: emptyList(),
                            isLoading = false,
                            error = ""
                        )
                    }
                    is Response.Error -> {
                        newsState.value = newsState.value.copy(
                            news = response.data?.articles ?: emptyList(),
                            isLoading = true,
                            error = response.message!!
                        )
                    }
                }

            }
        }
    }
}