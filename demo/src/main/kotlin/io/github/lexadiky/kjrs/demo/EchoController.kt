package io.github.lexadiky.kjrs.demo

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.HeaderParam
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import java.util.UUID

@Path("/echo")
class EchoController {

    @GET
    @POST
    suspend fun echo(
        body: UUID,
        @HeaderParam("X-MY-CUSTOM-HEADER") customHeader: String
    ) {

    }
}