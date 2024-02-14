package io.github.lexadiky.kjrs.codegen

import com.squareup.kotlinpoet.ClassName

object ClassQualifierLibrary {
    val ktorRouting = ClassName.bestGuess("io.ktor.server.routing.Routing")
}