package org.musiccollection.resource

import org.musiccollection.resource.domain.AlbumRequest
import org.musiccollection.service.AlbumService
import org.musiccollection.util.convertToSort
import io.smallrye.faulttolerance.api.RateLimit
import jakarta.annotation.security.RolesAllowed
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.ResponseStatus
import org.jboss.resteasy.reactive.RestResponse
import org.musiccollection.resource.domain.AlbumResponse

@Suppress("unused")
@Path(Resource.Path.ALBUM)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(Role.USER)
@RateLimit
class AlbumResource(private val albumService: AlbumService) {

    // create

    @POST
    @ResponseStatus(RestResponse.StatusCode.CREATED)
    fun create(@Valid album: AlbumRequest): AlbumResponse = albumService
        .create(album)
        .toDomain()

    // read

    @GET
    fun readAll(@QueryParam(Resource.Param.SORT) sort: String?): List<AlbumResponse> = albumService.
    readAll(sort.convertToSort())
        .map { it.toDomain() }

    @GET
    @Path(Resource.Path.ID)
    fun read(@Positive @PathParam(Resource.Param.ID) id: Long): AlbumResponse = albumService
        .read(id)
        .toDomain()

    // update

    @PUT
    @Path(Resource.Path.ID)
    fun update(
        @Positive @PathParam(Resource.Param.ID) id: Long,
        @Valid album: AlbumRequest
    ): AlbumResponse = albumService
        .update(id, album)
        .toDomain()

    // delete

    @DELETE
    @RolesAllowed(Role.ADMIN)
    fun deleteAll() = albumService.deleteAll()

    @DELETE
    @Path(Resource.Path.ID)
    fun delete(@Positive @PathParam(Resource.Param.ID) id: Long) = albumService.delete(id)
}
