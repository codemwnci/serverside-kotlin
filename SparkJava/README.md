# ServerSide Kotlin - SparkJava Example
This is a more complete example of building a simple web app in Kotlin, using Spark Java.
In keeping with recent tradition, we will build a ToDo application. 

Reminder: The SparkJava WebPage [can be found here](http://sparkjava.com/) 

## The Code

In this example, the application is made up of 2 separate files.
1. The simple VueJS app
1. The SparkJava Controller (this also contains the DAOs and DB setup code)

### Front End Code
I am not going to go into any detail of the VueJS app that powers the front end. It is a very simple JavaScript application that allows a Todo to be Created, Updated (marked as complete), Listed and Deleted. If you would prefer to use CURL see the CURL commands at the bottom of the page. 

### Kotlin Server Side Code
Unlike the SpringBoot example, the SparkJava appears to be more verbose. This is in direct contrast the the mini-examples shown on the main page, where Spring appeared the most verbose. This comes down to one major reason; Spring is using JPA to hide the overhead of DB setup and SQL queries to reduce the amount of code you have to write. This is a matter of taste and control. I love JPA for its simplicity in these cases, but have often come up against use cases where I have struggled to get JPA to do what I want. Therefore, we have a balance between verbosity and flexibility.

To work through the Kotlin code, let's break it down into a few chunks.

The SetupDB function at the bottom of the file is a simple function that creates a DataSource, and then executes an SQL to create a table. This is only used because we are using H2 in memory database, so we need to recreate the database on server start each time.

Next, let's inspect the code `fun Any.toJson() = jacksonObjectMapper().writeValueAsString(this)`. This neat little function is an extension function, applied to the root class Any, and adds a toJson function to it. This simply passes the object to the Jackson Object Mapper and outputs it as a string. This is used to simplify the code so that we can return List<Todo> and Todo objects as JSON by simply calling toJson on the object.

You will notice that the remainder of the code is inside the MAIN function. This is simply the way SparkJava adds it's routes to the Spark Server. Before we go into inspecting the code for some of the routes though, let's look at a few lines of code that sit outside of these routes.

```kotlin
data class Todo(val id: Long, val text: String, val done: Boolean, val createdAt: LocalDateTime)
val toTodo: (Row) -> Todo = { row -> Todo(row.long(1), row.string(2), row.boolean(3), row.localDateTime(4))}
fun getTodo(id: Long): Todo? = using(sessionOf(HikariCP.dataSource())) { session ->
    session.run(queryOf("select id, text, done, created_at from todo where id=?", id).map(toTodo).asSingle)
}
```

The data class is pretty straightforward. This is our DTO, using the neat Kotlin way to create domain objects.
The next line is a function value. It defines a lambda that takes a database `Row` and returns a `Todo` DTO, and does so by specifying the columns and datatypes to read from the result set row.
The final set of lines uses the above lambda. Here we are using Kotliquery to assist us in the DB interactions, which is where the `using` statment comes in. We then run a query over the DataSource session, which is a simple select returning a single Todo from an ID, which then maps the result set to an object (using the previously mentioned `toTodo` lambda). The asSingle indicates to Kotliquery that we are expecting a single result, so to return a single Todo, rather than a List<Todo>.

So, now we have our DTO, our Database, and a few helpers to transfer data from the database to the DTO, so now let's take a look at a few of the routes. We won't discuss all of them, because there is a lot of repetition here.

The first part of the routes is the `path("/todo/")`. This is a simple shorthand, to prevent us having to prepend `/todo/` onto each path that is to come. 

```kotlin
get("") { req, res ->
    val todos: List<Todo> = using(sessionOf(HikariCP.dataSource())) { session ->
        session.run( queryOf("select id, text, done, created_at from todo").map(toTodo).asList )
    }
    todos.toJson()
}
```

This first route is pretty straightforward. Similar to the `getTodo` helper function we have already seen above, this route gets a list of Todos, by endinging the database call with `asList`. Next, we simply call the `toJson` extension function that we previously created, and our JSON will return back to the calling client.

```kotlin
get(":id") { req, res ->
    getTodo(req.params("id").toLong())?.toJson()
}
```

This next route is even simpler. With an id being passed to the route `GET /todo/:id`, we can extract the parameter, pass it to our getTodo helper function that we have already discussed, and against call the toJson function to return the single ToDo object back to the client. 

```kotlin
post("") { req, res ->
    if (req.body().isNullOrEmpty()) badRequest("a todo cannot be blank")

    val todo = req.body()
    val id = using(sessionOf(HikariCP.dataSource())) { session ->
        session.run(queryOf("insert into todo (text) values(?)", todo).asUpdateAndReturnGeneratedKey)
    }

    if (id == null) internalServerError("there was a problem creating the Todo")
    else getTodo(id)?.toJson()
}
```

This final route that we will discuss is a little more complex, but still pretty simple. First of all, we are using POST rather than GET. Next we do a simple check, and halt proceedings if it is null or empty (badRequest, calls `halt`, which is SparkJava function that throws an exception to prevent further processing of the route).

Next we extract the content of the RequestBody, which is the text of the Todo. We then run an insert statement. When running insert statements with Kotliquery, we have two options 1. `asUpdate`, 2. `asUpdateAndReturnGeneratedKey`, the former returns an integer with the number of rows affected, the latter returns the generated ID. In our case, we want the auto-generated ID of the Todo.

Next we check the ID is not null, which will confirm if the Todo was inserted correctly, and finally we load the Todo back from the database, and return it to the client as a JSON. The purpose of loading it from the database is so that we get all the default values, such as the created timestamp, and defaulting the completed to false. We could have done this at the application tier, but the database is our single source of truth, and as such I find this approach reduces bugs.

I won't go into detail on the PUT and DELETE requests, hopefully the code is self explanatory, with the only real addition not already discussed is loading the JSON passed in the body in the PUT into a JSON object using `jacksonObjectMapper().readTree(req.body())`, the rest is much the same as what we have already discussed.

So, now we have a serverside that fully replicates the SpringBoot application.


## Run The Application
From the SparkJava directory, first compile the code `mvn compile`, then run using `mvn exec:java -Dexec.mainClass="codemwnci.ServerKt"`


## Test The REST API
Go to http://localhost:8080 and use the provided front end. Enter a Todo in the text box and press enter. Done!

Alternatively, if you would rather use CURL
- Create Todo: `curl -d "Hello" -H "Content-Type:application/text" http://localhost:8080/todo/`
- Read Single Todo: `curl http://localhost:8080/todo/1` 
- Update Todo: `curl -X PUT -d "{\"text\":\"Hello\",\"done\":true}" -H "Content-Type: application/json" http://localhost:8080/todo/1`
- List Todo: `curl http://localhost:8080/todo/` 
- Delete Todo: `curl -X DELETE http://localhost:8080/todo/1`