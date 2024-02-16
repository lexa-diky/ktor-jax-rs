package io.github.lexadiky.kjrs

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

@AutoService(SymbolProcessorProvider::class)
internal class KtorJaxRsSymbolProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return KtorJaxRsSymbolProcessor(
            environment.logger,
            environment.codeGenerator,
            KtorJaxRsConfig()
        )
    }
}