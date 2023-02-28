package com.bill.movies.dto

import com.bill.movies.model.Star
import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

@Schema(description = "DTO for a star")
data class StarDto(val id: Int = 0, val name: String? = null) : Serializable
fun StarDto.toStar(): Star {
    return Star(
        id = this.id,
        name = requireNotNull(this.name) { "Name must not be null" }
    )
}