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



## Run The Application





## Test The REST API
Go to http://localhost:8080 and use the provided front end. Enter a Todo in the text box and press enter. Done!

Alternatively, if you would rather use CURL
- Create Todo: `curl -d "Hello" -H "Content-Type:application/text" http://localhost:8080/todo/`
- Read Single Todo: `curl http://localhost:8080/todo/1` 
- Update Todo: `curl -X PUT -d "{\"text\":\"Hello\",\"done\":true}" -H "Content-Type: application/json" http://localhost:8080/todo/1`
- List Todo: `curl http://localhost:8080/todo/` 
- Delete Todo: `curl -X DELETE http://localhost:8080/todo/1`