package io.github.lexadiky.kjrs.codegen

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import io.github.lexadiky.kjrs.KtorJaxRsConfig
import io.github.lexadiky.kjrs.descriptor.ResourceDescriptor

class JaxRsCodeGenerator(private val config: KtorJaxRsConfig) {

    fun generate(resource: ResourceDescriptor): FileSpec {
        return FileSpec.builder(
            resource.packageQualifier,
            config.resourceFileNameTemplate.format(resource.simpleName)
        )
            .addFileComment(createFileComment(resource))
            .addType(generateSyntheticImplementationType(resource))
            .addDefaultImports()
            .build()
    }

    private fun generateSyntheticImplementationType(resource: ResourceDescriptor): TypeSpec {
        val className = resource.syntheticImplementationName(config)
        return TypeSpec.classBuilder(className)
            .addModifiers(KModifier.PRIVATE)
            .addFunction(generateRootBindingFunction(resource))
            .build()
    }

    private fun generateRootBindingFunction(resource: ResourceDescriptor): FunSpec {
        return FunSpec.builder("bind")
            .addParameter("routing", ClassQualifierLibrary.ktorRouting)
            .build()
    }

    private fun createFileComment(resource: ResourceDescriptor): String = buildString {
        appendLine(config.banner)
        append("source: ${resource.qualifier}")
    }

    private fun FileSpec.Builder.addDefaultImports(): FileSpec.Builder {
        return addClassImport(ClassQualifierLibrary.ktorRouting)
    }
}