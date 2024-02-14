package io.github.lexadiky.kjrs.descriptor

import io.github.lexadiky.kjrs.util.AcceptsSanitizer

data class HandlerDescriptor(
    val handlerMethod: String,
    val httpMethod: String,
    val contentType: String,
    val parameters: List<HandlerParameterDescriptor>,
) {

    fun bindingUniqueQualifier(): String {
        val sanContentType = AcceptsSanitizer.sanitize(contentType)
        val baseName = "${httpMethod.lowercase()}${handlerMethod.capitalize()}_$sanContentType"

        if (parameters.isEmpty()) {
            return baseName
        }


        val parameterAliases = parameters.joinToString(separator = "_") { it.alias }
        return "${baseName}_$parameterAliases"
    }
}