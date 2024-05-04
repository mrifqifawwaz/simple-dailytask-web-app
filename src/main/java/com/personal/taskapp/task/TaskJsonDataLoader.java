package com.personal.taskapp.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class TaskJsonDataLoader implements CommandLineRunner {
    
    private static final Logger log = LoggerFactory.getLogger(TaskJsonDataLoader.class);
    private final ObjectMapper objectMapper;
    private final TaskRepository taskRepository;

    public TaskJsonDataLoader(ObjectMapper objectMapper, @Qualifier("JDBCTaskRepository") TaskRepository taskRepository) {
        this.objectMapper = objectMapper;
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(taskRepository.count() == 0) {
            try (InputStream inputStream = getClass().getResourceAsStream("/data/tasks.json")) {
                Tasks allTasks = objectMapper.readValue(inputStream, Tasks.class);
                log.info("Reading {} tasks from JSON data and saving to in-memory collection.", allTasks.tasks().size());
                taskRepository.saveAll(allTasks.tasks());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read JSON data", e);
            }
        } else {
            log.info("Not loading Tasks from JSON data because the collection contains data.");
        }
    }
}
