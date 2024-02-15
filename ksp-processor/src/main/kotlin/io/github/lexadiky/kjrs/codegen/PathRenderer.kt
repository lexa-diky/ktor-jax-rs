package io.github.lexadiky.kjrs.codegen

import io.github.lexadiky.kjrs.descriptor.PathDescriptor

internal class PathRenderer {

    fun render(path: PathDescriptor): String = path.nodes.joinToString(
        prefix = PathDescriptor.PATH_SEPARATOR,
        separator = PathDescriptor.PATH_SEPARATOR,
        transform = { node ->
            when (node) {
                is PathDescriptor.Node.Parameter -> render(node)
                is PathDescriptor.Node.Static -> node.name
            }
        }
    )


    private fun render(path: PathDescriptor.Node.Parameter): String = buildString {
        append(PathDescriptor.PARAM_LEFT_BRACE)
        append(path.name)
        if (path.regex != null) {
            append(PathDescriptor.REGEX_SEPARATOR)
            append(path.regex)
        }
        append(PathDescriptor.PARAM_RIGHT_BRACE)
    }
}