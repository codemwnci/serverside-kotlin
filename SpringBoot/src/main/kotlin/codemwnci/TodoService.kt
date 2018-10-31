package codemwnci

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.bind.annotation.*
import javax.persistence.*
import java.time.Instant


@RestController @RequestMapping(value = "/todo")
class TodoResources(val todoRepo: TodoReposiory) {

    @GetMapping(value = "/")
    fun getAllTodos() = todoRepo.findAll()

    @GetMapping(value = "/{id}")
    fun getOneTodo(@PathVariable id: Long) = todoRepo.findById(id)

    @PostMapping(value = "/")
    fun newTodo(@RequestBody text: String) = todoRepo.save(Todo(text = text))

    @PutMapping(value = "/{id}")
    fun updateTodo(@PathVariable id: Long, @RequestBody todo: Todo): Todo {
        val toUpdate: Todo = todoRepo.findById(id).orElseThrow { Exception("server error") }
        toUpdate.text = todo.text
        toUpdate.done = todo.done
        return todoRepo.save(toUpdate)
    }

    @DeleteMapping(value = "/{id}")
    fun deleteTodo(@PathVariable id: Long): String {
        todoRepo.deleteById(id)
        return "ok"
    }
}

interface TodoReposiory : JpaRepository<Todo, Long>

@Entity
class Todo(@Id @GeneratedValue(strategy = GenerationType.AUTO)
           val id: Long = 0,
           var text: String = "",
           var done: Boolean = false,
           val createdAt: Instant = Instant.now())

