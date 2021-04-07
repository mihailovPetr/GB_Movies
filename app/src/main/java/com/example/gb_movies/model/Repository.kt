package com.example.gb_movies.model

interface Repository {
    fun getMovieGroupsFromServer(requests: List<String>, onLoadedListener: OnLoadedListener)
    fun getMovieGroupsFromLocalStorage(): List<List<Movie>>

    interface OnLoadedListener {
        fun onLoaded(movies: List<List<Movie>>)
        fun onFailed(throwable: Throwable)
    }
}
