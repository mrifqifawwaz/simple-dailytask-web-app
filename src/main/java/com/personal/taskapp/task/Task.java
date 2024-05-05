package com.personal.taskapp.task;

import java.time.Duration;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;


public record Task(
    Integer id,
    @NotEmpty
    String taskTitle,
    LocalDateTime startedOn,
    LocalDateTime completedOn,
    TimeSet timeSet
) {
    public Task {
    }

    public Duration getDuration() {
        return Duration.between(startedOn, completedOn);
    }
}
