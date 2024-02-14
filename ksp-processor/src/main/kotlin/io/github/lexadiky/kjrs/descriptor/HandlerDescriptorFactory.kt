package io.github.lexadiky.kjrs.descriptor

import com.google.devtools.ksp.symbol.KSFunctionDeclaration

class HandlerDescriptorFactory {

    fun create(function: KSFunctionDeclaration): HandlerDescriptor {
        return HandlerDescriptor(
            function.simpleName.asString()
        )
    }
}