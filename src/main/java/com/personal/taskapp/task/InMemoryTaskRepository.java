package com.personal.taskapp.task;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Repository
public class InMemoryTaskRepository {

    private static final Logger log = LoggerFactory.getLogger(InMemoryTaskRepository.class);
    private final List<Task> tasks = new ArrayList<>();

    public List<Task> findAll() {
        return tasks;
    }

    public Optional<Task> findById(Integer id) {
        return Optional.ofNullable(tasks.stream()
                .filter(task -> task.id() == id)
                .findFirst()
                .orElseThrow(TaskNotFoundException::new));
    }

    public void create(Task task) {
        Task newTask = new Task(task.id(),
                task.taskTitle(),
                task.startedOn(),
                task.completedOn(),
                task.timeSet());

        tasks.add(newTask);
    }

    public void update(Task newTask, Integer id) {
        Optional<Task> existingTask = findById(id);
        if(existingTask.isPresent()) {
            var r = existingTask.get();
            log.info("Updating Existing Task: " + existingTask.get());
            tasks.set(tasks.indexOf(r),newTask);
        }
    }

    public void delete(Integer id) {
        log.info("Deleting Task: " + id);
        tasks.removeIf(task -> task.id().equals(id));
    }

    public int count() {
        return tasks.size();
    }

    public void saveAll(List<Task> runs) {
        runs.stream().forEach(run -> create(run));
    }

    public List<Task> findByTimeSet(String timeSet) {
        return tasks.stream()
                .filter(task -> Objects.equals(task.timeSet(), timeSet))
                .toList();
    }

    @PostConstruct
    private void init() {
        tasks.add(new Task(1,
                "Day Time Daily Task",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                TimeSet.DAY));

        tasks.add(new Task(2,
                "Night Time Daily Task",
                LocalDateTime.now(),
                LocalDateTime.now().plus(60, ChronoUnit.MINUTES),
                TimeSet.NIGHT));
    }



}
