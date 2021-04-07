package com.example.gb_movies.model

data class MoviesGroup(
    val title: String,
    val description: String = "",
    val request: String = "",
    var movies: List<Movie>? = null
)