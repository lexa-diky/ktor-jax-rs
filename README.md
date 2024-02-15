# Ktor JAX-RS

![GitHub License](https://img.shields.io/github/license/lexa-diky/ktor-jax-rs)

[JAX-RS](https://www.oracle.com/technical-resources/articles/java/jax-rs.html)
support for [Ktor](https://ktor.io) framework.

## Getting started

### Add KSP dependency
```kotlin
repositories {
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    ksp("io.github.lexa-diky.ktor-jax-rs:ksp-processor:0.0.4-SNAPSHOT")
}
```

## Features
- 🚀 Fast, no runtime reflection 
- 🪄 Plug & Play, no complex setup required
- ☂️ Readable and debuggable generated code
- 🥋 Coroutines support out of the box

## Example 

### Resource definition
```kotlin
@Path("/echo")
class EchoController {

    @GET
    @Path("/{text}")
    @Consumes("application/json")
    suspend fun echo(
        body: UUID,
        @PathParam("text") text: String,
        @HeaderParam("X-MY-CUSTOM-HEADER") customHeader: String
    ) {

    }
}
```

### Generated bindings
```kotlin
public class EchoControllerJaxRsGenerated(
    private val handlers: EchoController,
) {
    public fun bind(routing: Routing) {
        routing.route("/echo") {
            echo(this)
        }
    }

    private fun echo(route: Route) {
        route.route("/{text}") {
            method(HttpMethod("GET")) {
                accept(ContentType.parse("application/json")) {
                    handle {
                        handlers.echo(
                            body = call.receive(),
                            text = call.parameters.getOrFail("text"),
                            customHeader = call.request.queryParameters.getOrFail("X-MY-CUSTOM-HEADER"),
                        )
                    }
                }
            }
        }
    }
}
```
