package io.github.lexadiky.kjrs.descriptor

sealed interface ResponseDescriptor {

    data object JaxRsResponse : ResponseDescriptor

    data object AnyObject: ResponseDescriptor

    data object Unit: ResponseDescriptor
}