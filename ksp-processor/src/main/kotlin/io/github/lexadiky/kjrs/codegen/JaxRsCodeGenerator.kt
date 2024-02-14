package io.github.lexadiky.kjrs.codegen

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import io.github.lexadiky.kjrs.KtorJaxRsConfig
import io.github.lexadiky.kjrs.descriptor.HandlerDescriptor
import io.github.lexadiky.kjrs.descriptor.ResourceDescriptor

class JaxRsCodeGenerator(private val config: KtorJaxRsConfig) {

    fun generate(resource: ResourceDescriptor): FileSpec {
        return FileSpec.builder(
            resource.packageQualifier,
            config.resourceFileNameTemplate.format(resource.simpleName)
        )
            .indent(" ".repeat(4))
            .addFileComment(createFileComment(resource))
            .addType(generateSyntheticImplementationType(resource))
            .addDefaultImports()
            .build()
    }

    private fun generateSyntheticImplementationType(resource: ResourceDescriptor): TypeSpec {
        val className = resource.syntheticImplementationName(config)
        val builder = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.PRIVATE)
            .addFunction(generateRootBindingFunction(resource))


        resource.handlers.map(::generateBindingFunction)
            .forEach(builder::addFunction)

        return builder.build()
    }

    private fun generateRootBindingFunction(resource: ResourceDescriptor): FunSpec {
        return FunSpec.builder("bind")
            .addParameter("routing", ClassQualifierLibrary.ktorRouting)
            .addCode(generateRootBindingFunctionBody(resource))
            .build()
    }

    private fun generateRootBindingFunctionBody(resource: ResourceDescriptor): CodeBlock {
        val cb = CodeBlock.builder()

        resource.handlers.forEach { handler ->
            cb.addStatement("%L(routing)", handler.bindingUniqueQualifier())
        }

        return cb.build()
    }

    private fun generateBindingFunction(handler: HandlerDescriptor): FunSpec {
        val builder = FunSpec.builder(handler.bindingUniqueQualifier())
            .addParameter("routing", ClassQualifierLibrary.ktorRouting)

        return builder.build()
    }

    private fun createFileComment(resource: ResourceDescriptor): String = buildString {
        appendLine(config.banner)
        append("source: ${resource.qualifier}")
    }

    private fun FileSpec.Builder.addDefaultImports(): FileSpec.Builder {
        return addClassImport(ClassQualifierLibrary.ktorRouting)
    }
}