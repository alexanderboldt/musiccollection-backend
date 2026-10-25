package org.musiccollection.resource

import org.musiccollection.domain.ArtistRequest
import org.musiccollection.service.ArtistService
import org.musiccollection.util.convertToSort
import io.smallrye.faulttolerance.api.RateLimit
import jakarta.annotation.security.RolesAllowed
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.NotBlank
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
import org.musiccollection.domain.ArtistResponse

@Suppress("unused")
@Path(Resource.Path.ARTIST)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(Role.USER)
@RateLimit
class ArtistResource(private val artistService: ArtistService) {

    // create

    @POST
    @ResponseStatus(RestResponse.StatusCode.CREATED)
    fun create(@Valid artist: ArtistRequest): ArtistResponse = artistService
        .create(artist)
        .toDomain()

    // read

    @GET
    fun readAll(@QueryParam(Resource.Param.SORT) sort: String?): List<ArtistResponse> = artistService
        .readAll(sort.convertToSort())
        .map { it.toDomain() }

    @GET
    @Path(Resource.Path.ID)
    fun read(@Positive @PathParam(Resource.Param.ID) id: Long): ArtistResponse = artistService
        .read(id)
        .toDomain()

    // update

    @PUT
    @Path(Resource.Path.ID)
    fun update(
        @Positive @PathParam(Resource.Param.ID) id: Long,
        @Valid artist: ArtistRequest
    ): ArtistResponse = artistService
        .update(id, artist)
        .toDomain()

    // delete

    @DELETE
    @Path(Resource.Path.ID)
    fun delete(@Positive @PathParam(Resource.Param.ID) id: Long) = artistService.delete(id)
}
