package io.github.lexadiky.kjrs.demo

import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
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
    suspend fun echo(): Response {
        val response = Response.ok(mapOf("a" to "b"))
            .build()

        return response
    }


}