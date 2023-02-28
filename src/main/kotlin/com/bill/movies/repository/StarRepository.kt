package com.bill.movies.repository

import com.bill.movies.model.Star
import org.springframework.data.jpa.repository.JpaRepository

interface StarRepository : JpaRepository<Star, Long> {
    fun findByName(name: String): Star?
}