// package com.todo.todolist.controller;

// import com.todo.todolist.model.Task;
// import com.todo.todolist.repository.TaskRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;
// import java.util.stream.Collectors;
// import java.util.Comparator;
 
// @RestController
// @RequestMapping("/api/tasks")
// @CrossOrigin(origins = "*")
// public class TaskController {

//     @Autowired
//     private TaskRepository taskRepository;

//     @PostMapping("/add")
//     public Task addTask(@RequestBody Task task) {
//         return taskRepository.save(task);
//     }

//     @GetMapping("/user/{userId}")
//     public List<Task> getTasks(
//             @PathVariable Long userId,
//             @RequestParam(required = false) String priority,
//             @RequestParam(required = false) String status,
//             @RequestParam(required = false) String sortBy) {
        
//         List<Task> tasks = taskRepository.findByUserId(userId);

//         if (priority != null) {
//             tasks = tasks.stream().filter(t -> t.getPriority().equalsIgnoreCase(priority)).collect(Collectors.toList());
//         }
//         if (status != null) {
//             tasks = tasks.stream().filter(t -> t.getStatus().equalsIgnoreCase(status)).collect(Collectors.toList());
//         }

//         if ("deadline".equalsIgnoreCase(sortBy)) {
//             tasks.sort(Comparator.comparing(Task::getDeadline, Comparator.nullsLast(Comparator.naturalOrder())));
//         } else if ("priority".equalsIgnoreCase(sortBy)) {
//             tasks.sort(Comparator.comparing(Task::getPriority));
//         }

//         return tasks;
//     }

//     @DeleteMapping("/delete/{id}")
//     public String deleteTask(@PathVariable Long id) {
//         taskRepository.deleteById(id);
//         return "Task deleted!";
//     }
// }
// package com.todo.todolist.controller;

// import java.util.List;

// import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.todo.todolist.model.Task;
// import com.todo.todolist.model.User;
// import com.todo.todolist.repository.TaskRepository;
// import com.todo.todolist.repository.UserRepository;

// @RestController
// @RequestMapping("/api/tasks")
// public class TaskController {

//     private final TaskRepository taskRepository;
//     private final UserRepository userRepository;

//     public TaskController(TaskRepository taskRepository, UserRepository userRepository) {
//         this.taskRepository = taskRepository;
//         this.userRepository = userRepository;
//     }

//     @GetMapping
//     public List<Task> getTasks(OAuth2AuthenticationToken token) {
//         String email = token.getPrincipal().getAttribute("email");
//         User user = userRepository.findByEmail(email).orElseThrow();
//         return taskRepository.findByUser(user);
//     }
// }


package com.todo.todolist.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo.todolist.model.Task;
import com.todo.todolist.model.User;
import com.todo.todolist.repository.TaskRepository;
import com.todo.todolist.repository.UserRepository;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskController(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    // ✅ ADD TASK
    @PostMapping("/add")
    public void addTask(@RequestBody TaskRequest req) {

        User user = userRepository.findById(req.userId()).orElseThrow();

        Task task = new Task();
        task.setDescription(req.description());
        task.setPriority(req.priority());
        task.setStatus(req.status());
        task.setDeadline(req.deadline());
        task.setUser(user);

        taskRepository.save(task);
    }

    // ✅ GET USER TASKS
    @GetMapping("/user/{userId}")
    public List<Task> getUserTasks(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return taskRepository.findByUser(user);
    }

    // ✅ DELETE
    @DeleteMapping("/delete/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }


    // 👇 ADD THIS AT BOTTOM OF TaskController.java
record TaskRequest(
    String description,
    String priority,
    String status,
    String deadline,
    Long userId
) {}

}

