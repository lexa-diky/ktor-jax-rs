package io.github.lexadiky.kjrs.codegen

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import org.intellij.lang.annotations.Language

internal class PreludeCodeGenerator {

    @Language("kotlin")
    fun addJaxRsPrelude(builder: TypeSpec.Builder): Unit = with(builder) {
        addFunction(
            FunSpec.builder("respondJaxRs")
                .addParameter("response", ClassQualifierLibrary.jaxRsResponse)
                .receiver(ClassQualifierLibrary.ktorApplicationCall)
                .addModifiers(KModifier.SUSPEND, KModifier.PRIVATE)
                .addCode("respond(response.entity)")
                .build()
        )
    }
}
