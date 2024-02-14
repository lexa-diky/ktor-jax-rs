package io.github.lexadiky.kjrs.codegen

import com.squareup.kotlinpoet.FileSpec
import io.github.lexadiky.kjrs.KtorJaxRsConfig
import io.github.lexadiky.kjrs.descriptor.ResourceDescriptor

class JaxRsCodeGenerator(private val config: KtorJaxRsConfig) {

    fun generate(resource: ResourceDescriptor): FileSpec {
        return FileSpec.builder(
            resource.packageQualifier,
            config.resourceFileNameTemplate.format(resource.simpleName)
        )
            .addFileComment(createFileComment(resource))
            .build()
    }

    private fun createFileComment(resource: ResourceDescriptor): String = buildString {
        appendLine(config.banner)
        append("source: ${resource.qualifier}")
    }
}