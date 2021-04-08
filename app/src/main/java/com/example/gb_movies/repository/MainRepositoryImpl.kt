package com.example.gb_movies.repository

import com.example.gb_movies.model.MoviesDTO
import retrofit2.Callback

class MainRepositoryImpl(private val remoteDataSource: RemoteDataSource) : MainRepository {

    override fun getMovies(url: String, callback: Callback<MoviesDTO>) {
        remoteDataSource.geMovies(url, callback)
    }
}