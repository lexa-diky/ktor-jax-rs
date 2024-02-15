package io.github.lexadiky.kjrs.util

import com.squareup.kotlinpoet.CodeBlock

internal fun CodeBlock.Builder.withControlFlow(
    controlFlow: String,
    vararg args: Any?,
    condition: Boolean = true,
    body: () -> Unit
) {
    if (condition) {
        beginControlFlow(controlFlow, *args)
        body()
        endControlFlow()
    } else {
        body()
    }
}