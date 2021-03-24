package com.example.gb_movies.model

class RepositoryImpl : Repository {

    override fun getMovieFromServer(): Movie {
        return Movie()
    }

    override fun getMovieFromLocalStorage(): Movie {
        return Movie()
    }
}
