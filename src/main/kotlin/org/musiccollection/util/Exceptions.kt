package org.musiccollection.util

import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import org.musiccollection.domain.Error

class BadRequestException : WebApplicationException(Response
    .status(Response.Status.BAD_REQUEST)
    .entity(Error(Response.Status.BAD_REQUEST.statusCode, "Invalid input"))
    .build()
)
