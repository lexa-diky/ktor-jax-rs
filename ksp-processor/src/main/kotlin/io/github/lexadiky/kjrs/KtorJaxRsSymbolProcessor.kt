package io.github.lexadiky.kjrs

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.writeTo
import io.github.lexadiky.kjrs.codegen.JaxRsCodeGenerator
import io.github.lexadiky.kjrs.descriptor.ResourceDescriptorFactory
import jakarta.ws.rs.Path
import kotlin.math.log

internal class KtorJaxRsSymbolProcessor(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
    ktorJaxRsConfig: KtorJaxRsConfig
) : SymbolProcessor {
    private val generator = JaxRsCodeGenerator(ktorJaxRsConfig)
    private val resourceDescriptorFactory = ResourceDescriptorFactory()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getSymbolsWithAnnotation(Path::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .forEach(::process)

        return emptyList()
    }

    private fun process(ksClass: KSClassDeclaration) {
        val descriptor = resourceDescriptorFactory.create(ksClass)
        generator.generate(descriptor)
            .writeTo(codeGenerator, aggregating = false)
    }
}