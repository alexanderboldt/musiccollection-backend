package org.musiccollection.service

import org.musiccollection.domain.AlbumRequest
import org.musiccollection.entity.AlbumEntity
import org.musiccollection.repository.AlbumRepository
import org.musiccollection.repository.ArtistRepository
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.time.Instant

@ApplicationScoped
class AlbumService(
    private val s3Service: S3Service,
    private val userService: UserService,
    private val albumRepository: AlbumRepository,
    private val artistRepository: ArtistRepository,
    @param:ConfigProperty(name = "bucket.album") private val bucket: String
) {
    // create

    @Transactional
    fun create(album: AlbumRequest): AlbumEntity {
        artistRepository.existsOrThrow(album.artistId, userService.userId)

        val entity = AlbumEntity(
            0,
            userService.userId,
            album.artistId,
            album.name,
            album.year,
            album.tracks,
            null,
            Instant.now(),
            Instant.now()
        )

        return albumRepository.save(entity)
    }

    // read

    @Transactional
    fun readAll(sort: Sort) = albumRepository.findAll(userService.userId, sort)

    @Transactional
    fun read(id: Long) = albumRepository.findOrThrow(id, userService.userId)

    // update

    @Transactional
    fun update(id: Long, album: AlbumRequest): AlbumEntity {
        artistRepository.existsOrThrow(album.artistId, userService.userId)

        return albumRepository
            .findOrThrow(id, userService.userId)
            .apply {
                artistId = album.artistId
                name = album.name
                year = album.year
                tracks = album.tracks
                updatedAt = Instant.now()
            }
    }

    // delete

    @Transactional
    fun deleteAll() {
        albumRepository.deleteAll()
    }

    @Transactional
    fun delete(id: Long) {
        val albumSaved = albumRepository.findOrThrow(id, userService.userId)

        // delete an existing image from the storage and the album
        albumSaved.filename?.also { s3Service.deleteFile(bucket, it) }
        albumRepository.deleteById(id)
    }
}
