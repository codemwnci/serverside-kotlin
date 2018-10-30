# Server Side Kotlin / Kotlin for Web Development
The purporse of this repository is to host a set of comparisons, with examples, of the many ways that a developer can create Web Applications with Kotlin as the server side language. 

Kotlin is a comprehensive and expressive language that makes it easier to develop than may other choices. However, it's fame has come from the Android community, and as such not enough attention has been given to serverside development, meaning that many developers are missing out on the boilerplate savings, simplicity and power of the Kotlin language.

This repository is a work in progress. It does not cover all frameworks and approaches. If your favoured framework is missing, please feel free to add a pull request (Contribution Guidelines are at the bottom).

## Frameworks
- Spring Boot
- SparkJava (pending)
- Jooby (pending)
- Ktor (pending)
- Vert.x (pending)


## Spring Boot
Spring added formal support for Kotlin in Spring 5 and Spring Boot 2. Whilst it was possible to develop Kotlin apps before, these later versions added idiomatic Kotlin syntax to further reduce boilerplate and prevent workarounds required to make Kotlin play nicely with Spring. As a result, building Spring Boot apps in Kotlin is now super simple.


## SparkJava
Spark Java is a micro framework, originally designed when Lambda's made it to Java 8, as a way to bring expressive web development to Java, inspired by Sinatra. Spark later enabled Kotlin, benefitting from Kotlin's syntax for allowing a final parameter lambda to be written as a body tag outside of the method call. Later, Spark began work on a Kotlin DSL, but this has largely been slow development and I will focus here on the Kotlin syntax over the original Java framework.

    import spark.Spark.*

    fun main(args: Array<String>) {
        port(8080)
        
        get("/") { req, res -> "Hello Kotlin!" }
        get("/hello/:name") { req, res -> "Hello: " + req.params(":name") }
        post("/someAPI") { req, res -> 
            res.status(201)
            "I created some resource using ${req.body()} honest!" 
        }
        delete("/someAPI/:id") { req, res -> "Pretend resource is gone" }
    }

# Contributors
- @codemwnci


# Contribution Guidelines
To contribute a new framework, please follow these simple steps
1. Create a new directory below this main one, with the name of the Framework. This should contain a full example, with a README.md acting as a tutorial
1. Add the Framework name to the Frameworks list (second heading)
1. Add a short code example of the framework on this README.md file, follow the Spring Boot section as an example. 
1. Add a link from Frameworks List (Step 2) and the Example Section (Step 3) to the Example directory (Step 1)
1. Add your name to the Contributors list on this README.md file

