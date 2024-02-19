package io.github.lexadiky.kjrs.descriptor

internal data class HandlerDescriptor(
    val handlerMethod: String,
    val httpMethod: String,
    val acceptsContentType: String,
    val producesContentType: String,
    val path: PathDescriptor,
    val parameters: List<HandlerParameterDescriptor>,
    val response: ResponseDescriptor
) {

    fun methodGroupQualifier(): String {
        return handlerMethod
    }
}

internal fun List<HandlerDescriptor>.groupByMethodGroup(): Map<String, List<HandlerDescriptor>> {
    return groupBy { it.methodGroupQualifier() }
}
