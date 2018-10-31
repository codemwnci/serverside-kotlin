# ServerSide Kotlin - SparkJava Example

This is a more complete example of building a simple web app in Kotlin, using SpringBoot. In keeping with recent tradition, we will build a ToDo application.

## The Code

In this example, the application is made up of 3 separate files.
1. The simple VueJS app
1. The SpringBoot application launcher
1. The TodoService (this contains the REST api, the JPA Repository and the JPA Entity)

### Front End Code
I am not going to go into any detail of the VueJS app that powers the front end. It is a very simple JavaScript application that allows a Todo to be Created, Updated (marked as complete), Listed and Deleted. If you would prefer to use CURL see the CURL commands at the bottom of the page. 

### Kotlin Server Side Code
There are two Kotlin files in this SpringBoot example. It would have been possible to merge them into one, but as a good practice of building out into a larger application, this approach is far neater.

#### SpringBootApp
This is a default SpringBoot Application launcher. This code would be auto-generated if you create your starter project from https://start.spring.io.
All this code does is initiate SpringBoot, which in turn tells Spring to look up the Spring Annotations to initialise controllers, dependency injection, etc.

#### TodoService
Let's start at the bottom of this file. From the `@Entity` annotation to the end, this creates a simple Todo data class.
This could be a single line if wished, but I have split it out for clarity, because we have a few annotations on the id field. It is marked as an Entity so that JPA can pick this up, and then the Id and GeneratedValue elements are set to tell the database that this is an auto-increment value. You will also notice that all the values have been given default values; this is because JPA needs a no-args constructor, so giving all elements a default value achieves this in the background.

Above the Entity annotation is the repository. The JPA Repository allows us to use DB helper methods to access our underlying database entities (we will use FindAll, FindById, Save, and DeleteById). In Kotlin, all we need to do is implement the interface (line 35), to extend the JpaRepository, setting the Type as Todo, and Key as a Long.

The remainder of the file is the RestController, which accesses the DB and works with the entities.
- Line 8: This sets up the class as Controller, serving REST requests, with all mappings inside the class being served up from the /todo path.
- Line 9: The class is defined here, with the main thing of note is that the JPARepository is passed in. This achieves the dependency injection required to give us an implementation of our TodoRepository interface.
- Line 11/12: This returns a JSON response of all the Todos in the database. As we are working with a RestController, if the response is a class, it is converted into JSON. This endpoint has been defined in a functional style (no parenthesise or return type, just an equals sign).
- Line 14/15: This function returns a single Todo. The Id is defined as a path variable using the {id} syntax in the GetMapping value, which is then passed in as a parameter into the function. We can then once again is the jpaRepository to return a Todo using the findById function.
- Line 17/18: This function is similar to the previous two, except it is a PostMapping (not Get), and the data is taken from the RequestBody, rathen than a PathVariable. We then use the JPA Repository to save a newly created Todo object, which we create using a named parameter (we only pass in a single parameter, all the others are defaulted). Note that the Id set to default of zero will be ignored by the DB in favour of the auto-generated Id.
- Line 20–26: This function is slightly different as we are not using the functional style (there is too much going on), which means we need a return type and a return statement. Similar to Line 14, we have the {id} passed into the parameter list, along with a Todo object, which is automatically converted from the JSON passed in from the RequestBody. The code then goes onto load the Todo from the Database (using the findById function), update the text and done variables, and then return the saved entity.
- Line 28–32: This function simply takes the {id} from the path and calls the deleteById function, and finally responds with the sameOk text that we returned in the SparkJava implementation.

## Run The Application
To run the application, simply type `mvn spring-boot:run` in a command prompt at the base of the directory, and Maven will do all the necessary to compile, package and run.

## Test The REST API
Go to http://localhost:8080 and use the provided front end. Enter a Todo in the text box and press enter. Done!

Alternatively, if you would rather use CURL
- Create Todo: `curl -d "Hello" -H "Content-Type:application/text" http://localhost:8080/todo/`
- Read Single Todo: `curl http://localhost:8080/todo/1` 
- Update Todo: `curl -X PUT -d "{\"text\":\"Hello\",\"done\":true}" -H "Content-Type: application/json" http://localhost:8080/todo/1`
- List Todo: `curl http://localhost:8080/todo/` 
- Delete Todo: `curl -X DELETE http://localhost:8080/todo/1`