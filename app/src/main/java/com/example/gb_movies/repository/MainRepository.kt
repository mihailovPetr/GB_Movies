package com.example.gb_movies.repository

import com.example.gb_movies.model.MoviesDTO

interface MainRepository {
    fun getMovies(path: String, callback: retrofit2.Callback<MoviesDTO>)
    //fun getMovieGroupsFromLocalStorage(): List<List<Movie>>
}
