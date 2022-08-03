package com.example.assignment_quantamit.utils

import com.example.assignment_quantamit.remote_data.Article

data class NewsState(
    val news: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)
