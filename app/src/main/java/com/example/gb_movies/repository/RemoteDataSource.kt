package com.example.gb_movies.repository

import com.example.gb_movies.BuildConfig
import com.example.gb_movies.model.MovieDTO
import com.example.gb_movies.model.MoviesDTO
import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {

    private val movieApi = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().create()
            )
        )
        //.client(createOkHttpClient(WeatherApiInterceptor()))
        .build().create(MovieAPI::class.java)

    fun geMovieDetails(movieId: Int, callback: Callback<MovieDTO>) {
        movieApi.getMovie(movieId, BuildConfig.TMDB_API_KEY).enqueue(callback)
    }

    fun geMovies(path: String, callback: Callback<MoviesDTO>) {
        movieApi.getMovies(path, BuildConfig.TMDB_API_KEY).enqueue(callback)
    }





//    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
//        val httpClient = OkHttpClient.Builder()
//        httpClient.addInterceptor(interceptor)
//        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//        return httpClient.build()
//    }
//
//    inner class MovieApiInterceptor : Interceptor {
//
//        @Throws(IOException::class)
//        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
//            return chain.proceed(chain.request())
//        }
//    }
}