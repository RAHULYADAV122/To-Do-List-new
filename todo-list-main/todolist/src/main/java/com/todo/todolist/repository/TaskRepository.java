package com.todo.todolist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todo.todolist.model.Task;
import com.todo.todolist.model.User;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
}
