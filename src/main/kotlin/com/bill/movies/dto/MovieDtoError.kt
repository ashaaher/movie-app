package com.bill.movies.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "DTO for a movieError")
data class MovieDtoError(
    @field:Schema
    val errorCode: String,

    @field:Schema
    val errorMessage: String
)
