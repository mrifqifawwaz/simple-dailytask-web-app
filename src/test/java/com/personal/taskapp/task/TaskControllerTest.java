package com.personal.taskapp.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    JDBCTaskRepository repository;

    private final List<Task> tasks = new ArrayList<>();

    @BeforeEach
    void setUp() {
        tasks.add(new Task(1,
        "Day Time Daily Task",
            LocalDateTime.now(),
            LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
            TimeSet.DAY));
    }

    @Test
    void shouldFindAllTasks() throws Exception {
        when(repository.findAll()).thenReturn(tasks);
        mvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(tasks.size())));
    }

    @Test
    void shouldFindOneTask() throws Exception {
        Task task = tasks.get(0);
        when(repository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(task));
        mvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(task.id())))
                .andExpect(jsonPath("$.taskTitle", is(task.taskTitle())))
                .andExpect(jsonPath("$.timeSet", is(task.timeSet().toString())));
    }

    @Test
    void shouldReturnNotFoundWithInvalidId() throws Exception {
        mvc.perform(get("/api/tasks/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewTask() throws Exception {
        var task = new Task(null,"test", LocalDateTime.now(),LocalDateTime.now(), TimeSet.DAY);
        mvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task))
                )
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateTask() throws Exception {
        var task = new Task(null,"test", LocalDateTime.now(),LocalDateTime.now(), TimeSet.DAY);
        mvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task))
                )
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldDeleteTask() throws Exception {
        mvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }

}
