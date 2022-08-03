package com.example.assignment_quantamit.remote_data

data class NewsDTO(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)