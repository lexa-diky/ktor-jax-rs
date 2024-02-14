@file:OptIn(KspExperimental::class)

package io.github.lexadiky.kjrs.descriptor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.HEAD
import jakarta.ws.rs.HttpMethod
import jakarta.ws.rs.OPTIONS
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Produces

class HandlerDescriptorFactory {

    fun create(function: KSFunctionDeclaration): List<HandlerDescriptor> {
        val httpMethods = resolveHttpMethods(function)
        val consumesContentTypes = resolveConsumesContentType(function)
        val producesContentTypes = resolveProducesContentType(function)

        return producesContentTypes.flatMap { producesContentType ->
            consumesContentTypes.flatMap { acceptsContentType ->
                httpMethods.map { httpMethod ->
                    HandlerDescriptor(
                        handlerMethod = function.simpleName.asString(),
                        httpMethod = httpMethod,
                        acceptsContentType = acceptsContentType,
                        producesContentType = producesContentType,
                        parameters = function.parameters.map(::createParameter)
                    )
                }
            }
        }
    }

    private fun resolveConsumesContentType(function: KSFunctionDeclaration): List<String> {
        val consumesAnnotations = function.getAnnotationsByType(Consumes::class).toList()
        return if (consumesAnnotations.isEmpty()) {
            listOf(DEFAULT_CONTENT_TYPE)
        } else {
            consumesAnnotations.flatMap { it.value.toList() }
        }
    }

    private fun resolveProducesContentType(function: KSFunctionDeclaration): List<String> {
        val consumesAnnotations = function.getAnnotationsByType(Produces::class).toList()
        return if (consumesAnnotations.isEmpty()) {
            listOf(DEFAULT_CONTENT_TYPE)
        } else {
            consumesAnnotations.flatMap { it.value.toList() }
        }
    }

    private fun createParameter(parameter: KSValueParameter): HandlerParameterDescriptor {
        return HandlerParameterDescriptor(
            alias = parameter.name?.asString() ?: error("only named parameters are supported")
        )
    }

    private fun resolveHttpMethods(function: KSFunctionDeclaration): List<String> {
        val buffer = ArrayList<String>()
        when {
            function.isAnnotationPresent(GET::class) -> buffer += HttpMethod.GET
            function.isAnnotationPresent(POST::class) -> buffer += HttpMethod.POST
            function.isAnnotationPresent(PUT::class) -> buffer += HttpMethod.PUT
            function.isAnnotationPresent(DELETE::class) -> buffer += HttpMethod.DELETE
            function.isAnnotationPresent(HEAD::class) -> buffer += HttpMethod.HEAD
            function.isAnnotationPresent(OPTIONS::class) -> buffer += HttpMethod.OPTIONS
            function.isAnnotationPresent(HttpMethod::class) ->
                buffer += function.getAnnotationsByType(HttpMethod::class).map { it.value }
            else -> buffer += HttpMethod.GET
        }
        return buffer
    }

    companion object {

        private const val DEFAULT_CONTENT_TYPE = "*/*"
    }
}