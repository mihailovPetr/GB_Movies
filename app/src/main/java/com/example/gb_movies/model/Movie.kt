package com.example.gb_movies.model

data class Movie(
    val name: String = "name",
    val director: String = "director",
    val description : String = "description",
    val year: Int = 0,
    val duration: String = "duration",
    val actors: ArrayList<String> = arrayListOf("actors"),
    val genres : ArrayList<Genre> = arrayListOf(Genre.ACTION),
) {


    enum class Genre {
        ACTION,
        ADVENTURE,
        ANIMATION,
        BIOGRAPHY,
        COMEDY,
        CRIME,
        DOCUMENTARY
    }
}
