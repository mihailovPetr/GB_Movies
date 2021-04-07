package com.example.gb_movies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gb_movies.BuildConfig
import com.example.gb_movies.model.Movie
import com.example.gb_movies.model.MoviesGroup
import com.example.gb_movies.model.Repository
import com.example.gb_movies.model.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()
) :
    ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getMovieGroupsFromLocalSource() = getDataFromRemoteSource()

    fun getMovieGroupsFromRemoteSource() = getDataFromRemoteSource()

    private fun getDataFromRemoteSource() {
        liveDataToObserve.value = AppState.Loading

        Thread {
            val moviesGroups = getMoviesGroups()
            val requestList: ArrayList<String> = arrayListOf()
            for (movieGroup in moviesGroups) {
                requestList.add(movieGroup.request)
            }
            repositoryImpl.getMovieGroupsFromServer(requestList, object: Repository.OnLoadedListener{

                override fun onLoaded(movies: List<List<Movie>>) {
                    for (i in 0 until moviesGroups.size) {
                        moviesGroups[i].movies = movies[i]
                    }
                    liveDataToObserve.postValue(AppState.Success(moviesGroups))
                }

                override fun onFailed(throwable: Throwable) {
                    liveDataToObserve.postValue(AppState.Error(throwable))
                }

            } )


        }.start()
    }

    private fun getMoviesGroups(): List<MoviesGroup> {
        return listOf(
            MoviesGroup(
                "В кинотеартрах",
                "На этой недели",
                "https://api.themoviedb.org/3/discover/movie?primary_release_date.gte=2021-03-06&primary_release_date.lte=2021-04-01&api_key=${BuildConfig.TMDB_API_KEY}&language=ru-RU&region=RU&sort_by=popularity.desc"
            ),
            MoviesGroup(
                "Скоро в кино",
                "Самые ожидаемые фильмы",
                "https://api.themoviedb.org/3/discover/movie?api_key=${BuildConfig.TMDB_API_KEY}&language=ru-RU&region=RU&release_date.gte=2021-04-03&release_date.lte=2021-04-28&with_release_type=2|3"
            ),
            MoviesGroup(
                "Популярное",
                "Топ этой неделе",
                "https://api.themoviedb.org/3/discover/movie?api_key=${BuildConfig.TMDB_API_KEY}&language=ru-RU&region=RU&sort_by=popularity.desc"
            )
        )
    }
}
