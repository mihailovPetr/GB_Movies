package com.example.gb_movies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gb_movies.BuildConfig
import com.example.gb_movies.model.Movie
import com.example.gb_movies.model.MovieDTO
import com.example.gb_movies.model.MoviesDTO
import com.example.gb_movies.model.MoviesGroup
import com.example.gb_movies.repository.MainRepository
import com.example.gb_movies.repository.MainRepositoryImpl
import com.example.gb_movies.repository.RemoteDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class MainViewModel(
    val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: MainRepository = MainRepositoryImpl(RemoteDataSource())
) : ViewModel() {

    fun getMovieGroupsFromLocalSource() = getDataFromRemoteSource()

    fun getMovieGroupsFromRemoteSource() = getDataFromRemoteSource()

    val movieGroups = getMoviesGroups()

    private fun getDataFromRemoteSource(){
        liveData.value = AppState.Loading

        val pathes = ArrayList<String>()
        
        for (moviesGroup in movieGroups){

        }


        repository.getMovies("path", callback)
    }

    private val callback = object : Callback<MoviesDTO> {
        override fun onResponse(call: Call<MoviesDTO>, response: Response<MoviesDTO>) {
            val serverResponse = response.body()
            liveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<MoviesDTO>, t: Throwable) {
            liveData.postValue(AppState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }

        private fun checkResponse(serverResponse: MoviesDTO): AppState {
            val movies = serverResponse.results
            return if (movies.isNullOrEmpty()) {
                AppState.Error(Throwable(CORRUPTED_DATA))
            } else {
                AppState.Success(movies)
            }
        }
    }

    private fun getDataFromRemoteSource1() {
        liveData.value = AppState.Loading

        Thread {
            val moviesGroups = getMoviesGroups()
            val requestList: ArrayList<String> = arrayListOf()
            for (movieGroup in moviesGroups) {
                requestList.add(movieGroup.request)
            }
            repository.getMovies(
                requestList,
                object : MainRepository.OnLoadedListener {

                    override fun onLoaded(movies: List<List<Movie>>) {
                        for (i in 0 until moviesGroups.size) {
                            moviesGroups[i].movies = movies[i]
                        }
                        liveData.postValue(AppState.Success(moviesGroups))
                    }

                    override fun onFailed(throwable: Throwable) {
                        liveData.postValue(AppState.Error(throwable))
                    }

                })


        }.start()
    }

    private fun getMoviesGroups(): List<MoviesGroup> {
        return listOf(
            MoviesGroup(
                "В кинотеартрах",
                "На этой недели",
                "movie/now_playing?language=ru-RU&region=RU&sort_by=popularity.desc"
            ),
            MoviesGroup(
                "Скоро в кино",
                "Самые ожидаемые фильмы",
                "movie/upcoming?language=ru-RU&region=RU&sort_by=popularity.desc"
            ),
            MoviesGroup(
                "Популярное",
                "Топ этой недели",
                "movie/popular?language=ru-RU&region=RU&sort_by=popularity.desc"
            )
        )
    }

//    private fun getMoviesGroupsOld(): List<MoviesGroup> {
//        return listOf(
//            MoviesGroup(
//                "В кинотеартрах",
//                "На этой недели",
//                "https://api.themoviedb.org/3/discover/movie?primary_release_date.gte=2021-03-06&primary_release_date.lte=2021-04-01&api_key=${BuildConfig.TMDB_API_KEY}&language=ru-RU&region=RU&sort_by=popularity.desc"
//            ),
//            MoviesGroup(
//                "Скоро в кино",
//                "Самые ожидаемые фильмы",
//                "https://api.themoviedb.org/3/discover/movie?api_key=${BuildConfig.TMDB_API_KEY}&language=ru-RU&region=RU&release_date.gte=2021-04-03&release_date.lte=2021-04-28&with_release_type=2|3"
//            ),
//            MoviesGroup(
//                "Популярное",
//                "Топ этой неделе",
//                "https://api.themoviedb.org/3/discover/movie?api_key=${BuildConfig.TMDB_API_KEY}&language=ru-RU&region=RU&sort_by=popularity.desc"
//            )
//        )
//    }
}
