package io.github.lexadiky.kjrs.codegen

import com.squareup.kotlinpoet.ClassName

object ClassQualifierLibrary {
    val ktorRouting = ClassName.bestGuess("io.ktor.server.routing.Routing")
    val ktorRoute = ClassName.bestGuess("io.ktor.server.routing.Route")
    val ktorRouteFn = ClassName("io.ktor.server.routing", "route")
    val ktorCallFn = ClassName("io.ktor.server.application", "call")
    val ktorGetOrFailFn = ClassName("io.ktor.server.util", "getOrFail")
    val ktorMethodFn = ClassName("io.ktor.server.routing", "method")
    val ktorBodyReceiveFn = ClassName("io.ktor.server.request", "receive")
    val ktorAcceptFn = ClassName("io.ktor.server.routing", "accept")
    val ktorHttpMethod = ClassName.bestGuess("io.ktor.http.HttpMethod")
    val ktorContentType = ClassName.bestGuess("io.ktor.http.ContentType")
}