package com.personal.taskapp.task;


import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/tasks")
class TaskController {
    
    private final JDBCTaskRepository taskRepository;

    TaskController(JDBCTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    List<Task>findAll() {
        return taskRepository.findAll();
    }

    @GetMapping("/{id}")
    Task findById(@PathVariable Integer id) {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Run not found.");
        }
        return task.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    void create(@Valid @RequestBody Task task) {
        taskRepository.create(task);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        taskRepository.delete(id);
    }

    List<Task> findByTimeSet(@RequestParam String timeSet) {
        return taskRepository.findByTimeSet(timeSet);
    }
}
