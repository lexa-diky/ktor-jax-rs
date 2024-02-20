package io.github.lexadiky.kjrs.demo

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.path
import io.ktor.server.routing.routing
import org.slf4j.event.Level

fun main() {
    embeddedServer(Netty) {
        install(ContentNegotiation) {
            json()
        }
        install(CallLogging) {
            level = Level.DEBUG
        }

        routing {
            EchoControllerJaxRsGenerated(EchoController())
                .bind(this)
        }
    }.start(wait = true)
}

