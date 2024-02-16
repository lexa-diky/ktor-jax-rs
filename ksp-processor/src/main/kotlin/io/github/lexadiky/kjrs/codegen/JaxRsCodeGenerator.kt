package io.github.lexadiky.kjrs.codegen

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import io.github.lexadiky.kjrs.KtorJaxRsConfig
import io.github.lexadiky.kjrs.descriptor.HandlerDescriptor
import io.github.lexadiky.kjrs.descriptor.HandlerDescriptorFactory.Companion.DEFAULT_CONTENT_TYPE
import io.github.lexadiky.kjrs.descriptor.HandlerParameterDescriptor
import io.github.lexadiky.kjrs.descriptor.ResourceDescriptor
import io.github.lexadiky.kjrs.descriptor.groupByMethodGroup
import io.github.lexadiky.kjrs.util.withControlFlow

internal class JaxRsCodeGenerator(private val config: KtorJaxRsConfig) {
    private val pathRenderer = PathRenderer()

    fun generate(resource: ResourceDescriptor): FileSpec {
        return FileSpec.builder(
            resource.packageQualifier,
            config.resourceFileNameTemplate.format(resource.simpleName)
        )
            .indent(" ".repeat(4))
            .addFileComment(createFileComment(resource))
            .addType(generateSyntheticImplementationType(resource))
            .addDefaultImports()
            .build()
    }

    private fun generateSyntheticImplementationType(resource: ResourceDescriptor): TypeSpec {
        val className = resource.syntheticImplementationName(config)
        val builder = TypeSpec.classBuilder(className)
            .addFunction(generateRootBindingFunction(resource))
            .primaryConstructor(generateConstructor(resource))
            .addProperty(generateHandlerProperty(resource))

        resource.handlers.groupByMethodGroup()
            .map { (groupName, descriptors) -> generateBindingFunctionForGroup(groupName, descriptors) }
            .forEach(builder::addFunction)

        return builder.build()
    }

    private fun generateHandlerProperty(resource: ResourceDescriptor): PropertySpec {
        return PropertySpec.builder("handlers", ClassName.bestGuess(resource.qualifier))
            .addModifiers(KModifier.PRIVATE)
            .initializer("handlers")
            .build()
    }

    private fun generateConstructor(resource: ResourceDescriptor): FunSpec {
        return FunSpec.constructorBuilder()
            .addParameter("handlers", ClassName.bestGuess(resource.qualifier))
            .build()
    }

    private fun generateRootBindingFunction(resource: ResourceDescriptor): FunSpec {
        val builder = FunSpec.builder("bind")
            .addParameter("routing", ClassQualifierLibrary.ktorRouting)

        val cb = CodeBlock.builder()
            .beginControlFlow("routing.route(%S)", pathRenderer.render(resource.path))
            .add(generateRootBindingFunctionBody(resource))
            .endControlFlow()

        return builder
            .addCode(cb.build())
            .build()
    }

    private fun generateRootBindingFunctionBody(resource: ResourceDescriptor): CodeBlock {
        val cb = CodeBlock.builder()


        resource.handlers.groupByMethodGroup().forEach { (groupName, _) ->
            cb.addStatement("%L(this)", groupName)
        }

        return cb.build()
    }

    private fun generateBindingFunctionForGroup(groupName: String, handlers: List<HandlerDescriptor>): FunSpec {
        val builder = FunSpec.builder(groupName)
            .addModifiers(KModifier.PRIVATE)
            .addParameter("route", ClassQualifierLibrary.ktorRoute)

        val bodyBuilder = CodeBlock.builder()

        handlers.forEach { handler ->
            bodyBuilder.withControlFlow("with(route)") {
                bodyBuilder.withControlFlow(
                    "route(%S)",
                    pathRenderer.render(handler.path),
                    condition = handler.path.nodes.isNotEmpty()
                ) {
                    bodyBuilder.withControlFlow(
                        "method(HttpMethod(%S))",
                        handler.httpMethod
                    ) {
                        bodyBuilder.withControlFlow(
                            "accept(ContentType.parse(%S))",
                            handler.acceptsContentType,
                            condition = handler.acceptsContentType != DEFAULT_CONTENT_TYPE
                        ) {
                            bodyBuilder.withControlFlow("handle") {
                                bodyBuilder
                                    .addStatement("%L.%L(", "handlers", handler.handlerMethod)
                                    .indent()

                                handler.parameters.forEach { parameter ->
                                    bodyBuilder.add("%L = ", parameter.alias)
                                    bodyBuilder.add(renderParameter(parameter))
                                    bodyBuilder.add(",\n")
                                }

                                bodyBuilder
                                    .unindent()
                                    .addStatement(")")
                            }
                        }
                    }
                }
            }
        }

        builder.addCode(bodyBuilder.build())
        return builder.build()
    }

    private fun renderParameter(parameter: HandlerParameterDescriptor): CodeBlock {
        return when (parameter) {
            is HandlerParameterDescriptor.Body -> CodeBlock.of("call.receive()")
            is HandlerParameterDescriptor.Path -> CodeBlock.of("call.parameters.getOrFail(%S)", parameter.key)
            is HandlerParameterDescriptor.Query -> CodeBlock.of(
                "call.request.queryParameters.getOrFail(%S)",
                parameter.key
            )

            is HandlerParameterDescriptor.Header -> CodeBlock.of("call.request.headers[%S]!!", parameter.key)
        }
    }

    private fun createFileComment(resource: ResourceDescriptor): String = buildString {
        appendLine(config.banner)
        append("source: ${resource.qualifier}")
    }

    private fun FileSpec.Builder.addDefaultImports(): FileSpec.Builder {
        return addClassImport(ClassQualifierLibrary.ktorRouting)
            .addClassImport(ClassQualifierLibrary.ktorRoute)
            .addClassImport(ClassQualifierLibrary.ktorRouteFn)
            .addClassImport(ClassQualifierLibrary.ktorHttpMethod)
            .addClassImport(ClassQualifierLibrary.ktorCallFn)
            .addClassImport(ClassQualifierLibrary.ktorGetOrFailFn)
            .addClassImport(ClassQualifierLibrary.ktorMethodFn)
            .addClassImport(ClassQualifierLibrary.ktorBodyReceiveFn)
            .addClassImport(ClassQualifierLibrary.ktorContentType)
            .addClassImport(ClassQualifierLibrary.ktorAcceptFn)
    }
}