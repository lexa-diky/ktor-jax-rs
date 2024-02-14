package io.github.lexadiky.kjrs.codegen

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec

internal fun FileSpec.Builder.addClassImport(name: ClassName): FileSpec.Builder {
    return addImport(name.packageName, name.simpleName)
}