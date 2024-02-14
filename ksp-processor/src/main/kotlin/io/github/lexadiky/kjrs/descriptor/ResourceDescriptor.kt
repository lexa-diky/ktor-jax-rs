package io.github.lexadiky.kjrs.descriptor

import io.github.lexadiky.kjrs.KtorJaxRsConfig

data class ResourceDescriptor(
    val packageQualifier: String,
    val simpleName: String,
    val handlers: List<HandlerDescriptor>
) {
    val qualifier = "$packageQualifier.$simpleName"

    fun syntheticImplementationName(config: KtorJaxRsConfig): String =
        config.resourceFileNameTemplate.format(simpleName)
}