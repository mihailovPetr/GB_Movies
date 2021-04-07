package com.example.gb_movies.viewmodel

import com.example.gb_movies.model.MoviesGroup

sealed class AppState {
    data class Success(val moviesData: List<MoviesGroup>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
