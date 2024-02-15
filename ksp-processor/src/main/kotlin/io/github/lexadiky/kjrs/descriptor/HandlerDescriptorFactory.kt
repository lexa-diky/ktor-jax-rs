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
import jakarta.ws.rs.HeaderParam
import jakarta.ws.rs.HttpMethod
import jakarta.ws.rs.OPTIONS
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam

class HandlerDescriptorFactory {
    private val pathDescriptorFactory = PathDescriptorFactory()

    fun create(function: KSFunctionDeclaration): List<HandlerDescriptor> {
        val httpMethods = resolveHttpMethods(function)
        val consumesContentTypes = resolveConsumesContentType(function)
        val producesContentTypes = resolveProducesContentType(function)

        val parameters = function.parameters.map(::createParameter)
        require(parameters.count { it is HandlerParameterDescriptor.Body } <= 1) {
            "handler can have only single body parameter"
        }

        return producesContentTypes.flatMap { producesContentType ->
            consumesContentTypes.flatMap { acceptsContentType ->
                httpMethods.map { httpMethod ->
                    HandlerDescriptor(
                        handlerMethod = function.simpleName.asString(),
                        httpMethod = httpMethod,
                        acceptsContentType = acceptsContentType,
                        producesContentType = producesContentType,
                        path = resolvePath(function),
                        parameters = parameters
                    )
                }
            }
        }
    }

    private fun resolvePath(function: KSFunctionDeclaration): PathDescriptor {
        val pathAnnotations = function.getAnnotationsByType(Path::class).toList()
        require(pathAnnotations.size <= 1) {
            "only one ${Path::class} annotation is supported by standard"
        }
        val pathAnnotation = pathAnnotations.firstOrNull()
        return pathDescriptorFactory.create(pathAnnotation?.value.orEmpty())

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
        return createPathParameter(parameter)
            ?: createQueryParameter(parameter)
            ?: createHeaderParameter(parameter)
            ?: createBodyParameter(parameter)
    }

    private fun createBodyParameter(parameter: KSValueParameter) = HandlerParameterDescriptor.Body(
        parameter.name?.asString() ?: error("only named parameters are supported")
    )

    private fun createPathParameter(parameter: KSValueParameter): HandlerParameterDescriptor? {
        val pathParamAnnotation = parameter.getAnnotationsByType(PathParam::class).toList()
        if (pathParamAnnotation.size == 1) {
            return HandlerParameterDescriptor.Path(
                alias = parameter.name?.asString() ?: error("only named parameters are supported"),
                key = pathParamAnnotation.first().value,
            )
        }
        return null
    }

    private fun createQueryParameter(parameter: KSValueParameter): HandlerParameterDescriptor? {
        val pathParamAnnotation = parameter.getAnnotationsByType(QueryParam::class).toList()
        if (pathParamAnnotation.size == 1) {
            return HandlerParameterDescriptor.Query(
                alias = parameter.name?.asString() ?: error("only named parameters are supported"),
                key = pathParamAnnotation.first().value,
            )
        }
        return null
    }

    private fun createHeaderParameter(parameter: KSValueParameter): HandlerParameterDescriptor? {
        val pathParamAnnotation = parameter.getAnnotationsByType(HeaderParam::class).toList()
        if (pathParamAnnotation.size == 1) {
            return HandlerParameterDescriptor.Query(
                alias = parameter.name?.asString() ?: error("only named parameters are supported"),
                key = pathParamAnnotation.first().value,
            )
        }
        return null
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

        const val DEFAULT_CONTENT_TYPE = "*/*"
    }
}