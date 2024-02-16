package io.github.lexadiky.kjrs.descriptor

internal data class PathDescriptor(
    val nodes: List<Node>
) {

    sealed interface Node {

        data class Static(val name: String): Node
        data class Parameter(val name: String, val regex: Regex?): Node
    }

    companion object Tokens {

        const val PATH_SEPARATOR = "/"
        const val PARAM_LEFT_BRACE = "{"
        const val PARAM_RIGHT_BRACE = "}"
        const val REGEX_SEPARATOR = ":"
    }
}