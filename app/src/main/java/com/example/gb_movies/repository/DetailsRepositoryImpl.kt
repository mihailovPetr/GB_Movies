package com.example.gb_movies.repository

import com.example.gb_movies.model.MovieDTO
import retrofit2.Callback

class DetailsRepositoryImpl(private val remoteDataSource: RemoteDataSource) : DetailsRepository {

    override fun getMovieDetailsFromServer(movieId: Int, callback: Callback<MovieDTO>) {
        remoteDataSource.geMovieDetails(movieId, callback)
    }
}