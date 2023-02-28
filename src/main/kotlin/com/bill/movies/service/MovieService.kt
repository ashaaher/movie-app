package com.bill.movies.service

import com.bill.movies.dto.MovieDto
import com.bill.movies.model.Movie

interface MovieService {
    fun getMovie(id: Long): MovieDto
    fun createMovie(movieDto: MovieDto): MovieDto
    fun updateMovie(id: Long, movieDto: MovieDto): MovieDto
    fun deleteMovie(id: Long)
}