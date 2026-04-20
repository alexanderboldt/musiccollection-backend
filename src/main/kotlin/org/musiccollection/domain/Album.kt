package org.musiccollection.domain

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.time.Instant

data class AlbumRequest(
    @field:Positive val artistId: Long,
    @field:NotBlank val name: String,
    @field:Positive val year: Int,
    @field:Positive val tracks: Int
)

data class AlbumResponse(
    val id: Long,
    val userId: String,
    val artistId: Long,
    val name: String,
    val year: Int,
    val tracks: Int,
    val filename: String?,
    val createdAt: Instant,
    val updatedAt: Instant
)
