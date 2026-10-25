package org.musiccollection.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.musiccollection.domain.ArtistResponse
import java.time.Instant

@Entity
data class ArtistEntity(
    @Id
    @GeneratedValue
    val id: Long,
    val userId: String,
    var name: String,
    var filename: String?,
    val createdAt: Instant,
    var updatedAt: Instant
) {
    fun toDomain() = ArtistResponse(
        id,
        userId,
        name,
        filename,
        createdAt,
        updatedAt
    )
}
