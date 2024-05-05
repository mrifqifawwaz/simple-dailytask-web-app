package com.personal.taskapp.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InMemoryTaskRepositoryTest {
    
    InMemoryTaskRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTaskRepository();
        repository.create(new Task(1,
                "Day Time Daily Task",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                TimeSet.DAY));

        repository.create(new Task(2,
        "Night Time Daily Task",
                LocalDateTime.now(),
                LocalDateTime.now().plus(60, ChronoUnit.MINUTES),
                TimeSet.NIGHT));
    }

    @Test
    void shouldFindAllTask() {
        List<Task> tasks = repository.findAll();
        assertEquals(2, tasks.size());
    }

    @Test
    void shouldFindTaskWithValidId() {
        var task = repository.findById(1).get();
        assertEquals("Day Time Daily Task", task.taskTitle());
    }

    @Test
    void shouldNotFindTaskWithInvalidId() {
        TaskNotFoundException notFoundException = assertThrows(
                TaskNotFoundException.class,
                () -> repository.findById(3).get()
        );

        assertEquals("Task Not Found", notFoundException.getMessage());
    }

    @Test
    void shouldCreateNewTask() {
        repository.create(new Task(3,
                "Saturday Day Time Task",
                LocalDateTime.now(),
                LocalDateTime.now().plus(60, ChronoUnit.MINUTES),
                TimeSet.DAY));
        List<Task> tasks = repository.findAll();
        assertEquals(3, tasks.size());
    }

    @Test
    void shouldUpdateTask() {
        repository.update(new Task(1,
                "Saturday Day Time Task",
                LocalDateTime.now(),
                LocalDateTime.now().plus(60, ChronoUnit.MINUTES),
                TimeSet.DAY), 1);
        var task = repository.findById(1).get();
        assertEquals("Saturday Day Time Task", task.taskTitle());
        assertEquals(TimeSet.DAY, task.timeSet());
    }

    @Test
    void shouldDeleteTask() {
        repository.delete(1);
        List<Task> tasks = repository.findAll();
        assertEquals(1, tasks.size());
    }
}
