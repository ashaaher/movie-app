package com.bill.movies.exception

import com.bill.movies.util.ErrorCode
import org.springframework.http.HttpStatus

class MovieServiceException(
    val errorCode: ErrorCode,
    private val errorMessage: String
) : RuntimeException(errorMessage) {

    fun getHttpStatus(): HttpStatus {
        return errorCode.getHttpStatus()
    }

    fun getErrorCode(): String {
        return errorCode.getErrorCode()
    }

}