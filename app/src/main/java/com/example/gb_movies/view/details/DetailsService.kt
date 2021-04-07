package com.example.gb_movies.view.details

import android.app.IntentService
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.gb_movies.BuildConfig
import com.example.gb_movies.model.MovieDTO
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

const val MOVIE_ID_EXTRA = "MOVIE ID"
private const val REQUEST_GET = "GET"
private const val REQUEST_TIMEOUT = 10000

class DetailsService(name: String = "DetailService") : IntentService(name) {

    private val broadcastIntent = Intent(DETAILS_INTENT_FILTER)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) {
            onEmptyIntent()
        } else {
            val id = intent.getIntExtra(MOVIE_ID_EXTRA, 0)
            if (id == 0) {
                onEmptyData()
            } else {
                loadMovie(id)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadMovie(id: Int) {
        try {
            val uri =
                URL("https://api.themoviedb.org/3/movie/${id}?api_key=${BuildConfig.TMDB_API_KEY}&language=ru-RU")
            Thread {
                lateinit var urlConnection: HttpsURLConnection
                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection.requestMethod = REQUEST_GET
                    urlConnection.readTimeout = REQUEST_TIMEOUT
                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))

                    val movieDTO: MovieDTO =
                        Gson().fromJson(getLines(bufferedReader), MovieDTO::class.java)
                    onSuccessResponse(movieDTO)
                } catch (e: Exception) {
                    onErrorRequest(e.message ?: "Empty error")
                } finally {
                    urlConnection.disconnect()
                }
            }.start()
        } catch (e: MalformedURLException) {
            onMalformedURL()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    private fun onSuccessResponse(movieDTO: MovieDTO) {
        broadcastIntent.putExtra(DETAILS_MOVIE_EXTRA, movieDTO.toMovie())
        sendBroadcast(DETAILS_RESPONSE_SUCCESS_EXTRA)
    }

    private fun onMalformedURL() {
        sendBroadcast(DETAILS_URL_MALFORMED_EXTRA)
    }

    private fun onErrorRequest(error: String) {
        broadcastIntent.putExtra(DETAILS_REQUEST_ERROR_MESSAGE_EXTRA, error)
        sendBroadcast(DETAILS_REQUEST_ERROR_EXTRA)
    }

    private fun onEmptyData() {
        sendBroadcast(DETAILS_DATA_EMPTY_EXTRA)
    }

    private fun onEmptyIntent() {
        sendBroadcast(DETAILS_INTENT_EMPTY_EXTRA)
    }

    private fun sendBroadcast(result: String) {
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, result)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

}