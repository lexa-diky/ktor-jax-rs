@file:OptIn(KspExperimental::class)

package io.github.lexadiky.kjrs.descriptor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSClassDeclaration
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.HEAD
import jakarta.ws.rs.HttpMethod
import jakarta.ws.rs.OPTIONS
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT

class ResourceDescriptorFactory {
    private val handlerDescriptorFactory = HandlerDescriptorFactory()

    fun create(ksClassDeclaration: KSClassDeclaration): ResourceDescriptor {
        return ResourceDescriptor(
            packageQualifier = ksClassDeclaration.packageName.asString(),
            simpleName = ksClassDeclaration.simpleName.asString(),
            handlers = resolveHandlers(ksClassDeclaration).flatten()
        )
    }

    private fun resolveHandlers(ksClassDeclaration: KSClassDeclaration) =
        ksClassDeclaration.getAllFunctions()
            .filter { function ->
                HANDLER_MARKER_ANNOTATIONS
                    .any { handlerAnnotation -> function.isAnnotationPresent(handlerAnnotation) }
            }
            .map(handlerDescriptorFactory::create)
            .toList()

    companion object {

        private val HANDLER_MARKER_ANNOTATIONS = listOf(
            GET::class,
            POST::class,
            PUT::class,
            DELETE::class,
            HEAD::class,
            OPTIONS::class,
            HttpMethod::class,
        )
    }
}