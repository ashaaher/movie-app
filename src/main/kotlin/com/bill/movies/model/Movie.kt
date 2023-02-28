package com.bill.movies.model

import com.bill.movies.dto.MovieDto
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.persistence.*
@Entity
@Table(name = "movie")
data class Movie(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var title: String,

    @Column(name = "release_date", nullable = false)
    var releaseDate: LocalDate,

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "movie_star",
        joinColumns = [JoinColumn(name = "movie_id")],
        inverseJoinColumns = [JoinColumn(name = "star_id")]
    )
    var stars: MutableSet<Star> = mutableSetOf()
) {
    constructor() : this(0,"",LocalDate.now(), mutableSetOf())

    fun toMovieDto(): MovieDto {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return MovieDto(id, title, releaseDate.format(formatter), stars.map { it.name })
    }
}