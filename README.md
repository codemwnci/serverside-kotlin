# Server Side Kotlin / Kotlin for Web Development
The purpose of this repository is to host a set of comparisons, with examples, of the many ways that a developer can create Web Applications with Kotlin as the server side language. 

Kotlin is a comprehensive and expressive language that makes it easier to develop than may other choices. However, it's fame has come from the Android community, and as such not enough attention has been given to serverside development, meaning that many developers are missing out on the boilerplate savings, simplicity and power of the Kotlin language.

This repository is a work in progress. It does not cover all frameworks and approaches. If your favoured framework is missing, please feel free to add a pull request (Contribution Guidelines are at the bottom).

## Frameworks
- [Spring Boot](/SpringBoot/)
- [SparkJava](/SparkJava/)
- Quarkus.io (pending detailed example)
- Jooby (pending detailed example)
- Ktor (pending)
- Vert.x (pending)
- Javalin.io (pending detailed example)
- Micronaut.io (pending)


## Spring Boot
[Spring](https://spring.io/) added formal support for Kotlin in [Spring 5 and SpringBoot 2](https://docs.spring.io/spring-boot/docs/2.1.0.RELEASE/reference/htmlsingle/#boot-features-kotlin). Whilst it was possible to develop Kotlin apps before, these later versions added idiomatic Kotlin syntax to further reduce boilerplate and prevent workarounds required to make Kotlin play nicely with Spring. As a result, building Spring Boot apps in Kotlin is now super simple.


```kotlin
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping(value = "/")
class MyController() {

    @GetMapping(value = "/")
    fun index() = "Hello World!"

    @GetMapping(value = "/hello/{name}")
    fun helloName(@PathVariable name: String) = "Hello {name}"


    @PostMapping(value = "/someAPI")
    fun postToAPI(@RequestBody body: String, res: HttpServletResponse): String {
        res.status = 201
        return "I created some resource using {body} honest!"
    }

    @DeleteMapping(value = "/someAPI/{id}")
    fun deleteResource(@PathVariable id: Long) = "Pretend resource is gone"
}

@SpringBootApplication
class BootdemoApplication

fun main(args: Array<String>) {
    runApplication<BootdemoApplication>(*args)    
}
```   

## SparkJava
[Spark Java](http://sparkjava.com/) is a micro framework, originally designed when Lambda's made it to Java 8, as a way to bring expressive web development to Java, inspired by Sinatra. Spark later enabled Kotlin, benefitting from Kotlin's syntax for allowing a final parameter lambda to be written as a body tag outside of the method call. Later, Spark began work on a Kotlin DSL, but this has largely been slow development and I will focus here on the Kotlin syntax over the original Java framework.

```kotlin
import spark.Spark.*

fun main(args: Array<String>) {
    port(8080)

    get("/") { req, res -> "Hello Kotlin!" }
    get("/hello/:name") { req, res -> "Hello: ${req.params(":name")}" }
    post("/someAPI") { req, res -> 
        res.status(201)
        "I created some resource using ${req.body()} honest!" 
    }
    delete("/someAPI/:id") { req, res -> "Pretend resource is gone" }
}
```

## Quarkus
[Quarkus](https://quarkus.io/) is another Java framework that has been given a Kotlin module (sound familiar yet!?). Quarkus is designed for Cloud Native, Containerised apps. It is not intended to run in a Main function like the other examples, but instead, uses Maven to execute `mvnw quarkus:dev`. When you are ready to compile, maven packages it up into a Jar and/or Container to run. It has super-fast startup times, and low memory requirements, but also comes with Native support via GraalVM, to give almost instantaneous boot times and even lower memory needs. See the website for more details.
The code itself is very straightforward, concise, yet powerful.

```kotlin
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response;

@Path("/hello")
class HelloResource {

    data class Greeting(val message: String = "Hello Kotlin!")

    @GET @Produces(MediaType.APPLICATION_JSON)
    fun hello() = Greeting()
    
    @GET @Path("/{name}")
    fun helloName(@PathParam("name") name: String) = Greeting("Hello $name!")
    
    @POST @Produces(MediaType.TEXT_PLAIN)
    fun postToAPI(body: String) = Response.ok("I created some resource using $body honest!").status(201).build()

    @DELETE @Path("/{id}")
    fun deleteResource(@PathParam("id") id: Long): String {
        // do some DB work to delete the resource
        return "pretend resource is gone"
    }
}

```



## Jooby
[Jooby](https://jooby.org/) is another Java microframework that has been given a Kotlin module to benefit from the idioms of Kotlin to reduce boilerplate. Unfortunately, unlike SparkJava, the examples are very sparse (even the example on the Jooby site has a single get function as the example project). It does however have some nice features such as the use of implicits to reduce boilerplate further. 


```kotlin
import org.jooby.*

fun main(args: Array<String>) {
    run(*args) {
        get("/") { "Hello Kotlin" }
        get("/hello/:name") { "Hello: ${param("name").value}" }
        post("/someAPI") { req, res ->
            res.status(201)
            res.send("I created some resource using ${req.body(String::class.java)} honest!")
        }
        delete("/someAPI/:id") { "Pretend resource is gone" }
    }
}
```

## Javalin.io
[Javalin.io](https://javalin.io/) unlike many of the frameworks described here, describes itself as a lightweight web framework for Java **and** Kotlin. It is super simple to set up, and very easy to get running. There are lots of similarities with Javalin and SparkJava, but Javalin seems to be more regularly updated, and more actively supported. It is very lightweight, but has a decent set of features for building REST endpoints.
Whilst this example demonstrates sending text responses, it is also very simple to send JSON, futures, or rendered HTML.

```kotlin
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*

fun main() {
    val app = Javalin.create().start(7000)
    
    app.routes {
        path("hello") {
            get("/") { ctx ->
                ctx.result("Hello Kotlin")
            }

            get("/{name}") { ctx ->
                ctx.result("Hello ${ ctx.pathParam("name") }")
            }

            post("/") { ctx ->
                ctx.status(201)
                ctx.result("I created some resource using ${ctx.body()} honest!")
            }

            delete("/{id}") { ctx ->
                // get resource using ctx.pathParam("id")
                ctx.result("Pretend resource is gone")
            }
        }
    }
}
```

    
# Contributors
[@codemwnci](https://github.com/codemwnci)


# Contribution Guidelines
To contribute a new framework, please follow these simple steps
1. Create a new directory below this main one, with the name of the Framework. This should contain a full example, with a README.md acting as a tutorial
1. Add the Framework name to the Frameworks list (second heading)
1. Add a short code example of the framework on this README.md file, follow the Spring Boot section as an example. 
1. Add a link from Frameworks List (Step 2) and the Example Section (Step 3) to the Example directory (Step 1)
1. Add your name to the Contributors list on this README.md file

