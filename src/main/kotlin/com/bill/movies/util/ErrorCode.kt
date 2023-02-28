package com.bill.movies.util

import org.springframework.http.HttpStatus

enum class ErrorCode(private val errorCode: String, private val message: String, private val useArgs: Boolean, private val httpStatus: HttpStatus) {
  MISSING_TITLE("MOV001", "Title is required, it cannot be empty", false, HttpStatus.BAD_REQUEST),
  MISSING_STAR("MOV002", "At least one star is required", false, HttpStatus.BAD_REQUEST),
  MISSING_RELEASE_DATE("MOV003", "Release date is required", false, HttpStatus.BAD_REQUEST),
  DUPLICATE_MOVIE("MOV004", "Movie with title:'%s' and release_date:'%s' already exists", true, HttpStatus.CONFLICT),
  MOVIE_NOT_FOUND("MOV005", "Movie with id %d not found", true, HttpStatus.NOT_FOUND),
  DUPLICATE_STAR("MOV006", "Star list contains duplicate stars", false, HttpStatus.BAD_REQUEST),
  UNKNOWN_ERROR("MOV007", "An unknown error occurred", false, HttpStatus.INTERNAL_SERVER_ERROR);

  fun getMessage(vararg args: Any): String {
    return if (useArgs) {
      message.format(*args)
    } else {
      message
    }
  }

  fun getHttpStatus(): HttpStatus {
    return httpStatus
  }

  fun getErrorCode(): String {
    return errorCode
  }
}