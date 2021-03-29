package com.example.gb_movies.viewmodel

import com.example.gb_movies.model.Movie

sealed class AppState {
    data class Success(val movieData: List<Movie>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
