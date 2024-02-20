package io.github.lexadiky.kjrs.runtime

import com.google.auto.service.AutoService
import jakarta.ws.rs.SeBootstrap
import jakarta.ws.rs.core.Application
import jakarta.ws.rs.core.EntityPart
import jakarta.ws.rs.core.Link
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.UriBuilder
import jakarta.ws.rs.core.Variant
import jakarta.ws.rs.ext.RuntimeDelegate
import java.util.concurrent.CompletionStage

@AutoService(RuntimeDelegate::class)
class RuntimeDelegateImpl : RuntimeDelegate() {

    override fun createUriBuilder(): UriBuilder {
        TODO("Not yet implemented")
    }

    override fun createResponseBuilder(): Response.ResponseBuilder {
        return ResponseResponseBuilderImpl()
    }

    override fun createVariantListBuilder(): Variant.VariantListBuilder {
        TODO("Not yet implemented")
    }

    override fun <T : Any?> createEndpoint(p0: Application?, p1: Class<T>?): T {
        TODO("Not yet implemented")
    }

    override fun <T : Any?> createHeaderDelegate(p0: Class<T>?): HeaderDelegate<T> {
        TODO("Not yet implemented")
    }

    override fun createLinkBuilder(): Link.Builder {
        TODO("Not yet implemented")
    }

    override fun createConfigurationBuilder(): SeBootstrap.Configuration.Builder {
        TODO("Not yet implemented")
    }

    override fun bootstrap(p0: Application?, p1: SeBootstrap.Configuration?): CompletionStage<SeBootstrap.Instance> {
        TODO("Not yet implemented")
    }

    override fun bootstrap(
        p0: Class<out Application>?,
        p1: SeBootstrap.Configuration?
    ): CompletionStage<SeBootstrap.Instance> {
        TODO("Not yet implemented")
    }

    override fun createEntityPartBuilder(p0: String?): EntityPart.Builder {
        TODO("Not yet implemented")
    }
}