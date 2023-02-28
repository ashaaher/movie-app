package com.bill.movies.controller

import com.bill.movies.dto.MovieDto
import com.bill.movies.exception.MovieServiceException
import com.bill.movies.service.MovieService
import com.bill.movies.dto.MovieDtoError
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/movies")
@OpenAPIDefinition(
    info = Info(
        title = "Movies API",
        version = "1.0",
        description = "API for managing movies"
    )
)
class MovieController(private val movieService: MovieService) {

    @CreateMovieOperation
    @PostMapping
    fun createMovie(@RequestBody movieDto: MovieDto): ResponseEntity<Any> {
        return try {
            val createdMovie = movieService.createMovie(movieDto)
            ResponseEntity(createdMovie, HttpStatus.CREATED)
        } catch (e: MovieServiceException) {
            val errorCode = e.errorCode
            val errorMessage = e.message ?: "Unknown error"
            val error = MovieDtoError(errorCode.getErrorCode(), errorMessage)
            ResponseEntity(error, errorCode.getHttpStatus())
        }
    }

    @GetMovieOperation
    @GetMapping("/{id}")
    fun getMovie(
        @PathVariable("id")
        @Parameter(
            description = "ID of the movie to get",
            required = true
        ) id: Long
    ): ResponseEntity<Any> {
        return try {
            val movie = movieService.getMovie(id)
            ResponseEntity(movie, HttpStatus.OK)
        } catch (e: MovieServiceException) {
            val errorCode = e.errorCode
            val errorMessage = e.message ?: "Unknown error"
            val error = MovieDtoError(errorCode.getErrorCode(), errorMessage)
            ResponseEntity(error, errorCode.getHttpStatus())
        }
    }

    @UpdateMovieOperation
    @PutMapping("/{id}")
    fun updateMovie(
        @PathVariable("id")
        @Parameter(
            description = "ID of the movie to update",
            required = true
        ) id: Long,
        @RequestBody movieDto: MovieDto
    ): ResponseEntity<Any> {
        return try {
            val updatedMovie = movieService.updateMovie(id, movieDto)
            ResponseEntity(updatedMovie, HttpStatus.OK)
        } catch (e: MovieServiceException) {
            val errorCode = e.errorCode
            val errorMessage = e.message ?: "Unknown error"
            val error = MovieDtoError(errorCode.getErrorCode(), errorMessage)
            ResponseEntity(error, errorCode.getHttpStatus())
        }
    }

    @DeleteMovieOperation
    @DeleteMapping("/{id}")
    fun deleteMovie(
        @PathVariable("id")
        @Parameter(
            description = "ID of the movie to delete",
            required = true
        ) id: Long
    ): ResponseEntity<Any> {
        try {
            movieService.deleteMovie(id)
        } catch (e: MovieServiceException) {
            val errorCode = e.errorCode
            val errorMessage = e.message ?: "Unknown error"
            val error = MovieDtoError(errorCode.getErrorCode(), errorMessage)
            return ResponseEntity(error, errorCode.getHttpStatus())
        }
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}