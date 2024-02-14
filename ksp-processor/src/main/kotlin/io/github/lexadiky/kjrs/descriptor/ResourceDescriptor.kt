package io.github.lexadiky.kjrs.descriptor

data class ResourceDescriptor(
    val packageQualifier: String,
    val simpleName: String
) {

    val qualifier = "$packageQualifier.$simpleName"
}