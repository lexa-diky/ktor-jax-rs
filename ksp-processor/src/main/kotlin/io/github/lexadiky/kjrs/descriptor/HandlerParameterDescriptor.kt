package io.github.lexadiky.kjrs.descriptor

sealed interface HandlerParameterDescriptor {

    val alias: String

    data class Query(
        override val alias: String,
        val key: String
    ) : HandlerParameterDescriptor

    data class Path(
        override val alias: String,
        val key: String
    ) : HandlerParameterDescriptor

    data class Header(
        override val alias: String,
        val key: String
    ) : HandlerParameterDescriptor


    data class Body(
        override val alias: String
    ) : HandlerParameterDescriptor
}