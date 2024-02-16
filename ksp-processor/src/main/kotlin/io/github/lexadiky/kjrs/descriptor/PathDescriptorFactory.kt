package io.github.lexadiky.kjrs.descriptor

class PathDescriptorFactory {

    fun create(rawPath: String): PathDescriptor {
        if (rawPath.isEmpty()) {
            return PathDescriptor(emptyList())
        }

        require(rawPath.startsWith(PathDescriptor.PATH_SEPARATOR)) {
            "path values must start with ${PathDescriptor.PATH_SEPARATOR}"
        }
        require(!rawPath.endsWith(PathDescriptor.PATH_SEPARATOR)) {
            "path values must not have ${PathDescriptor.PATH_SEPARATOR} at the end"
        }

        val nodes = rawPath.split(PathDescriptor.PATH_SEPARATOR)
            .filter(String::isNotEmpty)
            .map(::parseToken)

        return PathDescriptor(nodes)
    }

    private fun parseToken(token: String): PathDescriptor.Node {
        return if (token.startsWith(PathDescriptor.PARAM_LEFT_BRACE) && token.endsWith(PathDescriptor.PARAM_RIGHT_BRACE)) {
            parseParamToken(token)
        } else {
            PathDescriptor.Node.Static(token)
        }
    }

    private fun parseParamToken(token: String): PathDescriptor.Node.Parameter {
        val tokenInnerValue = token.substring(1, token.length - 1)
        return if (tokenInnerValue.contains(":")) {
            parseParamTokenWithRegex(token)
        } else {
            PathDescriptor.Node.Parameter(tokenInnerValue, null)
        }
    }

    private fun parseParamTokenWithRegex(token: String): PathDescriptor.Node.Parameter {
        val (name, regexRaw) = token.split(PathDescriptor.REGEX_SEPARATOR.toRegex(), 2)
        return PathDescriptor.Node.Parameter(name, regexRaw.toRegex())
    }
}