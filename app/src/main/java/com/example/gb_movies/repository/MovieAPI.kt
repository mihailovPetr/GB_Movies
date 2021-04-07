package com.example.gb_movies.repository

import com.example.gb_movies.model.MovieDTO
import com.example.gb_movies.model.MoviesDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {
    @GET("3/movie/{movie_id}")
    fun getMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "ru-RU",
        @Query("region") region: String = "RU",
    ): Call<MovieDTO>

//    @GET("3/movie/{option}")
//    fun getMovies(
//        @Path("option") option: String,
//        @Query("api_key") apiKey: String,
//        @Query("language") language: String = "ru-RU",
//        @Query("region") region: String = "RU",
//    ): Call<List<MovieDTO>>

    //TODO
    @GET("3/{path}}")
    fun getMovies(
        @Path("path") path: String,
        @Query("api_key") apiKey: String
    ): Call<MoviesDTO>
}