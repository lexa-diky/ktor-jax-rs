package io.github.lexadiky.kjrs.descriptor

import io.github.lexadiky.kjrs.KtorJaxRsConfig

internal data class ResourceDescriptor(
    val packageQualifier: String,
    val simpleName: String,
    val handlers: List<HandlerDescriptor>,
    val path: PathDescriptor
) {
    val qualifier = "$packageQualifier.$simpleName"

    fun syntheticImplementationName(config: KtorJaxRsConfig): String =
        config.resourceFileNameTemplate.format(simpleName)
}