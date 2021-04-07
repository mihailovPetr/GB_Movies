package com.example.gb_movies.repository

import com.example.gb_movies.model.MovieDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MovieAPI {
   @GET("3/movie")
   fun getWeather(
       @Header("X-Yandex-API-Key") token: String,
       @Query("lat") lat: Double,
       @Query("lon") lon: Double
   ): Call<MovieDTO>
}
