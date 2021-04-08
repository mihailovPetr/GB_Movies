package com.example.gb_movies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gb_movies.model.MoviesDTO
import com.example.gb_movies.model.MoviesGroup
import com.example.gb_movies.repository.MainRepository
import com.example.gb_movies.repository.MainRepositoryImpl
import com.example.gb_movies.repository.RemoteDataSource
import com.example.gb_movies.utils.getMoviesGroups
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val REQUEST_ERROR = "Ошибка запроса на сервер"

class MainViewModel(
    val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: MainRepository = MainRepositoryImpl(RemoteDataSource())
) : ViewModel() {

    fun getMovieGroupsFromLocalSource() = getDataFromRemoteSource()

    fun getMovieGroupsFromRemoteSource() = getDataFromRemoteSource()

    private val movieGroups = getMoviesGroups()

    private fun getDataFromRemoteSource() {
        liveData.value = AppState.Loading

        for (moviesGroup in movieGroups) {
            repository.getMovies(moviesGroup.request, MainCallback(moviesGroup))
        }
    }

    private var counter = movieGroups.size

    private inner class MainCallback(val group: MoviesGroup) : Callback<MoviesDTO> {
        override fun onResponse(call: Call<MoviesDTO>, response: Response<MoviesDTO>) {
            val serverResponse = response.body()
            if (response.isSuccessful && serverResponse != null) {
                group.movies = serverResponse.toMovieList()
            }
            checkResponses()
        }

        override fun onFailure(call: Call<MoviesDTO>, t: Throwable) {
            checkResponses()
        }

        private fun checkResponses() {
            counter--
            if (counter != 0) return
            counter = movieGroups.size

            for (movieGroup in movieGroups) {
                if (movieGroup.movies == null) {
                    liveData.postValue(AppState.Error(Throwable(REQUEST_ERROR)))
                    return
                }
            }
            liveData.postValue(AppState.Success(movieGroups))
        }
    }
}
