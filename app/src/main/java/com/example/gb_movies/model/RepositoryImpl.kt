package com.example.gb_movies.model

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.gb_movies.BuildConfig
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class RepositoryImpl : Repository {

    override fun getMovieGroupsFromLocalStorage() = listOf<List<Movie>>()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getMovieGroupsFromServer(requests: List<String>, onLoadedListener: Repository.OnLoadedListener) {
        val movies: ArrayList<List<Movie>> = arrayListOf()

        try {
            for (request in requests){
                movies.add(loadMovies(request))
            }
        } catch(e: Exception){
            onLoadedListener.onFailed(e)
        }

        onLoadedListener.onLoaded(movies)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadMovies(request: String) : List<Movie>{
        val response = sendRequest(request)
        val moviesDTO: MoviesDTO = Gson().fromJson(response, MoviesDTO::class.java)

        val movies = arrayListOf<Movie>()
        for (movieDTO in moviesDTO.results){
            movies.add(movieDTO.toMovie())
        }
        return movies
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun sendRequest(request: String): String {
        try {
            val uri = URL(request)
            lateinit var urlConnection: HttpsURLConnection

            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.readTimeout = 10000
                val bufferedReader =
                    BufferedReader(InputStreamReader(urlConnection.inputStream))
                return getLines(bufferedReader)
            } catch (e: Exception) {
                Log.e("", "Fail connection", e)
                e.printStackTrace()
                throw e
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
            throw e
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun sendRequestOld(request: String, listener: OnResponseListener) {
        try {
            val uri =
                URL(request)
            val handler = Handler(Looper.getMainLooper())
            Thread {
                lateinit var urlConnection: HttpsURLConnection
                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection.requestMethod = "GET"
                    urlConnection.readTimeout = 10000
                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val response = getLines(bufferedReader)
                    handler.post { listener.onLoaded(response) }
                } catch (e: Exception) {
                    Log.e("", "Fail connection", e)
                    e.printStackTrace()
                    listener.onFailed(e)
                } finally {
                    urlConnection.disconnect()
                }
            }.start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
            listener.onFailed(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }



    interface OnResponseListener {
        fun onLoaded(response: String)
        fun onFailed(throwable: Throwable)
    }


}

//fun testGetMovies(): List<MoviesGroup> {
//    return listOf(
//        Movie(550, "Movie1"),
//        Movie(5687, "Movie2"),
//        Movie(520, "Movie3"),
//        Movie(487, "Movie4"),
//        Movie(550, "Movie5"),
//        Movie(5687, "Movie6"),
//        Movie(520, "Movie7"),
//        Movie(427, "Movie8"),
//        Movie(552, "Movie9"),
//        Movie(5687, "Movie10"),
//        Movie(521, "Movie11"),
//        Movie(487, "Movie12"),
//    )
//}