package com.bill.movies.dto

import com.bill.movies.model.Movie
import com.bill.movies.model.Star
import com.bill.movies.repository.StarRepository
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Schema(description = "DTO for a movie")
data class MovieDto(

    @field:Schema
    val id: Long? = null,

    @field:Schema
    val title: String,

    @field:Schema
    val releaseDate: String,

    @field:Schema
    val stars: List<String>
) : Serializable

fun MovieDto.toMovie(starRepository: StarRepository): Movie {
    val stars = this.stars.map { starName ->
        starRepository.findByName(starName) ?: starRepository.save(Star(name = starName))
    }.toMutableSet()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return Movie(
        title = this.title,
        releaseDate = LocalDate.parse(this.releaseDate, formatter) ?: throw IllegalArgumentException("Release date must not be null"),
        stars = stars
    )
}




