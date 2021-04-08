package com.example.gb_movies.utils

import com.example.gb_movies.model.MoviesGroup

fun getMoviesGroups(): List<MoviesGroup> {
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