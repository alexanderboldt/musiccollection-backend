package org.musiccollection.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import java.io.InputStream
import java.nio.file.Path
import java.util.UUID

/**
 * Manages the connection to the S3-storage.
 */
@ApplicationScoped
class S3Service {

    @Inject
    private lateinit var s3Client: S3Client

    /**
     * Creates the assigned bucket, if it doesn't exist yet.
     * This function should be used in development- or test-mode.
     * In production, the buckets should already exist.
     *
     * @param bucket the bucket to create as a [String].
     */
    fun createBucket(bucket: String) {
        val bucketExists = s3Client
            .listBuckets { it.build() }
            .buckets()
            .any { it.name() == bucket }

        if (!bucketExists) {
            s3Client.createBucket { it.bucket(bucket).build() }
        }
    }

    /**
     * Uploads a file. The filename will be created with a random UUID and the extension from the assigned filename.
     * The final filename will be returned and should be used to associate the file.
     *
     * @param bucket the bucket where to store as a [String].
     * @param path the path of the file as a [Path].
     * @param filename the filename as a [String].
     * @return returns the created filename as a [String].
     */
    fun uploadFile(bucket: String, path: Path, filename: String): String {
        val extension = filename
            .substringAfterLast(".", "")
            .let { if(it.isNotBlank()) ".$it" else "" }

        val filename = "${UUID.randomUUID()}$extension"

        s3Client.putObject(
            { it.bucket(bucket).key(filename).build() },
            RequestBody.fromFile(path)
        )

        return filename
    }

    /**
     * Downloads a file.
     *
     * @param bucket the bucket to download from as a [String].
     * @param filename the filename of the file as a [String].
     * @return the result as a [InputStream].
     */
    fun downloadFile(bucket: String, filename: String): InputStream {
        return s3Client.getObject {
            it.bucket(bucket).key(filename).build()
        }
    }

    /**
     * Deletes a file.
     *
     * @param bucket the bucket to delete from as a [String].
     * @param filename the filename of the file as a [String].
     */
    fun deleteFile(bucket: String, filename: String) {
        s3Client.deleteObject {
            it.bucket(bucket).key(filename).build()
        }
    }
}
