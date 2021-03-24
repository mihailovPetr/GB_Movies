package com.example.gb_movies.model

interface Repository {
    fun getMovieFromServer(): Movie
    fun getMovieFromLocalStorage(): Movie
}
