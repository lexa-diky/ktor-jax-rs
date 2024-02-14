package io.github.lexadiky.kjrs.descriptor

import io.github.lexadiky.kjrs.util.AcceptsSanitizer

data class HandlerDescriptor(
    val handlerMethod: String,
    val httpMethod: String,
    val acceptsContentType: String,
    val producesContentType: String,
    val parameters: List<HandlerParameterDescriptor>,
) {

    fun bindingUniqueQualifier(): String {
        val sanAccContentType = AcceptsSanitizer.sanitize(acceptsContentType)
        val sanProdContentType = AcceptsSanitizer.sanitize(acceptsContentType)

        val baseName =
            "${httpMethod.lowercase()}${handlerMethod.capitalize()}_${sanAccContentType}_${sanProdContentType}"

        if (parameters.isEmpty()) {
            return baseName
        }


        val parameterAliases = parameters.joinToString(separator = "_") { it.alias }
        return "${baseName}_$parameterAliases"
    }
}