package com.bill.movie.controller

import com.bill.movies.controller.MovieController
import com.bill.movies.dto.MovieDto
import com.bill.movies.service.MovieService
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.http.HttpStatus

class MovieControllerTest {

    @Mock
    lateinit var movieService: MovieService

    @InjectMocks
    lateinit var movieController: MovieController

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `createMovie should return CREATED response with created movie`() {
        val movieDto = MovieDto(
            title = "The Godfather",
            releaseDate = "1972-03-24",
            stars = listOf("Marlon Brando", "Al Pacino")
        )
        whenever(movieService.createMovie(movieDto)).thenReturn(movieDto)
        val response = movieController.createMovie(movieDto)
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(movieDto, response.body)
    }

    @Test
    fun `getMovie should return OK response with movie`() {
        val movieDto = MovieDto(
            title = "The Godfather",
            releaseDate = "1972-03-24",
            stars = listOf("Marlon Brando", "Al Pacino")
        )
        whenever(movieService.getMovie(1L)).thenReturn(movieDto)
        val response = movieController.getMovie(1L)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(movieDto, response.body)
    }

    @Test
    fun `updateMovie should return OK response with updated movie`() {
        val movieDto = MovieDto(
            title = "The Godfather",
            releaseDate = "1972-03-24",
            stars = listOf("Marlon Brando", "Al Pacino")
        )

        whenever(movieService.updateMovie(1L, movieDto)).thenReturn(movieDto)
        val response = movieController.updateMovie(1L, movieDto)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(movieDto, response.body)
    }

    @Test
    fun `deleteMovie should return NO_CONTENT response`() {
        val response = movieController.deleteMovie(1L)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }
}