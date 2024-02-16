package io.github.lexadiky.kjrs.descriptor

import io.github.lexadiky.kjrs.util.AcceptsSanitizer

internal data class HandlerDescriptor(
    val handlerMethod: String,
    val httpMethod: String,
    val acceptsContentType: String,
    val producesContentType: String,
    val path: PathDescriptor,
    val parameters: List<HandlerParameterDescriptor>,
) {

    fun methodGroupQualifier(): String {
        return handlerMethod
    }
}

internal fun List<HandlerDescriptor>.groupByMethodGroup(): Map<String, List<HandlerDescriptor>> {
    return groupBy { it.methodGroupQualifier() }
}
