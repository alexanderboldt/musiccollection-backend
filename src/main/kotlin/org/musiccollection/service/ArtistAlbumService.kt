package org.musiccollection.service

import org.musiccollection.repository.AlbumRepository
import org.musiccollection.repository.ArtistRepository
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.musiccollection.repository.entity.AlbumEntity

@ApplicationScoped
class ArtistAlbumService(
    private val userService: UserService,
    private val albumRepository: AlbumRepository,
    private val artistRepository: ArtistRepository
) {

    @Transactional
    fun readAll(artistId: Long, sort: Sort): List<AlbumEntity> {
        artistRepository.existsOrThrow(artistId, userService.userId)

        return albumRepository.findByArtistId(userService.userId, artistId, sort)
    }
}
