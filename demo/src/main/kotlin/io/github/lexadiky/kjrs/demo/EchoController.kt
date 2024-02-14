package io.github.lexadiky.kjrs.demo

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path

@Path("/echo")
class EchoController {

    @GET
    fun echo() {

    }
}