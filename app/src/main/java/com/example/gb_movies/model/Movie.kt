package com.example.gb_movies.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val name: String = "name",
    val director: String = "director",
    val description: String = "description",
    val year: Int = 0,
    val duration: String = "duration",
    val actors: ArrayList<String> = arrayListOf(),
    val genres: ArrayList<Genre> = arrayListOf(),
    val rating: Int = 0
) : Parcelable {

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
