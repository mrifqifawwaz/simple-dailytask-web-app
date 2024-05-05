package com.personal.taskapp.task;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerIntTest {
    
    @LocalServerPort
    int randomServerPort;

    RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + randomServerPort);
    }

    
    @Test
    void shouldFindAllTasks() {
        List<Task> tasks = restClient.get()
                        .uri("/api/tasks")
                        .retrieve()
                        .body(new ParameterizedTypeReference<>() {});
        assertEquals(10, tasks.size());
    }

    @Test
    void shouldFindRunById() {
        Task task = restClient.get()
                .uri("/api/tasks/1")
                .retrieve()
                .body(Task.class);

        assertAll(
                () -> assertEquals(1, task.id()),
                () -> assertEquals("Day Task", task.taskTitle()),
                () -> assertEquals("2024-02-20T06:05", task.startedOn().toString()),
                () -> assertEquals("2024-02-20T10:27", task.completedOn().toString()),
                () -> assertEquals(TimeSet.DAY, task.timeSet()));
    }

    @Test
    void shouldCreateNewTask() {
        Task task = new Task(11, "Night Task", LocalDateTime.now(), LocalDateTime.now().plusHours(2), TimeSet.NIGHT);

        ResponseEntity<Void> newTask = restClient.post()
                .uri("/api/tasks")
                .body(task)
                .retrieve()
                .toBodilessEntity();

        assertEquals(201, newTask.getStatusCodeValue());
    }

    @Test
    void shouldUpdateExistingTask() {
        Task task = restClient.get().uri("/api/tasks/1").retrieve().body(Task.class);

        ResponseEntity<Void> updatedTask = restClient.put()
                .uri("/api/tasks/1")
                .body(task)
                .retrieve()
                .toBodilessEntity();

        assertEquals(204, updatedTask.getStatusCodeValue());
    }

    @Test
    void shouldDeleteTask() {
        ResponseEntity<Void> task = restClient.delete()
                .uri("/api/tasks/1")
                .retrieve()
                .toBodilessEntity();

        assertEquals(204, task.getStatusCodeValue());
    }
}
