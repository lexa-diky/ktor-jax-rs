package io.github.lexadiky.kjrs.util

internal object AcceptsSanitizer {

    fun sanitize(expression: String): String {
        return expression.replace("*", "any")
            .replace("/", "_")
    }
}