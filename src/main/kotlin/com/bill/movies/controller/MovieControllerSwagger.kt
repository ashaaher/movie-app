package com.bill.movies.controller

import com.bill.movies.dto.MovieDto
import com.bill.movies.dto.MovieDtoError
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "Create a new movie", responses = [
        ApiResponse(
            responseCode = "201", description = "Movie created", content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(oneOf = [MovieDto::class, MovieDtoError::class])
                )
            ]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid movie data, missing title, release date or Stars",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(oneOf = [MovieDto::class, MovieDtoError::class])
                )
            ]
        ),
        ApiResponse(
            responseCode = "409", description = "Movie Already Exists", content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(oneOf = [MovieDto::class, MovieDtoError::class])
                )
            ]
        )
    ]
)
annotation class CreateMovieOperation

@Operation(
    summary = "Get a movie by ID", responses = [
        ApiResponse(
            responseCode = "200", description = "Found the movie", content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(oneOf = [MovieDto::class, MovieDtoError::class])
                )
            ]
        ),
        ApiResponse(
            responseCode = "404", description = "Movie not found", content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(oneOf = [MovieDto::class, MovieDtoError::class])
                )
            ]
        )
    ]
)
annotation class GetMovieOperation

@Operation(
    summary = "Update a movie by ID", responses = [
        ApiResponse(
            responseCode = "200", description = "Movie updated", content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(oneOf = [MovieDto::class, MovieDtoError::class])
                )
            ]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid movie data: missing title, release date or Stars",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(oneOf = [MovieDto::class, MovieDtoError::class])
                )
            ]
        ),
        ApiResponse(
            responseCode = "404", description = "Movie not found", content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(oneOf = [MovieDto::class, MovieDtoError::class])
                )
            ]
        ),
        ApiResponse(
            responseCode = "409", description = "Movie Already Exists", content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(oneOf = [MovieDto::class, MovieDtoError::class])
                )
            ]
        )
    ]
)
annotation class UpdateMovieOperation

@Operation(
    summary = "Delete a movie by ID", responses = [
        ApiResponse(responseCode = "204", description = "Movie deleted", content = []),
        ApiResponse(
            responseCode = "404", description = "Movie not found",
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(oneOf = [MovieDto::class, MovieDtoError::class])
                )
            ]
        )
    ]
)
annotation class DeleteMovieOperation
