package io.github.lexadiky.kjrs.runtime

import jakarta.ws.rs.core.EntityTag
import jakarta.ws.rs.core.GenericType
import jakarta.ws.rs.core.Link
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.MultivaluedMap
import jakarta.ws.rs.core.NewCookie
import jakarta.ws.rs.core.Response
import java.net.URI
import java.util.Date
import java.util.Locale

class ResponseImpl(
    private val entity: Any?,
    private val statusCode: Int,
    private val statusReasonPhrase: String?,
    private val headers: Map<String, List<Any>>,
    private val language: Locale?
) : Response() {

    override fun close() {
        TODO("Not yet implemented")
    }

    override fun getStatus(): Int {
        TODO("Not yet implemented")
    }

    override fun getStatusInfo(): StatusType {
        TODO("Not yet implemented")
    }

    override fun getEntity(): Any? {
        return entity
    }

    override fun <T : Any?> readEntity(entityType: Class<T>?): T {
        TODO("Not yet implemented")
    }

    override fun <T : Any?> readEntity(entityType: GenericType<T>?): T {
        TODO("Not yet implemented")
    }

    override fun <T : Any?> readEntity(entityType: Class<T>?, annotations: Array<out Annotation>?): T {
        TODO("Not yet implemented")
    }

    override fun <T : Any?> readEntity(entityType: GenericType<T>?, annotations: Array<out Annotation>?): T {
        TODO("Not yet implemented")
    }

    override fun hasEntity(): Boolean {
        TODO("Not yet implemented")
    }

    override fun bufferEntity(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getMediaType(): MediaType {
        TODO("Not yet implemented")
    }

    override fun getLanguage(): Locale {
        TODO("Not yet implemented")
    }

    override fun getLength(): Int {
        TODO("Not yet implemented")
    }

    override fun getAllowedMethods(): MutableSet<String> {
        TODO("Not yet implemented")
    }

    override fun getCookies(): MutableMap<String, NewCookie> {
        TODO("Not yet implemented")
    }

    override fun getEntityTag(): EntityTag {
        TODO("Not yet implemented")
    }

    override fun getDate(): Date {
        TODO("Not yet implemented")
    }

    override fun getLastModified(): Date {
        TODO("Not yet implemented")
    }

    override fun getLocation(): URI {
        TODO("Not yet implemented")
    }

    override fun getLinks(): MutableSet<Link> {
        TODO("Not yet implemented")
    }

    override fun hasLink(relation: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun getLink(relation: String?): Link {
        TODO("Not yet implemented")
    }

    override fun getLinkBuilder(relation: String?): Link.Builder {
        TODO("Not yet implemented")
    }

    override fun getMetadata(): MultivaluedMap<String, Any> {
        TODO("Not yet implemented")
    }

    override fun getStringHeaders(): MultivaluedMap<String, String> {
        TODO("Not yet implemented")
    }

    override fun getHeaderString(name: String?): String {
        TODO("Not yet implemented")
    }
}