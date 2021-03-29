package com.example.gb_movies.model

interface Repository {
    fun getMoviesFromServer(): List<Movie>
    fun getMoviesFromLocalStorage(): List<Movie>
}
