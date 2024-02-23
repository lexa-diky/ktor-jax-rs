package io.github.lexadiky.kjrs.runtime

import jakarta.ws.rs.core.CacheControl
import jakarta.ws.rs.core.EntityTag
import jakarta.ws.rs.core.Link
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.MultivaluedMap
import jakarta.ws.rs.core.NewCookie
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Variant
import java.net.URI
import java.util.Date
import java.util.Locale

data class ResponseResponseBuilderImpl internal constructor(
    private var entity: Any? = null,
    private var statusCode: Int = 200,
    private var statusReasonPhrase: String? = null,
    private var headers: Map<String, List<Any>> = emptyMap(),
    private var language: Locale? = null
) : Response.ResponseBuilder() {

    override fun build(): Response {
        return ResponseImpl(
            entity = entity,
            statusCode = statusCode,
            statusReasonPhrase = statusReasonPhrase,
            headers = headers,
            language = language
        )
    }

    override fun clone(): Response.ResponseBuilder {
        return copy()
    }

    override fun status(code: Int): Response.ResponseBuilder = apply {
        statusCode = code
    }

    override fun status(code: Int, reasonPhrase: String?): Response.ResponseBuilder = apply {
        statusCode = code
        statusReasonPhrase = reasonPhrase
    }

    override fun entity(entity: Any?): Response.ResponseBuilder = apply {
        this.entity = entity
    }

    override fun entity(p0: Any?, p1: Array<out Annotation>?): Response.ResponseBuilder {
        TODO("Not yet implemented")
    }

    override fun allow(vararg p0: String?): Response.ResponseBuilder {
        TODO("Not yet implemented")
    }

    override fun allow(p0: MutableSet<String>?): Response.ResponseBuilder {
        TODO("Not yet implemented")
    }

    override fun cacheControl(p0: CacheControl?): Response.ResponseBuilder {
        TODO("Not yet implemented")
    }

    override fun encoding(p0: String?): Response.ResponseBuilder {
        TODO("Not yet implemented")
    }

    override fun header(header: String?, value: Any?): Response.ResponseBuilder {
        if (header == null || value == null) {
            return this
        }

        val mm = headers.toMutableMap()
        val buff = mm.getOrDefault(header, emptyList())

        mm[header] = buff + value

        this.headers = mm
        return this
    }

    override fun replaceAll(p0: MultivaluedMap<String, Any>?): Response.ResponseBuilder {
        TODO("Not yet implemented")
    }

    override fun language(language: String?): Response.ResponseBuilder = apply {
        this.language = Locale.forLanguageTag(language)
    }

    override fun language(locale: Locale?): Response.ResponseBuilder = apply {
        this.language = locale
    }

    override fun type(p0: MediaType?): Response.ResponseBuilder {
        TODO("Not yet implemented")
    }

    override fun type(p0: String?): Response.ResponseBuilder {
        TODO("Not yet implemented")
    }

    override fun variant(p0: Variant?): Response.ResponseBuilder {
        TODO("Not yet implemented")
    }

    override fun contentLocation(p0: URI?): Response.ResponseBuilder {
        TODO("Not yet implemented")
    }

    override fun cookie(vararg p0: NewCookie?): Response.ResponseBuilder {
        TODO("Not yet implemented")
    }

    override fun expires(p0: Date?): Response.ResponseBuilder {
        TODO("Not yet implemented")
    }

    override fun lastModified(p0: Date?): Response.ResponseBuilder {
        TODO("Not yet implemented")
    }

    override fun location(p0: URI?): Response.ResponseBuilder {
        TODO("Not yet implemented")
    }

    override fun tag(p0: EntityTag?): Response.ResponseBuilder {
        TODO("Not yet implemented")
    }

    override fun tag(p0: String?): Response.ResponseBuilder {
        TODO("Not yet implemented")
    }

    override fun variants(vararg p0: Variant?): Response.ResponseBuilder {
        TODO("Not yet implemented")
    }

    override fun variants(p0: MutableList<Variant>?): Response.ResponseBuilder {
        TODO("Not yet implemented")
    }

    override fun links(vararg p0: Link?): Response.ResponseBuilder {
        TODO("Not yet implemented")
    }

    override fun link(p0: URI?, p1: String?): Response.ResponseBuilder {
        TODO("Not yet implemented")
    }

    override fun link(p0: String?, p1: String?): Response.ResponseBuilder {
        TODO("Not yet implemented")
    }
}