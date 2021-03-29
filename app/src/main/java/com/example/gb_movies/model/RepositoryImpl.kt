package com.example.gb_movies.model

class RepositoryImpl : Repository {

    override fun getMoviesFromServer() = testGetMovies()

    override fun getMoviesFromLocalStorage() = testGetMovies()
}

fun testGetMovies(): List<Movie> {
    return listOf(
        Movie("Movie1"),
        Movie("Movie2"),
        Movie("Movie3"),
        Movie("Movie4"),
        Movie("Movie5"),
        Movie("Movie6"),
        Movie("Movie7"),
        Movie("Movie8"),
        Movie("Movie9"),
        Movie("Movie10"),
        Movie("Movie11"),
        Movie("Movie12"),
        Movie("Movie13"),
        Movie("Movie14"),
        Movie("Movie15"),
    )
}