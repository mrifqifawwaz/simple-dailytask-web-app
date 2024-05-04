package com.personal.taskapp.task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    
    List<Task> findAll();

    Optional<Task> findById(Integer id);

    void create(Task task);

    void update(Task task, Integer id);

    void delete(Integer id);

    int count();

    void saveAll(List<Task> runs);
}
