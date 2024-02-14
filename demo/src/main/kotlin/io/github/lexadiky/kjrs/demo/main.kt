package io.github.lexadiky.kjrs.demo

import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun main() {
    embeddedServer(Netty) {
        routing {
            val a: Routing = this
            this.route("/") {
            }
        }
    }
}

