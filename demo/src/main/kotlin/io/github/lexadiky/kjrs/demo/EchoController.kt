package io.github.lexadiky.kjrs.demo

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam

@Path("/echo")
class EchoController {

    @GET
    @POST
    @Path("/{text}")
    @Consumes("application/json")
    fun echo(@PathParam("text") text: String) {

    }
}