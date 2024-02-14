package io.github.lexadiky.kjrs.descriptor

import com.google.devtools.ksp.symbol.KSClassDeclaration

class ResourceDescriptorFactory {

    fun create(ksClassDeclaration: KSClassDeclaration): ResourceDescriptor {
       return ResourceDescriptor(
           packageQualifier = ksClassDeclaration.packageName.asString(),
           simpleName =  ksClassDeclaration.simpleName.asString()
       )
    }
}