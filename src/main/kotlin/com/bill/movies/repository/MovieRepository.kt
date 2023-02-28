package com.bill.movies.repository

import com.bill.movies.model.Movie
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface MovieRepository : JpaRepository<Movie, Long> {
    fun findByTitleAndReleaseDate(title: String, releaseDate: LocalDate): Movie?
}