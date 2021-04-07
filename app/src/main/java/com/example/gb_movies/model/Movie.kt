package com.example.gb_movies.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val id: Int = 0,
    val title: String = "name",
    val director: String = "director",
    val overview: String = "description",
    val runTime: Int = 0,
    val actors: List<String> = listOf(),
    val genres: List<String> = listOf(), //TODO:
    val vote: Float = 0f,
    val voteCount: Int = 0,
    val budget: Int = 0,
    val revenue: Int = 0,
    val releaseDate: String = "",


    ) : Parcelable {

//    enum class Genre { TODO:
//        ACTION,
//        ADVENTURE,
//        ANIMATION,
//        BIOGRAPHY,
//        COMEDY,
//        CRIME,
//        DOCUMENTARY
//    }

    fun genresToString(): String {
        val sb = StringBuilder()
        for (genre in genres) {
            sb.append(genre)
            sb.append(", ")
        }
        return sb.substring(0, sb.length - 2)
    }
}

data class GenreDTO(val id: Int, val name: String)
data class ProductionCountrieDTO(val name: String)

data class MovieDTO(
    val id: Int?,
    val title: String? = null,
    val overview: String? = null,
    val genres: List<GenreDTO>? = null,
    val popularity: Float? = null,
    val runtime: Int? = null,
    val vote_average: Float? = null,
    val vote_count: Int? = null,
    val release_date: String? = null,
    val budget: Int? = null,
    val revenue: Int? = null,
    val adult: Boolean? = null,
    val production_countries: List<ProductionCountrieDTO>? = null
) {
    fun getGenres(): String {
        val sb = StringBuilder()
        if (genres != null) {
            for (genre in genres) {
                sb.append(genre.name)
                sb.append(", ")
            }
        }
        return sb.substring(0, sb.length - 2)
    }

    fun getListOfGenres(): List<String> {
        val result = ArrayList<String>()
        if (genres != null) {
            for (genre in genres) {
                result.add(genre.name)
            }
        }
        return result
    }

    fun toMovie(): Movie {
        return Movie(
            id ?: 0,
            title ?: "",
            "",
            overview ?: "",
            runtime ?: 0,
            listOf(),
            getListOfGenres(),
            vote_average ?: 0f,
            vote_count ?: 0,
            budget ?: 0,
            revenue ?: 0,
            release_date ?: ""
        )
    }
}

data class MoviesDTO(
    val results: List<MovieDTO>
)
