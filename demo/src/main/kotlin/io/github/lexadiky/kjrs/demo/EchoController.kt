package io.github.lexadiky.kjrs.demo

import jakarta.ws.rs.GET
import jakarta.ws.rs.HeaderParam
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Response
import java.util.UUID

/**
 * My very good documentation
 */
@Path("/echo")
class EchoController {

    @GET
    @POST
    suspend fun echo(
        body: UUID,
        @HeaderParam("X-MY-CUSTOM-HEADER") customHeader: String
    ): Response {
        return Response.ok()
            .build()
    }
}