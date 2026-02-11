package org.musiccollection.service

import org.musiccollection.domain.ArtistResponse
import org.musiccollection.util.BadRequestException
import org.musiccollection.mapper.toDomain
import org.musiccollection.repository.ArtistRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.resteasy.reactive.multipart.FileUpload
import java.io.InputStream
import java.time.Instant

@ApplicationScoped
class ArtistImageService(
    private val s3Service: S3Service,
    private val userService: UserService,
    private val artistRepository: ArtistRepository,
    @param:ConfigProperty(name = "bucket.artist") private val bucket: String
) {

    @Transactional
    fun uploadImage(id: Long, image: FileUpload?): ArtistResponse {
        // check if the artist and the image are existing
        val artistSaved = artistRepository.findOrThrow(id, userService.userId)
        if (image == null || image.uploadedFile() == null) throw BadRequestException()

        // 1. if there is already an image saved, delete it first
        artistSaved.filename?.let { s3Service.deleteFile(bucket, it) }

        // 2. upload the new image and get the filename
        val filename = s3Service.uploadFile(bucket, image.uploadedFile(), image.fileName())

        // 3. update the artist and return it
        return artistSaved
            .also {
                it.filename = filename
                it.updatedAt = Instant.now()
            }.toDomain()
    }

    fun downloadImage(id: Long): Pair<InputStream, String> {
        // check if the artist and image are existing
        val filename = artistRepository.findOrThrow(id, userService.userId).filename ?: throw BadRequestException()

        // download the file and return it with the filename
        return s3Service.downloadFile(bucket, filename) to filename
    }

    @Transactional
    fun deleteImage(id: Long) {
        // check if the artist and image are existing
        val artistSaved = artistRepository.findOrThrow(id, userService.userId)
        val filename = artistSaved.filename ?: throw BadRequestException()

        // delete the file
        s3Service.deleteFile(bucket, filename)

        // update the artist
        artistSaved.also {
            it.filename = null
            it.updatedAt = Instant.now()
        }
    }
}
