package com.example.gb_movies.repository

import com.example.gb_movies.model.MovieDTO

interface DetailsRepository {
    fun getMovieDetailsFromServer(
        movieId: Int,
        callback: retrofit2.Callback<MovieDTO>
    )
}
