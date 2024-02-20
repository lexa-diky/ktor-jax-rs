@file:OptIn(KspExperimental::class)

package io.github.lexadiky.kjrs.descriptor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ClassName
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

internal class HandlerDescriptorFactory {
    private val pathDescriptorFactory = PathDescriptorFactory()

    fun create(function: KSFunctionDeclaration): List<HandlerDescriptor> {
        val httpMethods = resolveHttpMethods(function)
        val consumesContentTypes = resolveConsumesContentType(function)
        val producesContentTypes = resolveProducesContentType(function)
        val response = resolveResponse(function)

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
                        parameters = parameters,
                        response = response
                    )
                }
            }
        }
    }

    private fun resolveResponse(function: KSFunctionDeclaration): ResponseDescriptor {
        val returnType = function.returnType?.resolve() ?: return ResponseDescriptor.Unit

        return when(returnType.declaration.qualifiedName?.asString()) {
            KOTLIN_UNIT_QUALIFIED_NAME -> ResponseDescriptor.Unit
            JAX_RS_RESPONSE_QUALIFIED_NAME -> ResponseDescriptor.JaxRsResponse
            JAX_RS_RESPONSE_BUILDER_QUALIFIED_NAME -> error("returning response builder is not allowed")
            else -> ResponseDescriptor.AnyObject
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
        if (function.isAnnotationPresent(GET::class)){
            buffer += HttpMethod.GET
        }
        if (function.isAnnotationPresent(POST::class)){
            buffer += HttpMethod.POST
        }
        if (function.isAnnotationPresent(PUT::class)) {
            buffer += HttpMethod.PUT
        }
        if (function.isAnnotationPresent(DELETE::class)) {
            buffer += HttpMethod.DELETE
        }
        if (function.isAnnotationPresent(HEAD::class)){
            buffer += HttpMethod.HEAD
        }
        if (function.isAnnotationPresent(OPTIONS::class)){
            buffer += HttpMethod.OPTIONS
        }
        if (function.isAnnotationPresent(HttpMethod::class)) {
            buffer += function.getAnnotationsByType(HttpMethod::class).map { it.value }
        }
        if (buffer.isEmpty()) {
            buffer += HttpMethod.GET
        }

        return buffer
    }

    companion object {

        const val JAX_RS_RESPONSE_QUALIFIED_NAME = "jakarta.ws.rs.core.Response"
        const val JAX_RS_RESPONSE_BUILDER_QUALIFIED_NAME = "jakarta.ws.rs.core.Response.ResponseBuilder"
        const val KOTLIN_UNIT_QUALIFIED_NAME = "kotlin.Unit"
        const val DEFAULT_CONTENT_TYPE = "*/*"
    }
}